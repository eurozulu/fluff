package org.spoofer.fluff;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import org.spoofer.fluff.simple.SimpleMovementEngine;
import org.spoofer.fluff.simple.SimpleScene;

public class MainActivity extends AppCompatActivity {

    public static final long DEBUG_UPDATE_TIME = 250;

    private final MovementEngine movementEngine = new SimpleMovementEngine();

    private Scene scene;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scene = new SimpleScene();

        addButton(findViewById(R.id.btn_left) , Movement.Direction.Left);
        addButton(findViewById(R.id.btn_right) , Movement.Direction.Right);
        addButton(findViewById(R.id.btn_up) , Movement.Direction.Up);
        addButton(findViewById(R.id.btn_down) , Movement.Direction.Down);
    }


    private final Runnable debugUpdater = new Runnable() {
        @Override
        public void run() {
            Bot bot = scene.getBot(R.id.bot_player);
            Rect botLoc = bot.getLocation();
            Rect sceneLoc = scene.getSceneSize();

            StringBuilder s = new StringBuilder();
            s.append(String.format("Bot loction: %s ", botLoc)).append('\n')
                    .append(String.format("Scene loction: %s", sceneLoc));

            TextView debugTxt = findViewById(R.id.txt_debug);
            debugTxt.setText(s.toString());

            RadioButton led = findViewById(R.id.led_onAir);
            led.setBackgroundColor(movementEngine.isPerforming() ? getColor(R.color.colourRed) : getColor(R.color.colourGreen));
            uiHandler.postDelayed(debugUpdater, DEBUG_UPDATE_TIME);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        ((SimpleScene) scene).loadScene((ViewGroup) findViewById(R.id.gameboard));
        movementEngine.setScene(scene);
        uiHandler.postDelayed(debugUpdater, DEBUG_UPDATE_TIME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        uiHandler.removeCallbacks(debugUpdater);
    }


    private void addButton(View button, final Movement.Direction direction) {
            button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        movementEngine.moveBot(R.id.bot_player, direction);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        movementEngine.stopBot(R.id.bot_player);
                    }
                    default:
                        return false;
                }
                return true;
            }
        });

    }
}