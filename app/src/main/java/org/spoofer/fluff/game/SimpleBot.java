package org.spoofer.fluff.game;

import android.graphics.Rect;
import android.view.View;

public class SimpleBot implements Bot {

    private final View view;

    public SimpleBot(View view) {
        this.view = view;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public Rect getLocation() {
        View view = getView();
        int[] outLocation = new int[2];
        view.getLocationInWindow(outLocation);
        return new Rect(outLocation[0], outLocation[1],
                outLocation[0] + view.getWidth(), outLocation[1] + view.getHeight());
    }

}
