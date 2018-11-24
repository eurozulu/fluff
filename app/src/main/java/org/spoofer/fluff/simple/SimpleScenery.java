package org.spoofer.fluff.simple;

import android.graphics.Rect;
import android.widget.ImageView;

import org.spoofer.fluff.Agent;
import org.spoofer.fluff.Bot;
import org.spoofer.fluff.Director;
import org.spoofer.fluff.Movement;
import org.spoofer.fluff.Scenery;
import org.spoofer.fluff.SpecialAgent;

public class SimpleScenery extends SimpleBot implements Scenery {

    private static final int SCENERY_MARGIN = 1;

    public SimpleScenery(ImageView view) {
        super(view);
    }


    private class CollisionAgent extends SimpleAgent implements SpecialAgent {
        public CollisionAgent(int id, Director director) {
            super(id, director);
        }
    }

    @Override
    public void notifyCollision(Bot actor, Director director) {
        Movement movement = moveBotOutside(actor, getLocation());
        Agent collisionAgent = new CollisionAgent(actor.getView().getId(), director);
        director.moveBot(collisionAgent, movement);
    }


    // Move the bot to the opposite edge of the direction of travel.
    // i.e. if direction =Left, move to right side, if Up, move to Bottom.
    private Movement moveBotOutside(Bot actor, Rect boundary) {

        Rect botLoc = actor.getLocation();
        Movement movement = new SimpleMovement(botLoc);

        if (!Rect.intersects(botLoc, boundary))
            return movement;

        boundary.inset(-SCENERY_MARGIN, -SCENERY_MARGIN); // widen boundry to ensure player is not in collision after.

        Movement.Direction closestEdge = findClosestEdge(botLoc, boundary);
        Rect newLoc = new Rect(botLoc);

        switch (closestEdge) {
            case Left:
                newLoc.offset(boundary.left - botLoc.right, 0);
                break;
            case Right:
                newLoc.offset(boundary.right - botLoc.left, 0);
                break;
            case Up:
                newLoc.offset(0, boundary.top - botLoc.bottom);
                break;
            case Down:
                newLoc.offset(0, boundary.bottom - botLoc.top);
                break;

            default:
                return movement;
        }

        movement.setEndLocation(newLoc);

        return movement;
    }

    private Movement.Direction findClosestEdge(Rect location, Rect boundary) {

        Rect intersect = new Rect(boundary);
        if (!intersect.intersect(location))
            return Movement.Direction.Stop;

        Rect offsets = new Rect(
                Math.abs(boundary.left - intersect.left),
                Math.abs(boundary.top - intersect.top),
                Math.abs(boundary.right - intersect.right),
                Math.abs(boundary.bottom - intersect.bottom)
        );


        Movement.Direction direction = Movement.Direction.Stop;
        int smallest = Integer.MAX_VALUE;
        if (offsets.top < smallest) {
            smallest = offsets.top;
            direction = Movement.Direction.Up;
        }

        if (offsets.bottom < smallest) {
            smallest = offsets.bottom;
            direction = Movement.Direction.Down;
        }

        if (offsets.left < smallest) {
            smallest = offsets.left;
            direction = Movement.Direction.Left;
        }
        if (offsets.right < smallest) {
            smallest = offsets.right;
            direction = Movement.Direction.Right;
        }

        return direction;
    }

}
