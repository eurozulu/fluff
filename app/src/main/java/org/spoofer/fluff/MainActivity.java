package org.spoofer.fluff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.spoofer.fluff.game.controllers.ButtonController;
import org.spoofer.fluff.game.controllers.Controller;
import org.spoofer.fluff.game.Game;
import org.spoofer.fluff.game.GameState;
import org.spoofer.fluff.game.misc.Movement;
import org.spoofer.fluff.game.controllers.MultiBotControler;
import org.spoofer.fluff.utils.ScheduledUpdater;

public class MainActivity extends AppCompatActivity implements Game.GameListener {

    private static final long DISPLAY_UPDATE_INTERVAL = 500;

    // Scheduled update of the game status controls.
    private final ScheduledUpdater scheduledUpdater = new ScheduledUpdater(new ScheduledUpdater.Updated() {
        @Override
        public void update() {
            updateGameStateDisplay();
        }
    }, DISPLAY_UPDATE_INTERVAL);

    private final Game game = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ProgressBar) findViewById(R.id.prg_energy)).setMax(100); // Works as percentage

        game.setRootView((ViewGroup) findViewById(R.id.gameboard));
        game.addGameListener(this);

        game.addSceneListener(createControlPanel());

        game.addSceneListener(new MultiBotControler("monster_red"));
        game.addSceneListener(new MultiBotControler("monster_yellow"));
        game.addSceneListener(new MultiBotControler("monster_white"));

        Button startButton = findViewById(R.id.btn_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!game.isPlaying())
                    game.startNewGame();
                else
                    v.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        scheduledUpdater.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scheduledUpdater.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scheduledUpdater.stop();
        game.endGame();
    }

    @Override
    public void gameStarting(GameState gameState) {
        findViewById(R.id.btn_start).setVisibility(View.GONE);
        findViewById(R.id.txt_gameover).setVisibility(View.GONE);

        findViewById(R.id.panel_controls).setEnabled(true);
        findViewById(R.id.panel_controls).bringToFront();

        scheduledUpdater.start();
    }

    @Override
    public void gameEnding(GameState gameState) {

        scheduledUpdater.stop();

        findViewById(R.id.btn_start).setVisibility(View.VISIBLE);
        findViewById(R.id.txt_gameover).setVisibility(View.VISIBLE);
        findViewById(R.id.panel_controls).setEnabled(false);
    }

    private void updateGameStateDisplay() {

        GameState gameState = game.getGameState();
        if (null == gameState)
            return;

        String lives = String.format(getString(R.string.lives_pattern), gameState.getLives());
        ((TextView) findViewById(R.id.txt_lifecount)).setText(lives);

        String score = String.format(getString(R.string.score_pattern), gameState.getScore());
        ((TextView) findViewById(R.id.txt_score)).setText(score);

        ((ProgressBar) findViewById(R.id.prg_energy)).setProgress(gameState.getLifeEnergy());

    }

    private Controller createControlPanel() {
        ButtonController controller = new ButtonController(game.getDirector(), R.id.bot_player);
        controller.addButton(findViewById(R.id.btn_left), Movement.Direction.Left);
        controller.addButton(findViewById(R.id.btn_right), Movement.Direction.Right);
        controller.addButton(findViewById(R.id.btn_up), Movement.Direction.Up);
        controller.addButton(findViewById(R.id.btn_down), Movement.Direction.Down);

        return controller;
    }
}
