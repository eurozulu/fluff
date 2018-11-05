package org.spoofer.fluff.utils;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.XmlRes;

import org.spoofer.fluff.BuildConfig;
import org.spoofer.fluff.game.Bot;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BotConfigReader {


    private static final String TAG_BOT = "bot";
    private static final String TAG_ACTIONS = "actions";
    private static final String TAG_ACTION = "actions";

    private static final String ATTR_ID = "id";
    private static final String ATTR_CLASSNAME = "class";

    private static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;


    private final Map<String, BotConfig> botConfigs = new HashMap<>();



    class BotConfig {
        private final int viewId;
        private final Map<String, int[]> actions = new HashMap<>();
        private final Class<? extends Bot> botClass;

        public BotConfig(int viewId, Class<? extends Bot> botClass) {
            this.viewId = viewId;
            this.botClass = botClass;
        }

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

    public boolean contains(String name) {
        return botConfigs.containsKey(name);
    }
    public boolean contains(@IdRes int id) {
        return getById(id) != null;
    }

    public Class<? extends Bot> getBotClass(@IdRes int id) {
        BotConfig config = getById(id);
        return null != config ? config.botClass : null;
    }

    @DrawableRes
    public int[] getBotFrames(String name, String action) {
        return botConfigs.containsKey(name) ? botConfigs.get(name).getActions().get(action) : null;
    }


    public BotConfigReader loadConfig(Resources resources, @XmlRes int configId) throws IOException {

        botConfigs.clear();

        XmlResourceParser parser = resources.getXml(configId);
        try {
            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String name = parser.getName();

                    if (TAG_BOT.equals(name))
                        readBotTag(parser);

                }
                parser.nextTag();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return this;
    }

    private BotConfig readBotTag(XmlResourceParser parser, Resources resources) {

        @IdRes
        int botId = 0;
        String idName = parser.getAttributeValue(null, ATTR_ID);

        if (null != idName && idName.length() > 0)
            botId = resources.getIdentifier(idName, "id", PACKAGE_NAME);

        Class<? extends Bot> botClass = readBotClass(parser);

        if (0 != botId && null != botClass)
            botClasses.put(id, botClass);

    }


    private BotConfig getById(@IdRes int id) {
        BotConfig found = null;
        for (BotConfig config : botConfigs.values()) {
            if (config.viewId == id) {
                found = config;
                break;
            }
        }
        return found;
    }



    private Class<? extends Bot> readBotClass(XmlResourceParser parser) throws IOException, XmlPullParserException {
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


}
