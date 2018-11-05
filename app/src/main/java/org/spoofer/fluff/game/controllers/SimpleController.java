package org.spoofer.fluff.game.controllers;

import org.spoofer.fluff.game.Director;
import org.spoofer.fluff.game.misc.Movement;
import org.spoofer.fluff.game.Scene;

public abstract class SimpleController implements Controller {

    private final Director director;

    private Scene scene;

    public SimpleController(Director director) {
        this.director = director;
    }

    @Override
    public abstract void setDirection(Movement.Direction direction);


    public boolean isActive() {
        return null != scene;
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public void sceneStarting(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void sceneEnding(Scene scene) {
        this.scene = null;
    }


    public Director getDirector() { return director;}
}
