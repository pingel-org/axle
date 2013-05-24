package axle.ml

import axle.graph._

/**
 *
 * http://en.wikipedia.org/wiki/Hidden_Markov_model
 *
 */

sealed trait MarkovModelState

case object MarkovModelStartState extends MarkovModelState {
  override def toString() = "start"
}

case class UnobservableMarkovModelState(label: String) extends MarkovModelState {
  override def toString() = label
}

case class ObservableMarkovModelState(label: String) extends MarkovModelState {
  override def toString() = label
}

class HiddenMarkovModel(
  states: IndexedSeq[UnobservableMarkovModelState],
  observations: IndexedSeq[ObservableMarkovModelState],
  startProbability: Map[UnobservableMarkovModelState, Double],
  transitionProbability: Map[UnobservableMarkovModelState, Map[UnobservableMarkovModelState, Double]], // can be fully-connected
  emissionProbability: Map[UnobservableMarkovModelState, Map[ObservableMarkovModelState, Double]] // arrows from un-observables to observables
  ) {

  val startState = MarkovModelStartState

  val graph = JungDirectedGraph[MarkovModelState, Double](
    states ++ observations ++ List(startState),
    (vs: Seq[Vertex[MarkovModelState]]) => {
      val state2vertex = vs.map(v => (v.payload, v)).toMap
      val startEdges = startProbability.map({ case (dest, p) => (state2vertex(startState), state2vertex(dest), p) })
      val stateEdges = transitionProbability.flatMap({ case (from, toMap) => toMap.map({ case (to, p) => (state2vertex(from), state2vertex(to), p) }) })
      val emissionEdges = emissionProbability.flatMap({ case (from, toMap) => toMap.map({ case (to, p) => (state2vertex(from), state2vertex(to), p) }) })
      startEdges.toList ++ stateEdges.toList ++ emissionEdges.toList
    }
  )

}