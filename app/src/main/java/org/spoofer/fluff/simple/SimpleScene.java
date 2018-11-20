package org.spoofer.fluff.simple;

import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.spoofer.fluff.Bot;
import org.spoofer.fluff.Scene;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleScene implements Scene {

    private final Map<Integer, Bot> bots = new HashMap<>();

    private ViewGroup sceneView;


    public Collection<Bot> getBots() {
        return bots.values();
    }

    public Bot getBot(@IdRes int id) {
        return bots.get(id);
    }

    public ViewGroup getSceneView() {
        return sceneView;
    }

    public Rect getSceneSize() {
        View view = getSceneView();
        int[] outLocation = new int[2];
        view.getLocationInWindow(outLocation);
        return new Rect(outLocation[0], outLocation[1],
                outLocation[0] + view.getWidth(), outLocation[1] + view.getHeight());
    }

    public void loadScene(ViewGroup sceneView) {
        this.sceneView = sceneView;
        bots.clear();
        if (null != sceneView)
            loadSceneInternal(sceneView);
    }

    private void loadSceneInternal(ViewGroup parent) {

        for (int index = 0; index < parent.getChildCount(); index++) {
            View view = parent.getChildAt(index);

            if (view instanceof ViewGroup)
                loadSceneInternal((ViewGroup) view);

            else if (isViewABot(view)) {
                bots.put(view.getId(), new SimpleBot((ImageView) view));
            }
        }
    }

    private boolean isViewABot(View view) {
        return view.getId() != View.NO_ID && view.getId() != 0 && view instanceof ImageView;
    }
}
