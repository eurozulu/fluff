package org.spoofer.fluff.game;

import android.view.ViewGroup;

public interface Game {

    GameState getGameState();

    void addGameListener(GameListener listener);
    void removeGameListener(GameListener listener);

    void addSceneListener(SceneListener listener);
    void removeSceneListener(SceneListener listener);


    void setRootView(ViewGroup rootView);
    ViewGroup getRootView();

    void startNewGame();
    boolean isPlaying();

    void endGame();


    Director getDirector();


    interface GameListener {
        void gameStarting(GameState gameState);
        void gameEnding(GameState gameState);
    }

    interface SceneListener {
        void sceneStarting(Scene scene);
        void sceneEnding(Scene scene);
    }


}
