package org.spoofer.fluff.game;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.spoofer.fluff.BuildConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class SimpleScene implements Scene {

    private static final String LOGTAG = SimpleScene.class.getName();

    private static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    private static final int SCENERY_SEARCH_AREA = 10; // size of area around actor to lok for scenery.

    private final Set<Bot> bots = new HashSet<>();
    private final Set<Scenery> sceneries = new HashSet<>();

    private ViewGroup sceneView;


    public SimpleScene() {
        this(null);
    }

    public SimpleScene(ViewGroup sceneView) {
        setSceneView(sceneView);
    }

    @Override
    public ViewGroup getSceneView() {
        return sceneView;
    }

    @Override
    public Set<Bot> getBots() {
        return bots;
    }

    @Override
    public Set<Scenery> getScenery() {
        return sceneries;
    }

    @Override
    public Bot findBot(@IdRes int id) {
        return sceneView.findViewById(id);
    }

    @Override
    public Scenery findScenery(Rect searchArea) {
        Scenery sceneryFound = null;

        for (Scenery scenery : getScenery()) {
            if (searchArea.intersect(scenery.getLocation()))
                sceneryFound = getClosestToCenter(searchArea, sceneryFound, scenery);
        }
        return sceneryFound;
    }


    public void setSceneView(ViewGroup sceneView) {
        bots.clear();
        sceneries.clear();

        if (null == sceneView)
            return;

        loadBots(sceneView);
    }


    private void loadBots(ViewGroup parent) {
        for (int index = 0; index < parent.getChildCount(); index++) {
            View view = parent.getChildAt(index);

            if (view instanceof ViewGroup) // Recusive call with group views to iterate their child views
                loadBots((ViewGroup) view);

            else if (View.NO_ID != view.getId() && 0 != view.getId()) {
                Bot bot = createBotFromView(view, getBotClass(view.getId(), view.getResources()));

                if (null != bot) {

                    bots.add(bot);

                    if (bot instanceof Scenery)
                        sceneries.add((Scenery) bot);
                }
            }
        }
    }


    private Class<? extends Bot> getBotClass(@IdRes int id, Resources resources) {

        Class<? extends Bot> botClass = null;

        try {
            // Match given ID name with string ID name
            String name = resources.getResourceName(id);
            int szId = resources.getIdentifier(name, "string", PACKAGE_NAME);
            String className = resources.getString(szId);

            botClass = Class.forName(className).asSubclass(Bot.class);

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return botClass;
    }

    private Bot createBotFromView(View view, Class<? extends Bot> botClass) {
        Bot bot = null;

        if (null != botClass) {
            Log.d(LOGTAG, String.format("view %s has been bound to %s Bot class", view, botClass.getSimpleName()));

            try {
                Constructor<? extends Bot> botConstructor = botClass.getConstructor(View.class);
                bot = botConstructor.newInstance(view);

            } catch (NoSuchMethodException e) {
                Log.e(LOGTAG, String.format("Bot class %s is not valid as it has no constructor with a View class paramaeter", botClass.getName()), e);
            } catch (IllegalAccessException e) {
                Log.e(LOGTAG, String.format("Failed to instanciate Bot class %s", botClass.getName()), e);
            } catch (InstantiationException e) {
                Log.e(LOGTAG, String.format("Failed to instanciate Bot class %s", botClass.getName()), e);
            } catch (InvocationTargetException e) {
                Log.e(LOGTAG, String.format("Failed to instanciate Bot class %s", botClass.getName()), e);
            }
        }
        return bot;
    }


    private Scenery getClosestToCenter(Rect location, Scenery... sceneries) {
        Scenery sceneryFound = null;
        for (Scenery scenery : sceneries) {
            if (null == scenery)
                continue;
            if (null == sceneryFound)
                sceneryFound = scenery;
            else {
                int foundDist = getShortestDistance(location, sceneryFound.getLocation());
                int newDist = getShortestDistance(location, scenery.getLocation());
                sceneryFound = newDist < foundDist ? scenery : sceneryFound;
            }
        }
        return sceneryFound;
    }

    private int getShortestDistance(Rect r1, Rect r2) {
        int xDiff = Math.abs(r1.centerX() - r2.centerX());
        int yDiff = Math.abs(r1.centerY() - r2.centerY());
        return Math.min(xDiff, yDiff);
    }

}
