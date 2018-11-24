package org.spoofer.fluff;

public interface Scenery extends Bot {

    /**
     * Requests a new Movement from the Scenery, for the given actor.
     * This is called when the actor has moved into this scenery
     * and is currently interecting the Scenery View.
     *
     *
     * The Scenery dictates if the current movement continues or is stopped,
     * depending on if the scenery is "solid".
     * If its solid, so nothing passes through it, the Scenery will issue a new movement,
     * directing the actor to the nearest surface of the scenery, outside the scenery itself.
     * If the acenery is not solid, allowing bots to pass through it, it will return the
     * current actors movement, which will continue, unchanged.
     *
     * @param actor the actor bot which has collided with this scenery
     * @param actorMovement the Movement the actor is currently executing (which caused the collision)
     * @return a new Movement, to be applied to the actor.
     *
     */
    Movement moveCollision(Bot actor, Movement actorMovement);
}
