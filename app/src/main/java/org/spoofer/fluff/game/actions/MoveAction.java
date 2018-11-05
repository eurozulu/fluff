package org.spoofer.fluff.game.actions;

import android.animation.Animator;
import android.animation.ObjectAnimator;

import org.spoofer.fluff.game.Bot;
import org.spoofer.fluff.game.misc.AnimatorBuilder;
import org.spoofer.fluff.game.misc.Movement;
import org.spoofer.fluff.game.misc.SimpleMovement;

public class MoveAction implements Action {

    private final Movement.Direction direction;

    public MoveAction(Movement.Direction direction) {
        this.direction = direction;
    }

    Movement.Direction getDirection() {
        return direction;
    }

    @Override
    public Animator performAction(Bot bot) {
        Movement movement = new SimpleMovement(bot.getLocation(), getDirection());
        AnimatorBuilder builder = new AnimatorBuilder();
        if (movement.isHorizontal())
            builder.add(ObjectAnimator.ofFloat(bot.getView(), "translationX", movement.getToLocation().left));

        if (movement.isVertical())
            builder.add(ObjectAnimator.ofFloat(bot.getView(), "translationY", movement.getToLocation().top));

        return builder.build();
    }
}
