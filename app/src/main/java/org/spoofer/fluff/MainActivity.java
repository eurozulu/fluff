package org.spoofer.fluff;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import org.spoofer.fluff.simple.ButtonController;
import org.spoofer.fluff.simple.MonsterController;
import org.spoofer.fluff.simple.SimpleDirector;
import org.spoofer.fluff.simple.SimpleScene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final long DEBUG_UPDATE_TIME = 250;

    private final Director director = new SimpleDirector();

    private List<Controller> controllers = new ArrayList<>();

    private Scene scene;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scene = new SimpleScene(findViewById(R.id.gameboard));

        controllers.add(createPlayerController());
        controllers.addAll(getMonsterControllers(scene.getBots()));
    }


    @Override
    protected void onStart() {
        super.onStart();

        ((SimpleScene) scene).initalise();
        director.setScene(scene);
        uiHandler.postDelayed(debugUpdater, DEBUG_UPDATE_TIME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        director.stopAll();

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

                RadioButton ledOnAir = findViewById(R.id.led_onAir);
                ledOnAir.setBackgroundColor(director.isPerforming() ? getColor(R.color.colourRed) : getColor(R.color.colourGreen));

                RadioButton ledInCollision = findViewById(R.id.led_inCollision);
                ledInCollision.setBackgroundColor(director.isInCollision(R.id.bot_player) ?
                        getColor(R.color.colourRed) : getColor(R.color.colourGreen));

            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                uiHandler.postDelayed(debugUpdater, DEBUG_UPDATE_TIME);
            }
        }
    };


    private Controller createPlayerController() {
        ButtonController controller = new ButtonController(R.id.bot_player, director);
        controller.addButton(findViewById(R.id.btn_left), Movement.Direction.Left);
        controller.addButton(findViewById(R.id.btn_right), Movement.Direction.Right);
        controller.addButton(findViewById(R.id.btn_up), Movement.Direction.Up);
        controller.addButton(findViewById(R.id.btn_down), Movement.Direction.Down);
        return controller;
    }

    private List<Controller> getMonsterControllers(Collection<Bot> bots) {
        Resources resources = getResources();
        List<Controller> controllers = new ArrayList<>();

        for (Bot bot : bots) {
            @IdRes int botId = bot.getView().getId();
            String name = resources.getResourceName(botId);
            if (null != name && name.contains("monster_red"))
                controllers.add(new MonsterController(botId, director));
        }

        return controllers;
    }

}