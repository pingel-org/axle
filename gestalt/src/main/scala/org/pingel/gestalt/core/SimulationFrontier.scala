package org.pingel.gestalt.core

import java.util.PriorityQueue
import java.util.logging.Logger

case class SimulationFrontier {
  var openByGoal = Map[Name, PriorityQueue[SimulationExplanation]]()

  def initialize(simulation: Simulation, lexicon: Lexicon): Unit = {
    for (goal <- simulation.goals) {
      var open = new PriorityQueue[SimulationExplanation]()
      open.add(new SimulationExplanation(simulation, goal, simulation.createAtom(), lexicon))
      openByGoal += goal -> open
    }
  }

  def smallest(goal: Name) = openByGoal(goal).poll()

  def remove(goal: Name, explanation: SimulationExplanation): Unit = {
    openByGoal(goal).remove(explanation)
  }

  def add(goal: Name, explanation: SimulationExplanation): Unit = {
    Logger.global.entering("SimulationFrontier", "addToOpen")
    openByGoal(goal).add(explanation)
  }

}
