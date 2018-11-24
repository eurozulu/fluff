package org.spoofer.fluff.simple;

import org.spoofer.fluff.Director;
import org.spoofer.fluff.utils.ScheduledUpdater;

public class MonsterAgent extends SimpleAgent {

    ScheduledUpdater updater = new ScheduledUpdater(new ScheduledUpdater.Updated() {
        @Override
        public void update() {

        }
    }, 250);

    public MonsterAgent(int id, Director director) {
        super(id, director);
    }


}
