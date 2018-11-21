package org.spoofer.fluff.simple;

import android.graphics.Rect;

import org.spoofer.fluff.Movement;

public class SimpleMovement implements Movement {

    private Rect fromLocation = new Rect();
    private Direction direction = Direction.Stop;
    private int distance = 0;

    private int velocity = DEFAULT_VELOCITY;

    public SimpleMovement(Movement movement) {
        setStartLocation(fromLocation);
        this.direction = movement.getDirection();
        this.distance = movement.getDistance();
    }

    public SimpleMovement(Rect fromLocation) {
        setStartLocation(fromLocation);
    }

    public SimpleMovement(Rect fromLocation, Direction direction, int distance) {
        setStartLocation(fromLocation);
        this.direction = direction;
        this.distance = distance;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public Rect getStartLocation() {
        return fromLocation;
    }

    public void setStartLocation(Rect startLocation) {
        this.fromLocation = null != startLocation ? new Rect(startLocation) : new Rect();
    }

    public Rect getEndLocation() {
        Rect toLocation = new Rect(fromLocation);
        if (!isStill())
            offSet(toLocation, getDirection());

        return toLocation;
    }

    /**
     * Sets the Direction and distance based on the given Rect.
     * Takes the biggest single movement, so if both X and Y axes change
     * only one is moved.
     *
     * @param toLocation
     */
    public void setEndLocation(Rect toLocation) {
        int distX = toLocation.centerX() - fromLocation.centerX();
        int distY = toLocation.centerY() - fromLocation.centerY();

        if (Math.abs(distX) >= Math.abs(distY)) {
            direction = distX > 0 ? Direction.Right : distX < 0 ? Direction.Left : Direction.Stop;
            distance = Math.abs(distX);
        } else {
            direction = distY > 0 ? Direction.Down : distY < 0 ? Direction.Up : Direction.Stop;
            distance = Math.abs(distY);
        }
    }

    public boolean isStill() {
        return getDirection() == Direction.Stop || getDistance() == 0;
    }

    public boolean isHorizontal() {
        return !isStill() && (direction == Direction.Left || direction == Direction.Right);
    }

    public boolean isVertical() {
        return !isStill() && (direction == Direction.Up || direction == Direction.Down);
    }

    public long getDuration() {
        return Math.round((distance / (float)getVelocity()) * 1000);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getDistance() {
        return distance;
    }

    public int getRelativeDistance() {
        return direction == Direction.Left || direction == Direction.Up ? -distance : distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("[from: %s, distance: %d, direction: %s", fromLocation, distance, direction);
    }

    private void offSet(Rect location, Direction direction) {

        if (isStill())
            return;
        if (isHorizontal())
            location.offset(getRelativeDistance(), 0);
        else if (isVertical())
            location.offset(0, getRelativeDistance());

    }


}
