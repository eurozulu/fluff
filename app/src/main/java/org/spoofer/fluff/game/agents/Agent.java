package org.spoofer.fluff.game.agents;

import android.support.annotation.IdRes;

import org.spoofer.fluff.game.Director;

/**
 * An Agent is used to control a single Bot based on its View Id.
 * The Agent implementations use the Agents Director to request actions on behalf of the Bot.
 */
public interface Agent {

    @IdRes
    int getActorId();

    Director getDirector();


}
