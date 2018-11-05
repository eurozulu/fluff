package org.spoofer.fluff.game;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import org.spoofer.fluff.game.misc.LevelSequence;

public interface GameState {

    int DEFAULT_LIVES = 5;
    int DEFAULT_LIFE_ENERGY = 3 * 60 * 1000; // three minutes.


    int getScore();
    void addScore(int score);

    int getLevel();
    void nextLevel();
    boolean isGameOver();
    @LayoutRes int getLevelLayoutId();

    int getLives();
    void removeLife();
    boolean isAlive();

    /**
     * A percentage of life energy left.
     * Player starts with 100% which will decline as time passes.
     * In addition, game events may cause the lifeEnergy to decrease.
     * LifeEngery can never exceed 100.  Once it reaches zero, the current life dies, decreasing Lives by one.
     *
     * @see #getLives()
     * @return percentage of life energy the current life has left.
     */
    int getLifeEnergy();

    /**
     * Sets the levels for a new game.
     * This will reset all state, score, lives time and current level
     * The current level will be the first in the given sequence
     *
     * @param levels the levels for the game
     * @see #nextLevel()
     */
    void setLevels(LevelSequence levels);

}
