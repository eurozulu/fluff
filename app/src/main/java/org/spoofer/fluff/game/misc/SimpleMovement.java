package org.spoofer.fluff.game.misc;

import android.graphics.Rect;

public class SimpleMovement implements Movement {

    private Rect fromLocation;
    private Rect toLocation;


    public SimpleMovement(Rect fromLocation, Rect toLocation) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    public SimpleMovement(Rect fromLocation, Direction direction) {
        this(fromLocation, offSet(fromLocation, direction));
    }

    @Override
    public Direction getDirection() {
        if (isStill())
            return Direction.Stop;

        double bearing = getBearing();
        return bearing > 44 && bearing < 135 ? Direction.Right :
                bearing > 134 && bearing < 225 ? Direction.Down :
                        bearing > 224 && bearing < 315 ? Direction.Left :
                                Direction.Up;
    }


    @Override
    public boolean isStill() {
        return fromLocation.equals(toLocation);
    }

    @Override
    public boolean isHorizontal() {
        return toLocation.centerX() != fromLocation.centerX();
    }

    @Override
    public boolean isVertical() {
        return toLocation.centerY() != fromLocation.centerY();
    }

    @Override
    public int getVelocity() {
        return VELOCITY;
    }

    @Override
    public Rect getFromLocation() {
        return fromLocation;
    }

    @Override
    public Rect getToLocation() {
        return toLocation;
    }

    @Override
    public long getDistance() {
        if (isStill())
            return 0;

        int x = Math.abs(fromLocation.centerX() - toLocation.centerX());
        int y = Math.abs(fromLocation.centerY() - toLocation.centerY());
        return Math.round(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
    }

    public double getBearing() {
        /*
        https://stackoverflow.com/questions/9566069/how-to-calculate-angle-between-two-geographical-gps-coordinates
        float dy = lat2 - lat1;
        float dx = cosf(M_PI/180*lat1)*(long2 - long1);
        float angle = atan2f(dy, dx);

        ----

        double dy = lat2 - lat1;
        double dx = Math.cos(Math.PI / 180 * lat1) * (long2 - long1);
        double angle = Math.atan2(dy, dx);
        return angle;


         */
        float lat1 = fromLocation.exactCenterX();
        float lat2 = toLocation.exactCenterX();

        float long1 = fromLocation.exactCenterY();
        float long2 = toLocation.exactCenterY();

        double dy = lat2 - lat1;
        double dx = Math.cos(Math.PI / 180 * lat1) * (long2 - long1);
        double angle = Math.atan2(dy, dx);
        return angle;

    }

    /**
     * Gets time, in milliseconds, to move the distance, at the VELOCITY
     *
     * @return
     */
    public long getDuration() {
        return Math.round(((double) getDistance() / VELOCITY) * 1000);
    }

    private static Rect offSet(Rect from, Direction direction) {
        Rect r = new Rect(from);
        switch (direction) {
            case Left:
                r.offsetTo(0, r.top);
                break;

            case Right:
                r.offsetTo(r.right + r.width(), r.top);
                break;

            case Up:
                r.offsetTo(r.left, r.top - r.height());
                break;

            case Down:
                r.offsetTo(r.left, r.bottom + r.height());
                break;

        }
        return r;
    }

}
