package org.spoofer.fluff.game.controllers;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.IdRes;

import org.spoofer.fluff.game.Bot;
import org.spoofer.fluff.game.Scene;
import org.spoofer.fluff.game.Scenery;
import org.spoofer.fluff.game.misc.Movement;

import java.util.HashMap;
import java.util.Map;

public class MultiBotControler extends SimpleController {

    private final Map<String, Bot> monsters = new HashMap<>();

    private final String idName;


    public MultiBotControler(String idName) {
        this.idName = idName;
    }


    @Override
    public boolean isActive() {
        return super.isActive() && !monsters.isEmpty();
    }

    @Override
    public void setDirection(Movement.Direction direction) {
        if (!isActive())
            return;

        for (Bot bot : monsters.values()) {

            Rect botLocation = bot.getLocation();
            Scenery scenery = getLocalScenery(botLocation);
            if (null == scenery)
                continue;

            Movement movement = scenery.getMovement(botLocation, direction);
            if (!movement.isStill())
                performMovement(bot, movement);

        }
    }

    @Override
    public void sceneStarting(Scene scene) {
        super.sceneStarting(scene);
        this.monsters.clear();

        Resources resources = scene.getSceneView().getResources();

        for (Bot bot : scene.getBots()) {
            @IdRes int id = bot.getView().getId();
            String name;
            try {
                name = resources.getResourceEntryName(id);
                if (null != name && name.startsWith(idName))
                    monsters.put(name, bot);

            } catch (Resources.NotFoundException e) {
                continue;
            }
        }
    }

    @Override
    public void sceneEnding(Scene scene) {
        super.sceneEnding(scene);
        this.monsters.clear();
    }

}
