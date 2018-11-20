package org.spoofer.fluff.game;

import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.spoofer.fluff.game.agents.AgentGroup;
import org.spoofer.fluff.game.misc.LevelSequence;
import org.spoofer.fluff.utils.ScheduledUpdater;

import java.util.HashSet;
import java.util.Set;

public class SimpleGame implements Game {

    private static final String LOGTAG = SimpleGame.class.getName();


    private static final long DEFAULT_GAME_LENGTH = 3 * 60 * 1000;

    private final Director director = new SimpleDirector();

    private final GameState gameState = new SimpleGameState();
    private final Set<GameListener> gameListeners = new HashSet<>();


    private final ScheduledUpdater gameScheduler = new ScheduledUpdater(new ScheduledUpdater.Updated() {
        @Override
        public void update() {
            endGame();
        }
    }, DEFAULT_GAME_LENGTH);

    private final ScheduledUpdater sceneScheduler = new ScheduledUpdater(new ScheduledUpdater.Updated() {
        @Override
        public void update() {
            gameState.nextLevel();
            if (gameState.isGameOver())
                endGame();
            startScene(gameState.getLevelLayoutId());

        }
    }, GameState.DEFAULT_LIFE_ENERGY);


    private ViewGroup rootView;


    @Override
    public Director getDirector() {
        return director;
    }

    @Override
    public void addGameListener(GameListener listener) {
        gameListeners.add(listener);
    }

    @Override
    public void removeGameListener(GameListener listener) {
        gameListeners.remove(listener);
    }


    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void setRootView(ViewGroup rootView) {
        this.rootView = rootView;
    }

    @Override
    public ViewGroup getRootView() {
        return this.rootView;
    }

    @Override
    public void startNewGame() {
        if (null == rootView)
            throw new IllegalStateException("View root not set on game engine!");

        gameState.setLevels(new LevelSequence(rootView.getResources()));

        for (GameListener listener : gameListeners)
            listener.gameStarting(gameState);

        gameScheduler.start(); // Schedule will end the game.

        startScene(gameState.getLevelLayoutId());
    }

    @Override
    public boolean isPlaying() {
        return gameScheduler.isStarted();
    }

    @Override
    public void endGame() {
        director.endScene();

        for (GameListener listener : gameListeners)
            listener.gameEnding(gameState);
    }


    private void startScene(@LayoutRes int layoutId) {

        director.endScene();  // Kill any existing scene first.

        if (layoutId == 0)
            return;

        Scene scene = loadScene(layoutId);

        if (scene != null) {
            director.startScene(scene);
        } else {
            Log.d(LOGTAG, String.format("Failed to load scene id %s", layoutId));
        }

    }


    private Scene loadScene(@LayoutRes int layoutId) {
        // Inflate the given layout ID into the new level.
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        ViewGroup levelLayout = (ViewGroup) inflater.inflate(layoutId, null);
        if (null == levelLayout)
            return null;

        // Attach new layout to the rootView as it's one and only child.
        rootView.removeAllViews();
        rootView.addView(levelLayout);

        return new SimpleScene(levelLayout);
    }

    private void loadAgents() {
        //TODO: fill in the monster IDs

        int[] monstersRed = new int[] {

        };
        int[] monstersYellow = new int[] {

        };
        int[] monstersWhite = new int[] {

        };
        new AgentGroup(director, monstersRed);
        new AgentGroup(director, monstersYellow);
        new AgentGroup(director, monstersWhite);

    }
}
