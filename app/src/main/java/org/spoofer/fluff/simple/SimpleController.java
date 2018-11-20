package org.spoofer.fluff.simple;

import android.support.annotation.IdRes;

import org.spoofer.fluff.Controller;
import org.spoofer.fluff.Movement;
import org.spoofer.fluff.MovementEngine;

public class SimpleController implements Controller {

    @IdRes
    private final int id;

    private final MovementEngine movementEngine;

    public SimpleController(int id, MovementEngine movementEngine) {
        this.id = id;
        this.movementEngine = movementEngine;
    }

    @Override
    public int getBotId() {
        return id;
    }

    @Override
    public void move(Movement.Direction direction) {
        movementEngine.moveBot(getBotId(), direction);
    }

    protected MovementEngine getMovementEngine() { return movementEngine;}
}
