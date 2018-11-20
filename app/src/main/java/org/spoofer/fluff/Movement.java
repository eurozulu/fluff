package org.spoofer.fluff;

import android.graphics.Rect;

public interface Movement {

    int DEFAULT_VELOCITY = 350;  // dp's per second;

    enum Direction {
        Left,
        Right,
        Up,
        Down,
        Stop
    }

    int getVelocity();

    void setVelocity(int velocity);

    Rect getStartLocation();

    void setStartLocation(Rect startLocation);

    Rect getEndLocation();

    /**
     * Sets the Direction and distance based on the given Rect.
     * Takes the biggest single movement, so if both X and Y axes change
     * only one is moved.
     *
     * @param toLocation
     */
    void setEndLocation(Rect toLocation);

    boolean isStill();

    boolean isHorizontal();

    boolean isVertical();

    long getDuration();

    Direction getDirection();

    void setDirection(Direction direction);

    int getDistance();

    int getRelativeDistance();

    void setDistance(int distance);

}
