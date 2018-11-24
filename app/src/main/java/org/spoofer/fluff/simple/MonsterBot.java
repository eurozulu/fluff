package org.spoofer.fluff.simple;

import android.widget.ImageView;

import org.spoofer.fluff.Bot;
import org.spoofer.fluff.Director;
import org.spoofer.fluff.Movement;

public class MonsterBot extends SimpleScenery {
    public MonsterBot(ImageView view) {
        super(view);
    }

    @Override
    public void notifyCollision(Bot actor, Director director) {
        super.notifyCollision(actor, director);
    }

}
