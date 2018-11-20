package org.spoofer.fluff.simple;

import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import org.spoofer.fluff.Bot;

public class SimpleBot implements Bot {

    private final ImageView view;

    public SimpleBot(ImageView view) {
        this.view = view;
    }

    @Override
    public ImageView getView() {
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
