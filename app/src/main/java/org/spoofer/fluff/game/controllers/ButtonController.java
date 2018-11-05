package org.spoofer.fluff.game.controllers;

import android.view.MotionEvent;
import android.view.View;

import org.spoofer.fluff.game.Director;
import org.spoofer.fluff.game.misc.Movement;

public class ButtonController extends SingleBotController {

    public ButtonController(Director director, int actorId) {
        super(director, actorId);
    }

    public void addButton(View view, final Movement.Direction direction) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean used = false;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setDirection(direction);
                        used = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        used = true;
                        setDirection(Movement.Direction.Stop);
                        break;
                    default:
                        used = false;
                }
                return used;
            }
        });
    }
}
