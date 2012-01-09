package org.pingel.gestalt.core

import java.util.PriorityQueue
import java.util.logging.Logger

case class SimulationFrontier
{
  var openByGoal = Map[Name, PriorityQueue[SimulationExplanation]]()

  def initialize(simulation: Simulation, lexicon: Lexicon): Unit = {
    for( goal <- simulation.goals ) {
    	var open = new PriorityQueue[SimulationExplanation]()
    	open += new SimulationExplanation(simulation, goal, simulation.createAtom(), lexicon)
    	openByGoal += goal -> open
    }
  }

  def smallest(goal: Name) = openByGoal.get(goal).poll()

  def remove(goal: Name, explanation: SimulationExplanation): Unit = {
    openByGoal.get(goal).remove(explanation)
  }

  def add(goal: Name, explanation: SimulationExplanation): Unit = {
    Logger.global.entering("SimulationFrontier", "addToOpen")
    openByGoal.get(goal).add(explanation)
  }

}
