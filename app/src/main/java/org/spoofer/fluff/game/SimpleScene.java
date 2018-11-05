package org.spoofer.fluff.game;

import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.spoofer.fluff.BuildConfig;
import org.spoofer.fluff.R;
import org.spoofer.fluff.utils.BotConfigReader;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class SimpleScene implements Scene {

    private static final String LOGTAG = SimpleScene.class.getName();

    private static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    private static final int SCENERY_SEARCH_AREA = 10; // size of area around actor to lok for scenery.

    private final ViewGroup sceneView;

    private final Set<Bot> bots = new HashSet<>();
    private final Set<Scenery> sceneries = new HashSet<>();


    public SimpleScene(ViewGroup gameboard) {
        if (null == gameboard)
            throw new NullPointerException("Scene ViewGroup parent can not be null");
        this.sceneView = gameboard;
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

    @Override
    public void setSceneView(ViewGroup sceneView) {
        bots.clear();
        sceneries.clear();

        try {
            BotConfigReader botConfigReader = new BotConfigReader().loadConfig(sceneView.getResources(), R.xml.bots);
            loadBots(sceneView, botConfigReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBots(ViewGroup parent, BotConfigReader botConfigReader) {
        for (int index = 0; index < parent.getChildCount(); index++) {
            View view = parent.getChildAt(index);

            if (view instanceof ViewGroup) // Recusive call with group views to iterate their child views
                loadBots((ViewGroup) view, botConfigReader);

            else if (botConfigReader.contains(view.getId())) {
                Bot bot = createBotFromView(view, botConfigReader.getBotClass(view.getId()));
                if (null != bot) {

                    bots.add(bot);

                    if (bot instanceof Scenery)
                        sceneries.add((Scenery) bot);
                }
            }
        }
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
