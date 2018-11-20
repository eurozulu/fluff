package org.spoofer.fluff;

import org.spoofer.fluff.game.Bot;
import org.spoofer.fluff.game.actions.Action;

/**
 * The Script defines which Actions are carries out by which actors / Scenery
 */
public interface Script {

    Action getAction(Bot bot, String action);

}
