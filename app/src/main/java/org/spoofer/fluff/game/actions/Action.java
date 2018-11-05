package org.spoofer.fluff.game.actions;

import android.animation.Animator;

import org.spoofer.fluff.game.Bot;

public interface Action {

    Animator performAction(Bot bot);

}
