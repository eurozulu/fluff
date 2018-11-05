package org.spoofer.fluff.game.controllers;

import org.spoofer.fluff.game.Game;
import org.spoofer.fluff.game.misc.Movement;

public interface Controller extends Game.SceneListener {

    void setDirection(Movement.Direction direction);
}
