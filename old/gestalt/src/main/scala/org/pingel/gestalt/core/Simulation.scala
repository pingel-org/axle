package org.pingel.gestalt.core;

import java.util.logging.Logger

case class Simulation(goals: List[Name], constraints: List[SimpleTransform], forms: List[Form], transforms: List[Transform]) {
  // the working variables:
  var frontier: SimulationFrontier = null

  var complete = false

  def initialize(lexicon: Lexicon): Unit = {
    frontier = new SimulationFrontier()
    frontier.initialize(this, lexicon)
  }

  def createAtom() = {
    val name = new Name()
    // Note: this name is free !!
    new SimpleForm(name, null)
  }

  def createComplex() = new ComplexForm(createAtom(), createAtom(), null)

  def hasNext() = (!complete)

  def next(history: History, lexicon: Lexicon): Unit = {

    for (goal <- goals) {
      val explanation = frontier.smallest(goal)

      Logger.global.info("Simulation.next explanation.candidate " + explanation.candidate.toString())
      Logger.global.info("Simulation.next before calls to explanation.next()")

      explanation.next(history, lexicon)

      Logger.global.info("Simulation.next after call to explanation.next()")

      if (!explanation.hasNext()) {

        frontier.remove(goal, explanation)

        if (explanation.fits(lexicon)) {
          println()
          println("goal is satisfied by: " + explanation.candidate.toString())
          println()
          println("explanation:")
          println(explanation.toString(lexicon))
          complete = true
        } else {

          // Here we pass the explanation.candidate, rather than the whole explanation
          // because it seems that "explore" doesn't need the entire explanation.  This
          // may change.

          val exploration = new CandidateExploration(this, goal, explanation.candidate)

          while (exploration.hasNext()) {
            exploration.next(history, lexicon)
          }
        }

      }

      // TODO note somewhere (explored_by_goal) that candidate has been explored
    }

  }

  class CandidateExploration(simulation: Simulation, goal: Name, candidate: Form) {

    // private Set variables;

    // TODO this will change if the semantics of "scopedVars" changes
    // (I imagine someday scoping will happen elsewhere)
    val variable_it = candidate.lambda.getNames().iterator

    def next(history: History, lexicon: Lexicon): Unit = {

      val variable = variable_it.next()

      Logger.global.info("Simulation.explore variable is " + variable)

      var literal_index = 0
      while (literal_index < forms.size) {

        val literal = forms(literal_index)

        var replacement_map = Map[Name, Form]()
        replacement_map += variable -> literal

        frontier.add(goal,
          new SimulationExplanation(simulation,
            goal,
            candidate.duplicateAndReplace(replacement_map), lexicon))

        literal_index += 1
      }

      var replacement_map = Map[Name, Form]()
      replacement_map += variable -> createComplex()
      frontier.add(goal,
        new SimulationExplanation(simulation,
          goal,
          candidate.duplicateAndReplace(replacement_map), lexicon))

    }

    def hasNext() = variable_it.hasNext

  }
}
