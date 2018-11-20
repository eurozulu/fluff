package org.spoofer.fluff.game.misc;

import android.graphics.Rect;

import org.spoofer.fluff.game.actions.Action;

public interface Movement {

    int VELOCITY = 250;  // points / second


    enum Direction {
        Left,
        Right,
        Up,
        Down,
        Stop
    }

    boolean isStill();

    boolean isHorizontal();
    boolean isVertical();

    Rect getFromLocation();

    Rect getToLocation();

    Direction getDirection();

    long getDistance();

    int getVelocity();
}
