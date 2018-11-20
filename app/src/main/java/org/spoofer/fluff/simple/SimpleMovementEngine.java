package org.spoofer.fluff.simple;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import org.spoofer.fluff.Bot;
import org.spoofer.fluff.Movement;
import org.spoofer.fluff.MovementEngine;
import org.spoofer.fluff.Scene;

import java.util.HashMap;
import java.util.Map;

public class SimpleMovementEngine implements MovementEngine {

    private final Map<Bot, Animator> performances = new HashMap<>();
    private final Map<Bot, Bot> collisions = new HashMap<>();

    private Scene scene;




    public void moveBot(@IdRes int botId, Movement.Direction direction) {
        if (direction == Movement.Direction.Stop)
            stopBot(botId);

        if (null == scene)
            return;

        Bot bot = scene.getBot(botId);
        if (null != bot)
            moveBotInside(bot, direction, scene.getSceneSize());
    }


    public void stopBot(@IdRes int botId) {
        final Bot bot = null != scene ? scene.getBot(botId) : null;
        Animator animator = performances.get(bot);
        if (null != animator) {
            animator.cancel();
            performances.remove(bot);
        }
    }

    public void stopAll() {
        for (Animator animator : performances.values()) {
            animator.cancel();
        }
        performances.clear();
        collisions.clear();
        scene = null;
    }

    public void setScene(Scene scene) {
        if (null != this.scene)
            stopAll();
        this.scene = scene;
    }


    public boolean isPerforming() {
        return !performances.isEmpty();
    }


    private void moveBotInside(Bot actor, Movement.Direction direction, Rect boundary) {

        int distance;

        Rect botLoc = actor.getLocation();

        switch (direction) {
            case Left:
                distance = botLoc.left > boundary.left ? botLoc.left - boundary.left : 0;
                break;
            case Right:
                distance = botLoc.right < boundary.right ? boundary.right - botLoc.right : 0;
                break;
            case Up:
                distance = botLoc.top > boundary.top ? botLoc.top - boundary.top : 0;
                break;
            case Down:
                distance = botLoc.bottom < boundary.bottom ? boundary.bottom - botLoc.bottom : 0;
                break;

            default:
                distance = 0;
        }

        Movement movement = new SimpleMovement(actor.getLocation(), direction, distance);
        if (movement.isStill())
            return;

        Animator animator = buildAnimator(actor, movement);
        performances.put(actor, animator);

        animator.start();
    }



    private Bot checkCollision(Bot movingBot) {
        return null;
    }


    private Animator buildAnimator(final Bot bot, Movement movement) {
        ImageView view = bot.getView();

        if (movement.isStill())
            return null;

        String propertyName;
        float endLocation;
        if (movement.isHorizontal()) {
            propertyName = "translationX";
            endLocation = view.getTranslationX() + movement.getRelativeDistance();
        } else {
            propertyName = "translationY";
            endLocation = view.getTranslationY() + movement.getRelativeDistance();
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, propertyName, endLocation);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Bot collision = checkCollision(bot);
                if (null != collision) {
                    collisions.put(bot, collision);
                } else
                    collisions.remove(bot);
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                performances.remove(bot);

            }
        });

        animator.setDuration(movement.getDuration());
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private Movement findClosestBot(Bot actor, Movement.Direction direction) {

        // Find closest bot, in the direction of travel;
        Movement closest = null;
        Movement movement = new SimpleMovement(actor.getLocation());

        Rect actorPath = calculatePath(actor.getLocation(), direction);

        for (Bot bot : scene.getBots()) {
            if (bot == actor)
                continue;

            Rect botLoc = bot.getLocation();
            if (botLoc.intersects(botLoc, actorPath)) {
                movement.setEndLocation(botLoc);
                if (movement.getDirection() == direction && (null == closest || closest.getDistance() > movement.getDistance())) {
                    closest = new SimpleMovement(movement);
                }
            }
        }
        return closest;
    }
    private Rect calculatePath(Rect start, Movement.Direction direction) {
        Rect sceneLoc = scene.getSceneSize();
        Rect path = new Rect(start);
        switch (direction) {
            case Left:
                path.left = sceneLoc.left;
                break;
            case Right:
                path.right = sceneLoc.right;
                break;
            case Down:
                path.bottom = sceneLoc.bottom;
                break;
            case Up:
                path.top = sceneLoc.top;
                break;
        }
        return path;
    }

}
