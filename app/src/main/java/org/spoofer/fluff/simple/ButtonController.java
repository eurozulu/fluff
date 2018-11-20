package org.spoofer.fluff.simple;

import android.view.MotionEvent;
import android.view.View;

import org.spoofer.fluff.Movement;
import org.spoofer.fluff.MovementEngine;
import org.spoofer.fluff.R;

public class ButtonController extends SimpleController {

    public ButtonController(int id, MovementEngine movementEngine) {
        super(id, movementEngine);
    }

    public void addButton(View button, final Movement.Direction direction) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        getMovementEngine().moveBot(R.id.bot_player, direction);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        getMovementEngine().stopBot(R.id.bot_player);
                    }
                    default:
                        return false;
                }
                return true;
            }
        });
    }
}
