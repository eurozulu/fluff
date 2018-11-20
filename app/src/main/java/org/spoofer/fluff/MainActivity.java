package org.spoofer.fluff;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import org.spoofer.fluff.simple.ButtonController;
import org.spoofer.fluff.simple.SimpleMovementEngine;
import org.spoofer.fluff.simple.SimpleScene;

public class MainActivity extends AppCompatActivity {

    public static final long DEBUG_UPDATE_TIME = 250;

    private final MovementEngine movementEngine = new SimpleMovementEngine();

    private Controller controller;

    private Scene scene;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scene = new SimpleScene();
        controller = createPlayerController();
    }


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
        movementEngine.stopAll();

        uiHandler.removeCallbacks(debugUpdater);
    }






    private final Runnable debugUpdater = new Runnable() {
        @Override
        public void run() {
            Bot bot = scene.getBot(R.id.bot_player);
            try {
                if (null == bot)
                    return;

                Rect botLoc = bot.getLocation();
                Rect sceneLoc = scene.getSceneSize();

                StringBuilder s = new StringBuilder();
                s.append(String.format("Bot loction: %s ", botLoc)).append('\n')
                        .append(String.format("Scene loction: %s", sceneLoc));

                TextView debugTxt = findViewById(R.id.txt_debug);
                debugTxt.setText(s.toString());

                RadioButton led = findViewById(R.id.led_onAir);
                led.setBackgroundColor(movementEngine.isPerforming() ? getColor(R.color.colourRed) : getColor(R.color.colourGreen));
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                uiHandler.postDelayed(debugUpdater, DEBUG_UPDATE_TIME);
            }
        }
    };


    private Controller createPlayerController() {
        ButtonController controller = new ButtonController(R.id.bot_player, movementEngine);
        controller.addButton(findViewById(R.id.btn_left), Movement.Direction.Left);
        controller.addButton(findViewById(R.id.btn_right), Movement.Direction.Right);
        controller.addButton(findViewById(R.id.btn_up), Movement.Direction.Up);
        controller.addButton(findViewById(R.id.btn_down), Movement.Direction.Down);
        return controller;
    }


}