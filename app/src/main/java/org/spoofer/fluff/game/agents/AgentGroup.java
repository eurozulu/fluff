package org.spoofer.fluff.game.agents;

import android.support.annotation.IdRes;

import org.spoofer.fluff.game.Director;

import java.util.HashSet;
import java.util.Set;

public class AgentGroup {

    private final Set<Agent> agents;
    private final Director director;


    public AgentGroup(Director director, @IdRes int[] actorIds ) {
        this.director = director;
        this.agents = createAgents(director, actorIds);
    }


    public Director getDirector() {
        return director;
    }

    public Set<Agent> getAgents() { return agents; }


    private Set<Agent> createAgents(Director director, @IdRes int[] actorIds ) {
        Set<Agent> agents = new HashSet<>();
        for (int actorId : actorIds)
            agents.add(new SimpleAgent(director, actorId));
        return agents;
    }



}
