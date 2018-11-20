package org.spoofer.fluff.utils;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.XmlRes;
import android.util.Log;

import org.spoofer.fluff.game.Bot;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotConfigReader {

    private static final String LOGTAG = BotConfigReader.class.getName();


    private static final String TAG_BOT = "bot";
    private static final String TAG_ACTIONS = "actions";
    private static final String TAG_ACTION = "action";
    private static final String TAG_FRAME = "frame";

    private static final String ATTR_ID = "id";
    private static final String ATTR_CLASSNAME = "class";
    private static final String ATTR_ACTION_NAME = "name";


    private final Map<Integer, BotConfig> botConfigs = new HashMap<>();


    class BotConfig {
        @IdRes
        private final int viewId;

        private final Map<String, int[]> actions = new HashMap<>();
        private final Class<? extends Bot> botClass;

        public BotConfig(int viewId, Class<? extends Bot> botClass) {
            this.viewId = viewId;
            this.botClass = botClass;
        }

        @IdRes
        public int getViewId() {
            return viewId;
        }

        public Map<String, int[]> getActions() {
            return actions;
        }

        public Class<? extends Bot> getBotClass() {
            return botClass;
        }
    }

    public boolean contains(@IdRes int id) {
        return botConfigs.containsKey(id);
    }


    public Class<? extends Bot> getBotClass(@IdRes int id) {
        BotConfig config = botConfigs.get(id);
        return null != config ? config.botClass : null;
    }

    @DrawableRes
    public int[] getBotFrames(@IdRes int id, String action) {
        return botConfigs.containsKey(id) ? botConfigs.get(id).getActions().get(action) : null;
    }


    public BotConfigReader loadConfig(Resources resources, @XmlRes int configId) throws IOException {

        botConfigs.clear();

        XmlResourceParser parser = resources.getXml(configId);
        try {
            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG && TAG_BOT.equals(parser.getName())) {
                    BotConfig config = readBotTag(parser);
                    if (null != config)
                        botConfigs.put(config.viewId, config);
                }
                event = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return this;
    }

    private BotConfig readBotTag(XmlResourceParser parser) throws XmlPullParserException, IOException {

        BotConfig botConfig;

        @IdRes
        int botId = parser.getIdAttributeResourceValue(0);
        Class<? extends Bot> botClass = readBotClass(parser);

        if (0 != botId && null != botClass) {
            botConfig = new BotConfig(botId, botClass);
            readBotActions(parser, botConfig.getActions());

        } else {
            Log.d(LOGTAG, String.format("Ignoring bot config as either ID (%s) is invalid or no class (%s) could be loaded",
                    botId, botClass));
            botConfig = null;
        }

        return botConfig;
    }


    private void readBotActions(XmlResourceParser parser, Map<String, int[]> frames) throws XmlPullParserException, IOException {
        int event = parser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG) {
                String name = parser.getName();

                if (TAG_ACTION.equals(name)) {
                    BotAction action = readBotAction(parser);
                    if (null != action)
                        frames.put(action.name, action.frames);
                }
            } else if (event == XmlPullParser.END_TAG && TAG_ACTIONS.equals(parser.getName()))
                break;

            event = parser.next();
        }

    }

    private BotAction readBotAction(XmlResourceParser parser) throws XmlPullParserException, IOException {
        BotAction action = new BotAction();
        action.name = parser.getAttributeValue(null, ATTR_ACTION_NAME);


        List<Integer> frames = new ArrayList<>();
        int event = parser.next();
        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG && TAG_FRAME.equals(parser.getName())) {
                int id = parser.getIdAttributeResourceValue(0);
                if (id != 0)
                    frames.add(id);
            }

            event = parser.next();

            if (event == XmlPullParser.END_TAG && TAG_ACTION.equals(parser.getName()))
                break;

        }

        Integer[] frameArray = new Integer[frames.size()];
        frames.toArray(frameArray);
        action.frames = frameArray;

        return action;
    }

    private Class<? extends Bot> readBotClass(XmlResourceParser parser) {
        Class<? extends Bot> botClass = null;

        String class_name = parser.getAttributeValue(null, ATTR_CLASSNAME);
        if (null != class_name && class_name.length() > 0)
            try {
                botClass = Class.forName(class_name).asSubclass(Bot.class);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                botClass = null;
            }
        return botClass;
    }


    private class BotAction {
        String name;
        int[] frames;
    }
}
