package org.spoofer.fluff.game;

import org.spoofer.fluff.game.actions.Action;

public interface Scenery extends Bot {

    Action getAction(Bot actor, String action);
}
