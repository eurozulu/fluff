package org.spoofer.fluff.game;

import android.view.ViewGroup;

/**
 * A Game os the root object of the Game.  It manages the lifecycle of the game
 * and maintains a reference to the Root View, the view used to attach each game scene to the main view tree.
 *
 * The Game offers a listening interface for the application to monitor the lifecycle of the game.
 */
public interface Game {

    GameState getGameState();

    void addGameListener(GameListener listener);
    void removeGameListener(GameListener listener);


    void setRootView(ViewGroup rootView);
    ViewGroup getRootView();

    void startNewGame();
    boolean isPlaying();

    void endGame();

    /**
     * Gets the director of the game.  The Director controls each Scene, managing the Bot actions and interactions.
     * @return
     */
    Director getDirector();


    /**
     * Simple interface to monitor when a game starts and stops.
     */
    interface GameListener {
        void gameStarting(GameState gameState);
        void gameEnding(GameState gameState);
    }
}
