package org.spoofer.fluff.simple;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import org.spoofer.fluff.Agent;
import org.spoofer.fluff.Bot;
import org.spoofer.fluff.Director;
import org.spoofer.fluff.Movement;
import org.spoofer.fluff.Scene;
import org.spoofer.fluff.Scenery;
import org.spoofer.fluff.SpecialAgent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimpleDirector implements Director {

    private final Map<Agent, Animator> performances = new HashMap<>();
    private final Map<Bot, Bot> collisions = new HashMap<>();

    private Scene scene;


    public void moveBot(Agent agent, Movement.Direction direction) {
        if (direction == Movement.Direction.Stop)
            stopBot(agent);

        if (null == scene)
            return;

        Bot bot = scene.getBot(agent.getBotId());
        if (null == bot)
            return;

        Movement movement = moveBotInside(bot, direction, scene.getSceneSize());
        if (movement.isStill())
            return;

        moveBot(agent, movement);
    }


    public void stopBot(Agent agent) {
        Animator animator = performances.remove(agent);
        if (null != animator)
            animator.cancel();
    }


    public void stopAll() {
        for (Animator animator : performances.values()) {
            animator.cancel();
        }
        performances.clear();
        collisions.clear();
    }

    public void setScene(Scene scene) {
        if (null != this.scene)
            stopAll();
        this.scene = scene;
    }


    @Override
    public boolean isPerforming(Agent agent) {
        Bot bot = scene.getBot(agent.getBotId());
        return performances.containsKey(agent);
    }


    public boolean isPerforming(@IdRes int botId) {
        for (Agent agent : performances.keySet())
            if (agent.getBotId() == botId) {
                return true;
            }
        return false;
    }

    @Override
    public boolean isInCollision(Agent agent) {
        Bot bot = scene.getBot(agent.getBotId());
        return collisions.containsKey(bot);
    }



    @Override
    public void moveBot(Agent agent, Movement movement) {

        if (agent instanceof SpecialAgent)
            haltBot(agent.getBotId());
        else {
            stopBot(agent);

            if (isPerforming(agent.getBotId())) // Check actor not performing after stop, i.e. for another agent
                return;
        }

        Bot bot = scene.getBot(agent.getBotId());
        if (null == bot)
            return;

        ValueAnimator animator = buildAnimator(bot.getView(), movement);
        animator.addListener(getEndListener(agent));
        animator.addUpdateListener(getCollisionListener(bot));

        performances.put(agent, animator);

        animator.start();
    }

    /**
     * Stops Any performance with the given actor, regardless of the Agent.
     * @param botId
     */
    private void haltBot(@IdRes int botId) {
        Set<Agent> toRemove = new HashSet<>();
        for (Map.Entry<Agent, Animator> entry : performances.entrySet()) {
            if (entry.getKey().getBotId() == botId) {
                entry.getValue().cancel();
                toRemove.add(entry.getKey());
            }
        }
        for (Agent agent : toRemove)
            performances.remove(agent);

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


    private void collideBot(Bot actor, Bot target) {
        Bot collided = collisions.get(actor);
        if (target != collided) {
            decollideBot(actor);
            collisions.put(actor, target);
            if (target instanceof Scenery)
                ((Scenery) target).notifyCollision(actor, this);
        }
    }
    private void decollideBot(Bot actor) {
        Bot target = collisions.remove(actor);
//        if (null != target && target instanceof Scenery)
  //          ((Scenery) target).notifyDecollision(ac`)
    }

    /**
     * Build a movement Animator for the given View.
     * @param view
     * @param movement
     * @return
     */
    private ValueAnimator buildAnimator(ImageView view, Movement movement) {

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
        animator.setDuration(movement.getDuration());
        animator.setInterpolator(new LinearInterpolator());

        return animator;
    }

    /**
     * Gets the Collison listener to attach to each Animator
     * @return
     */
    private ValueAnimator.AnimatorUpdateListener getCollisionListener(final Bot bot) {
        return new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Bot collision = checkCollision(bot);
                if (null != collision) {
                    collideBot(bot, collision);

                } else if (collisions.containsKey(bot)) {
                    decollideBot(bot);
                }
            }
        };
    }

    private Animator.AnimatorListener getEndListener(final Agent agent) {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                performances.remove(agent);
            }
        };
    }

    private Bot checkCollision(Bot actor) {
        Bot found = null;

        Rect actorLocation = actor.getLocation();

        for (Bot bot : scene.getBots()) {
            if (bot == actor) // Don't bump into yourself!!
                continue;

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
