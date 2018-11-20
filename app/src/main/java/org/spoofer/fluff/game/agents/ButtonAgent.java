package org.spoofer.fluff.game.agents;

import android.view.MotionEvent;
import android.view.View;

import org.spoofer.fluff.game.Director;
import org.spoofer.fluff.game.misc.Movement;

/**
 * Button Agent adds a TouchListener to a given View to issue a new command to the director each time the
 * View is touched and issues a Stop when the touch is released.
 * Usually the View is a Button or similar
 */
public class ButtonAgent extends SimpleAgent {

    public ButtonAgent(Director director, int actorId) {
        super(director, actorId);
    }

    public void addButton(View view, final String command) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean used = true;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        performCommand(command);
                        break;
                    case MotionEvent.ACTION_UP:
                        performCommand(Movement.Direction.Stop.name());
                        break;
                    default:
                        used = false;
                }
                return used;
            }
        });
    }
}
