package org.spoofer.fluff.simple;

import android.view.MotionEvent;
import android.view.View;

import org.spoofer.fluff.Movement;
import org.spoofer.fluff.Director;
import org.spoofer.fluff.R;

public class ButtonController extends SimpleController {

    public ButtonController(int id, Director director) {
        super(id, director);
    }

    public void addButton(View button, final Movement.Direction direction) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        getDirector().moveBot(R.id.bot_player, direction);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        getDirector().stopBot(R.id.bot_player);
                    }
                    default:
                        return false;
                }
                return true;
            }
        });
    }
}
