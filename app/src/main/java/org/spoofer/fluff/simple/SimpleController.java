package org.spoofer.fluff.simple;

import android.support.annotation.IdRes;

import org.spoofer.fluff.Controller;
import org.spoofer.fluff.Movement;
import org.spoofer.fluff.Director;

public class SimpleController implements Controller {

    @IdRes
    private final int id;

    private final Director director;

    public SimpleController(int id, Director director) {
        this.id = id;
        this.director = director;
    }

    @Override
    public int getBotId() {
        return id;
    }

    @Override
    public void move(Movement.Direction direction) {
        director.moveBot(getBotId(), direction);
    }

    protected Director getDirector() { return director;}
}
