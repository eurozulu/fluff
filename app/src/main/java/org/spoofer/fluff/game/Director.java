package org.spoofer.fluff.game;

import android.support.annotation.IdRes;

/**
 * Director coordinates the scene, managing the Actions of each Actor and their interaction with the Scenery in the Scene
 */
public interface Director {

    void startScene(Scene scene);

    void endScene();

    void startAction(@IdRes int botId, String action);

    void stopAction(@IdRes int botId);

    void stopAll();
}
