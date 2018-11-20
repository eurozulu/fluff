package org.spoofer.fluff.game.agents;

import android.support.annotation.IdRes;

import org.spoofer.fluff.game.Director;

public class SimpleAgent implements Agent {

    private final Director director;

    @IdRes
    private final int actorId;

    public SimpleAgent(Director director, @IdRes int actorId) {
        this.director = director;
        this.actorId = actorId;
    }

    @Override
    public int getActorId() {
        return actorId;
    }

    @Override
    public Director getDirector() {
        return director;
    }

    public void performCommand(String command) {
        getDirector().startAction(getActorId(), command);
    }
}
