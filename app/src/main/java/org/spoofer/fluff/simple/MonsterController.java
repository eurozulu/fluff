package org.spoofer.fluff.simple;

import org.spoofer.fluff.MovementEngine;
import org.spoofer.fluff.utils.ScheduledUpdater;

public class MonsterController extends SimpleController {

    ScheduledUpdater updater = new ScheduledUpdater(new ScheduledUpdater.Updated() {
        @Override
        public void update() {

        }
    }, 250);

    public MonsterController(int id, MovementEngine movementEngine) {
        super(id, movementEngine);
    }


}
