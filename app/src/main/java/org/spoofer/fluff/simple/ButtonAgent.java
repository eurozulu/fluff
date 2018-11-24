package org.spoofer.fluff.simple;

import android.view.MotionEvent;
import android.view.View;

import org.spoofer.fluff.Movement;
import org.spoofer.fluff.Director;
import org.spoofer.fluff.R;

public class ButtonAgent extends SimpleAgent {

    public ButtonAgent(int id, Director director) {
        super(id, director);
    }

    public void addButton(View button, final Movement.Direction direction) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ButtonAgent.this.move(direction);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        ButtonAgent.this.move(Movement.Direction.Stop);
                    }
                    default:
                        return false;
                }
                return true;
            }
        });
    }
}
