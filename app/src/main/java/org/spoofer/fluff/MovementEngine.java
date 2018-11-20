package org.spoofer.fluff;

import android.support.annotation.IdRes;

public interface MovementEngine {


    void moveBot(@IdRes int botId, Movement.Direction direction);

    void stopBot(@IdRes int botId);

    void stopAll();

    void setScene(Scene scene);

    boolean isPerforming();
    boolean isInCollision(@IdRes int id);

}
