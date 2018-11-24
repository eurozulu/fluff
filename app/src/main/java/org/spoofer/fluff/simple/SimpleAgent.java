package org.spoofer.fluff.simple;

import android.support.annotation.IdRes;

import org.spoofer.fluff.Agent;
import org.spoofer.fluff.Movement;
import org.spoofer.fluff.Director;

public class SimpleAgent implements Agent {

    @IdRes
    private final int id;

    private final Director director;

    public SimpleAgent(int id, Director director) {
        this.id = id;
        this.director = director;
    }

    @Override
    public int getBotId() {
        return id;
    }

    @Override
    public void move(Movement.Direction direction) {
        director.moveBot(this, direction);
    }

}
