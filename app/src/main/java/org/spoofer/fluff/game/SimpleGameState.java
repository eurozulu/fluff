package org.spoofer.fluff.game;


import android.util.Log;

import org.spoofer.fluff.game.misc.LevelSequence;

import java.util.Iterator;

public class SimpleGameState implements GameState {

    private static final String LOGTAG = SimpleGameState.class.getName();


    private int levelCount;
    private int score;
    private int lives;
    private long startTime;

    private Iterator<Integer> layouts;
    private int levelLayout;



    @Override
    public void setLevels(LevelSequence levels) {
        Log.d(LOGTAG, String.format("Resetting game state with level sequence: %s", levels));

        lives = DEFAULT_LIVES;
        score = 0;
        levelCount = 0;

        layouts = levels.iterator();
        levelLayout = 0;

        nextLevel();

        startTime = System.currentTimeMillis();
        Log.d(LOGTAG, String.format("Game state reset and game clock started @: %tT", startTime));

    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void addScore(int score) {
        if (score > 0)
            this.score += score;
    }

    @Override
    public int getLevel() {
        return levelCount;
    }

    @Override
    public void nextLevel() {
        levelLayout = layouts.hasNext() ? layouts.next() : 0;
        levelCount = levelLayout != 0 ? levelCount + 1  : 0;
    }

    @Override
    public int getLevelLayoutId() {
        return levelLayout;
    }

    @Override
    public boolean isGameOver() {
        return !isAlive() && levelCount <= 0;
    }

    @Override
    public int getLives() {
        return lives;
    }

    @Override
    public void removeLife() {
        if (isAlive())
            lives--;
    }

    @Override
    public boolean isAlive() {
        return lives > 0;
    }

    @Override
    public int getLifeEnergy() {
        if (startTime == 0)
            return 0;

        long timeTaken = System.currentTimeMillis() - startTime;
        return (int)(DEFAULT_LIFE_ENERGY - timeTaken) / DEFAULT_LIFE_ENERGY;
    }

}
