package org.spoofer.fluff;

import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.view.ViewGroup;

import java.util.Collection;

public interface Scene {

    Collection<Bot> getBots();

    Bot getBot(@IdRes int id);

    ViewGroup getSceneView();

    Rect getSceneSize();

}
