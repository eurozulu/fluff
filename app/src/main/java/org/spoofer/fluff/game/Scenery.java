package org.spoofer.fluff.game;

import android.graphics.Rect;

import org.spoofer.fluff.game.misc.Movement;

public interface Scenery extends Bot {

    Movement getMovement(Rect fromLocation, Movement.Direction direction);
}
