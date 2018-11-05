package org.spoofer.fluff.utils;

import android.os.Handler;
import android.os.Looper;

public class ScheduledUpdater {

    public static final long DEFAULT_DELAY = 1000;

    private ScheduledUpdate scheduledUpdater = null;

    public interface Updated {
        void update();
    }


    private final Updated updated;
    private final long delay;

    public ScheduledUpdater(Updated updated) {
        this(updated, DEFAULT_DELAY);
    }

    public ScheduledUpdater(Updated updated, long delay) {
        this.updated = updated;
        this.delay = delay;
    }

    public void start() {
        if (null == scheduledUpdater) {
            scheduledUpdater = new ScheduledUpdate();
            scheduledUpdater.run();
        }
    }

    public void stop() {
        if (null != scheduledUpdater) {
            uiHandler.removeCallbacks(scheduledUpdater);
            scheduledUpdater = null;
        }
    }

    public boolean isStarted() { return null != scheduledUpdater; }


    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private class ScheduledUpdate implements Runnable  {
        @Override
        public void run() {
            try {
                updated.update();

            } finally { // reruns always, even after exception.
                uiHandler.postDelayed(this, delay);
            }
        }
    }

}
