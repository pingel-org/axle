package org.pingel.gestalt.core

import scala.collection._

case class SimulationExplanation(simulation: Simulation, goal: Name, candidate: Form, lexicon: Lexicon)
  extends Comparable[SimulationExplanation] {
  var betaSystemByConstraint = Map[SimpleTransform, ComplexTransform]()
  var betaCallByConstraint = Map[SimpleTransform, ComplexTransformCall]()
  var sourceByConstraint = Map[SimpleTransform, Form]()
  var destinationByConstraint = Map[SimpleTransform, Form]()

  var steps = 0

  var constraintIndex = 0

  while (constraintIndex < simulation.constraints.size) {
    val constraint = simulation.constraints(constraintIndex)
    val betaCall = makeBetaCall(constraint, lexicon)
    betaCallByConstraint += constraint -> betaCall
    constraintIndex += 1
  }

  def makeBetaCall(constraint: SimpleTransform, lexicon: Lexicon): ComplexTransformCall = {
    val goal_replacement_map = immutable.Map[Name, Form](goal -> candidate)

    // is this replacement free or not ???
    val source_replacement_map = immutable.Map[Name, Form](
      constraint.map.keySet.map(k => (k, new SimpleForm(constraint.map(k), null))).toList: _*
    ) // though I'm only using SimpleFormm

    var source = lexicon.getForm(constraint.guardName).duplicateAndReplace(source_replacement_map)
    source = source.duplicateAndReplace(goal_replacement_map)

    sourceByConstraint += constraint -> source

    val destination = lexicon.getForm(constraint.outName).duplicateAndReplace(goal_replacement_map)

    destinationByConstraint += constraint -> destination

    GLogger.global.info("SimulationExplanation.makeBetaCall goal is " + goal)
    GLogger.global.info("SimulationExplanation.makeBetaCall source is " + source)
    GLogger.global.info("SimulationExplanation.makeBetaCall destination is " + destination)

    var arcs = new mutable.ListBuffer[TransformEdge]()
    var exits = Set[TransformVertex]()

    val beta_in_node = new TransformVertex(new Name("in"), true, false)
    exits += beta_in_node
    var procedure_index = 0
    while (procedure_index < simulation.transforms.size) {
      val procedure = simulation.transforms(procedure_index)
      // note: this should be a "wild" Traversal
      // TODO !!!!!!!! arcs.add(new TransformEdge(procedure, beta_in_node, null, beta_in_node));
      procedure_index += 1
    }

    // TODO does this really need to be the frontier's createAtom() ?

    if (simulation.frontier == null) {
      GLogger.global.info("SimulationExplanation.makeBetaCall simulation.frontier == null")
      System.exit(1)
    }

    val newSituation = simulation.createAtom()
    val newName = new Name()
    lexicon.put(newName, newSituation)
    val betaSystem = new ComplexTransform(newName)
    betaSystem.addVertex(beta_in_node)
    // TODO add nodes to the betaSystem (mark exit nodes, too)
    for (arc <- arcs) {
      // TODO add arcs to the betaSystem
    }

    var betaHistory = new History()

    // TODO what is the traversal of betaCall ?? !!!!

    val betaCV = new CallVertex(betaHistory.nextVertexId(), betaSystem.start, source);
    val betaCall = betaSystem.constructCall(betaHistory.nextCallId(), betaHistory, lexicon, null).asInstanceOf[ComplexTransformCall]
    betaCall.unify(betaHistory, betaCV)
    betaHistory.addCall(betaCall)

    if (!betaCall.hasNext) {
      GLogger.global.info("SimulationExplanation.makeBetaCall the betaCall is immediately not active")
    }

    betaCall
  }

  def next(history: History, lexicon: Lexicon): Unit = {
    steps += 1
    GLogger.global.entering("SimulationExplanation", "next");
    var constraintIndex = 0
    while (constraintIndex < simulation.constraints.size) {
      GLogger.global.info("SimulationExplanation.next constraintIndex = " + constraintIndex)
      val constraint = simulation.constraints(constraintIndex)
      val betaCall = betaCallByConstraint(constraint)
      if (betaCall.hasNext) {
        GLogger.global.info("SimulationExplanation.next betaCall.next()")
        betaCall.next(history, lexicon)
      }
      constraintIndex += 1
    }
  }

  // For a SimulationExplanation to be "complete" means that
  // every betaCall (one per constraint) is done.
  // Of course, this says nothing about whether or not the betaCall
  // ended in an output state which matches what the constraint needs.
  // This is what constraintFits is for.

  def hasNext(): Boolean = {
    GLogger.global.entering("SimulationExplanation", "hasNext")
    var constraintIndex = 0
    while (constraintIndex < simulation.constraints.size) {
      GLogger.global.info("SimulationExplanation.hasNext constraintIndex = " + constraintIndex)
      val constraint = simulation.constraints(constraintIndex)
      val betaCall = betaCallByConstraint(constraint)
      if (betaCall.hasNext) {
        GLogger.global.info("SimulationExplanation.hasNext constraint " + constraintIndex + " is active.  returning true")
        return true
      }
      constraintIndex += 1
    }
    GLogger.global.info("SimulationExplanation.hasNext about to return false")
    false
  }

  def constraintFits(constraint: SimpleTransform, lexicon: Lexicon): ComplexTransform = {
    GLogger.global.entering("SimulationExplanation", "constraintFits")

    GLogger.global.info("SimulationExplanation.constraintFits constraint = " + constraint.toString())

    // return the cached copy
    // when is this ever called ???
    var rs = betaSystemByConstraint(constraint)
    if (rs != null) {
      GLogger.global.info("SimulationExplanation.constraintFits returning cached copy of the transform system")
      return rs
    }

    val betaCall = betaCallByConstraint(constraint)
    val source = sourceByConstraint(constraint)
    val destination = destinationByConstraint(constraint)

    GLogger.global.info("SimulationExplanation.constraintFits source situation is " + source.toString())
    GLogger.global.info("SimulationExplanation.constraintFits destination situation is " + destination.toString())

    for (output <- betaCall.getVertices()) {

      GLogger.global.info("SimulationExplanation.constraintFits checking to see if this output matches the destination: " + output.getForm().toString())
      GLogger.global.info("SimulationExplanation.constraintFits betaCall output situation: " + output.getForm())

      if (output.getForm().equals(destination)) {

        GLogger.global.info("SimulationExplanation.constraintFits it does")

        // TODO this first parameter (new Name()) is wrong !!!
        rs = betaCall.getTransformSystemTo(new Name(), source, output)

        betaSystemByConstraint += constraint -> rs

        // TODO not sure if this is the best place to alter the newLexicon.  It may be that these
        // TransformSystem's go unused, in which case we don't want to leave junk that refers to them

        lexicon.put(new Name((lexicon.getNameOf(constraint)).base + "_implementation"), rs)
        lexicon.put(new Name((lexicon.getNameOf(constraint)).base + "_guard"), source)

        return rs
      } else {
        GLogger.global.info("SimulationExplanation.constraintFits it doesn't")
      }
    }
    null
  }

  def fits(lexicon: Lexicon): Boolean = {
    GLogger.global.entering("SimulationExplanation", "fits")
    var constraintIndex = 0
    while (constraintIndex < simulation.constraints.size) {
      val constraint = simulation.constraints(constraintIndex)
      if (constraintFits(constraint, lexicon) == null) {
        return false
      }
      constraintIndex += 1
    }
    true
  }

  def compareTo(other: SimulationExplanation) = candidate.size.compareTo(other.candidate.size())
  // return candidate.size() + steps;

  def toString(lexicon: Lexicon) = {

    (0 until simulation.constraints.size).map(constraintIndex => {
      val constraint = simulation.constraints(constraintIndex)
      constraint.toString() + "\n\n"
      val rs = betaSystemByConstraint(constraint)
      rs match {
        case null => GLogger.global.info("SimulationExplanation.toString rs == null!")
        case _ => {
          "situation " + rs.guardName + " " + lexicon.getForm(rs.guardName).toString() + "\n" +
            "\n" +
            rs.toString() + "\n"
        }
      }
    }).mkString("")

  }

}
