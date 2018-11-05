package org.spoofer.fluff.game;

import org.spoofer.fluff.game.actions.Action;

public interface Director {

    void startAction(Bot bot, Action action);
    void stopAction(Bot bot);

    void stopAll();
}
