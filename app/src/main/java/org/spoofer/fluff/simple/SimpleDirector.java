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
import org.spoofer.fluff.Director;
import org.spoofer.fluff.Movement;
import org.spoofer.fluff.Scene;
import org.spoofer.fluff.Scenery;

import java.util.HashMap;
import java.util.Map;

public class SimpleDirector implements Director {

    private final Map<Bot, Animator> performances = new HashMap<>();
    private final Map<Bot, Bot> collisions = new HashMap<>();

    private Scene scene;


    public void moveBot(@IdRes int botId, Movement.Direction direction) {
        if (direction == Movement.Direction.Stop)
            stopBot(botId);

        if (null == scene)
            return;

        Bot bot = scene.getBot(botId);
        if (null == bot)
            return;

        Movement movement = moveBotInside(bot, direction, scene.getSceneSize());
        if (movement.isStill())
            return;

        moveBot(bot, movement);
    }


    public void stopBot(@IdRes int botId) {
        Bot bot = null != scene ? scene.getBot(botId) : null;
        if (null != bot)
            stopBot(bot);
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

    @Override
    public boolean isInCollision(@IdRes int id) {
        Bot bot = scene.getBot(id);
        return collisions.containsKey(bot);
    }


    private void moveBot(Bot actor, Movement movement) {
        stopBot(actor);
        Animator animator = buildAnimator(actor, movement);
        performances.put(actor, animator);

        animator.start();
    }


    // Move the bot to the opposite edge of the direction of travel.
    // i.e. if direction =Left, move to right side, if Up, move to Bottom.
    private Movement moveBotOutside(Bot actor, Movement.Direction direction, Rect boundary) {

        Rect botLoc = actor.getLocation();
        Rect newLoc = new Rect(botLoc);

        switch (direction) {
            case Left:
                newLoc.offset(boundary.right - botLoc.left, 0);
                break;
            case Right:
                newLoc.offset(boundary.left - botLoc.right, 0);
                break;
            case Up:
                newLoc.offset(0, boundary.bottom - botLoc.top);
                break;
            case Down:
                newLoc.offset(0, boundary.top - botLoc.bottom);
                break;
        }

        Movement movement = new SimpleMovement(botLoc);
        movement.setEndLocation(newLoc);

        return movement;
    }


    private Movement moveBotInside(Bot actor, Movement.Direction direction, Rect boundary) {

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

        return new SimpleMovement(actor.getLocation(), direction, distance);
    }


    private void stopBot(Bot bot) {
        Animator animator = performances.remove(bot);
        if (null != animator)
            animator.cancel();
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
                    if (collisions.put(bot, collision) != collision)
                        if (collision instanceof Scenery) {
                            Movement newMovement = ((Scenery) collision).moveCollision(bot, movement);
                            if (newMovement != movement)
                                moveBot(bot, newMovement);
                        }

                } else if (movement.getDirection() != Movement.Direction.Stop)
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

    private Bot checkCollision(Bot actor) {
        Bot found = null;

        Rect actorLocation = actor.getLocation();

        for (Bot bot : scene.getBots()) {
            if (Rect.intersects(bot.getLocation(), actorLocation)) {
                found = bot;
                break;
            }
        }
        return found;
    }

    /**
     * Gets the next bot the given actor will collide with
     *
     * @param actor
     * @param direction
     * @return
     * /
    private Bot getNextCollision(Bot actor, Movement.Direction direction) {

    // Find closest bot, in the direction of travel;
    Bot closestBot = null;
    Movement closest = null;
    Rect widenedLoc = actor.getLocation();
    //widenedLoc.inset(-10, -10);
    Movement movement = new SimpleMovement(actor.getLocation());

    Rect actorPath = calculatePath(widenedLoc, direction);

    for (Bot bot : scene.getBots()) {
    if (bot == actor)
    continue;

    Rect botLoc = bot.getLocation();
    if (botLoc.intersects(botLoc, actorPath)) {
    movement.setEndLocation(botLoc);
    if (null == closest || closest.getDistance() > movement.getDistance()) {
    closest = new SimpleMovement(movement);
    closestBot = bot;
    }
    }
    }
    return closestBot;
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

     */

}
