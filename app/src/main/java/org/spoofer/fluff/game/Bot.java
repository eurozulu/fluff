package org.spoofer.fluff.game;

import android.graphics.Rect;
import android.view.View;

/**
 * A Bot is the base object of all Game parts in each level.
 * Bots encapsulate a single {@link View} on screen.
 * The View is manipulated with {@link android.animation.Animator}s to portray the Bot in the game.
 */
public interface Bot {

    /**
     * Gets the View that represents this Bot
     *
     * @return
     */
    View getView();

    /**
     * Gets the current on screen location  of the Bot.
     * More specifically, gets the Bots View location, relative to the screen.
     *
     * @return
     */
    Rect getLocation();
}
