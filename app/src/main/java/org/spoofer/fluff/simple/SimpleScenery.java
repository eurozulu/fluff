package org.spoofer.fluff.simple;

import android.graphics.Rect;
import android.widget.ImageView;

import org.spoofer.fluff.Bot;
import org.spoofer.fluff.Movement;
import org.spoofer.fluff.Scenery;

public class SimpleScenery extends SimpleBot implements Scenery {

    public SimpleScenery(ImageView view) {
        super(view);
    }

    @Override
    public Movement getCollisionMovement(Bot actor, Movement movement) {
        Movement moveTo = moveBotOutside(actor, movement.getDirection(), getLocation());
        return moveTo;
    }


    // Move the bot to the opposite edge of the direction of travel.
    // i.e. if direction =Left, move to right side, if Up, move to Bottom.
    private Movement moveBotOutside(Bot actor, Movement.Direction direction, Rect boundary) {

        Rect botLoc = actor.getLocation();
        Movement movement = new SimpleMovement(botLoc);

        if (!Rect.intersects(botLoc, boundary))
            return movement;

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

            default:
                return movement;
        }

        movement.setEndLocation(newLoc);

        return movement;
    }

}
