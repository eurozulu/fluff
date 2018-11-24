package org.spoofer.fluff;

public interface Director {


    void moveBot(Agent agent, Movement.Direction direction);
    void moveBot(Agent agent, Movement movement);

    void stopBot(Agent agent);

    void stopAll();

    void setScene(Scene scene);

    boolean isPerforming(Agent agent);
    boolean isInCollision(Agent agent);

}
