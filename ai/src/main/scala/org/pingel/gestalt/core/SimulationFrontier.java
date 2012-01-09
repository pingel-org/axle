package org.pingel.gestalt.core;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Logger;

public class SimulationFrontier
{
    private Map<Name, PriorityQueue<SimulationExplanation>> openByGoal;

    SimulationFrontier()
    {
		this.openByGoal = new HashMap<Name, PriorityQueue<SimulationExplanation>>();
    }

	public void initialize(Simulation simulation, Lexicon lexicon) {
        
	    for( Name goal : simulation.goals ) {
			PriorityQueue<SimulationExplanation> open = new PriorityQueue<SimulationExplanation>();
			open.add(new SimulationExplanation(simulation, goal, simulation.createAtom(), lexicon));
			openByGoal.put(goal, open);
		}
	}

    public SimulationExplanation smallest(Name goal)
    {
        PriorityQueue<SimulationExplanation> open = openByGoal.get(goal);
        
		SimulationExplanation smallest = open.poll();
			
        return smallest;
    }

    public void remove(Name goal, SimulationExplanation explanation)
    {
        PriorityQueue<SimulationExplanation> open = openByGoal.get(goal);
        open.remove(explanation);
    }

    public void add(Name goal, SimulationExplanation explanation)
    {
		Logger.global.entering("SimulationFrontier", "addToOpen");
		        
		PriorityQueue<SimulationExplanation> open = openByGoal.get(goal);
        
        open.add(explanation);
    }

}
