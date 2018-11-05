package org.spoofer.fluff.game;

import android.animation.Animator;

import java.util.HashMap;
import java.util.Map;

public class SimpleDirector implements Director {

    private static final String LOGTAG = SimpleDirector.class.getName();

    private final Map<Bot, Animator> animators = new HashMap<>();

    @Override
    public void startAction(Bot bot, Animator animator) {
        stopAction(bot);
        animators.put(bot, animator);
        animator.start();
    }

    @Override
    public void stopAction(Bot bot) {
        Animator animator = animators.remove(bot);
        if (null != animator)
            animator.cancel();
    }

    @Override
    public void stopAll() {
        for (Animator animator : animators.values())
            animator.cancel();
        animators.clear();
    }
}
