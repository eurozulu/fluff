package org.spoofer.fluff.game.misc;

import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import org.spoofer.fluff.BuildConfig;
import org.spoofer.fluff.R;

import java.util.Arrays;
import java.util.Iterator;

public class LevelSequence implements Iterable<Integer> {

    private static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;


    @LayoutRes
    private final int[] levelLayouts;

    public LevelSequence(Resources resources) {
        this.levelLayouts = loadLevelLayouts(resources);
    }

    @LayoutRes
    public int getLevelLayoutId(int level) {
        return level >= 0 && level < levelLayouts.length ? levelLayouts[level] : 0;
    }

    @NonNull
    @Override
    public Iterator<Integer> iterator() {
        return new Iterator() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < levelLayouts.length;
            }

            @Override
            public Object next() {
                return hasNext() ? levelLayouts[index++] : null;
            }
        };
    }


    @LayoutRes
    public static int[] loadLevelLayouts(Resources resources) {

        String[] layouts = resources.getStringArray(R.array.scene_layouts);
        if (null == layouts || layouts.length < 1)
            return null;

        int[] layoutIds = new int[layouts.length];
        int index = 0;
        for (String layout : layouts) {
            int id = resources.getIdentifier(layout, "layout", PACKAGE_NAME);
            if (id > 0)
                layoutIds[index++] = id;
        }

        if (layouts.length != index) // trim array down
            layoutIds = Arrays.copyOf(layoutIds, index);

        return layoutIds;
    }


}
