package org.spoofer.fluff.game.controllers;

import android.support.annotation.IdRes;

import org.spoofer.fluff.game.Bot;
import org.spoofer.fluff.game.Director;
import org.spoofer.fluff.game.Scene;
import org.spoofer.fluff.game.actions.MoveAction;
import org.spoofer.fluff.game.misc.Movement;
import org.spoofer.fluff.game.misc.SimpleMovement;

public class SingleBotController extends SimpleController {

    @IdRes
    private final int actorId;
    private Bot target;

    public SingleBotController(Director director, int actorId) {
        super(director);
        this.actorId = actorId;
    }


    @Override
    public void setDirection(Movement.Direction direction) {
        if (!isActive())
            return;

        getDirector().startAction(target, new MoveAction(direction));

    }

    @Override
    public boolean isActive() {
        return super.isActive() && null != target;
    }

    @Override
    public void sceneStarting(Scene scene) {
        super.sceneStarting(scene);
        this.target = scene.findBot(actorId);
    }

    @Override
    public void sceneEnding(Scene scene) {
        super.sceneEnding(scene);
        target = null;
    }

}
