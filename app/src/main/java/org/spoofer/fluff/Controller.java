package org.spoofer.fluff;

import android.support.annotation.IdRes;

public interface Controller {

    @IdRes int getBotId();

    void move(Movement.Direction direction);
}
