package org.spoofer.fluff.game.misc;

import android.animation.Animator;
import android.animation.AnimatorSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AnimatorBuilder {

    private final Set<Animator> animators = new HashSet<>();

    public AnimatorBuilder() {
    }

    public AnimatorBuilder(Animator... animators) {
        this(Arrays.asList(animators));
    }

    public AnimatorBuilder(Collection<Animator> animators) {
        this.animators.addAll(animators);
    }


    public Animator build() {
        if (animators.isEmpty())
            return null;

        if (animators.size() == 1)
            return animators.iterator().next();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        return animatorSet;
    }

    public void add(Animator animator) {
        if (null != animator)
            animators.add(animator);
    }

    public void addAll(Collection<Animator> animators) {
        if (null != animators)
            this.animators.addAll(animators);
    }

    public boolean isEmpty() {
        return animators.isEmpty();
    }

    public void clear() {
        animators.clear();
    }
}
