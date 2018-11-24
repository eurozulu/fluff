package org.spoofer.fluff.simple;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.spoofer.fluff.Bot;
import org.spoofer.fluff.BuildConfig;
import org.spoofer.fluff.Scene;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleScene implements Scene {

    private static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    private final Map<Integer, Bot> bots = new HashMap<>();

    private final ViewGroup sceneView;


    public Collection<Bot> getBots() {
        return bots.values();
    }

    public Bot getBot(@IdRes int id) {
        return bots.get(id);
    }

    public ViewGroup getSceneView() {
        return sceneView;
    }

    public SimpleScene(ViewGroup sceneView) {
        this.sceneView = sceneView;
    }

    public Rect getSceneSize() {
        View view = getSceneView();
        int[] outLocation = new int[2];
        view.getLocationInWindow(outLocation);
        return new Rect(outLocation[0], outLocation[1],
                outLocation[0] + view.getWidth(), outLocation[1] + view.getHeight());
    }

    public void initalise() {
        bots.clear();
        loadScene(sceneView);
    }


    private void loadScene(ViewGroup parent) {

        for (int index = 0; index < parent.getChildCount(); index++) {
            View view = parent.getChildAt(index);

            if (view instanceof ViewGroup)
                loadScene((ViewGroup) view);

            else {
                Bot bot = createBotFromView(view);
                if (null != bot)
                    bots.put(view.getId(), bot);
            }
        }
    }

    private Bot createBotFromView(View view) {
        if (!(view instanceof ImageView))
            return null;

        int id = view.getId();
        if (id == View.NO_ID || id == 0)
            return null;

        Resources resources = view.getResources();
        String id_name = resources.getResourceEntryName(id);
        String className = lookupStringByName(id_name, resources);
        if (null == className || className.equals(""))
            return null;

        try {
            Class<? extends Bot> botClass = Class.forName(className).asSubclass(Bot.class);
            Constructor<? extends Bot> constructor = botClass.getConstructor(View.class);

            return constructor.newInstance(view);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String lookupStringByName(String name, Resources resources) {
        int id = resources.getIdentifier(name, "string", PACKAGE_NAME);
        return id != 0 ? resources.getString(id) : null;

    }

}
