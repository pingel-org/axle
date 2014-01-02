package axle.ml

import axle.graph._
import spire.math._
import spire.algebra._
import spire.implicits._

/**
 *
 * http://en.wikipedia.org/wiki/Hidden_Markov_model
 *
 */

sealed trait MarkovModelState

object MarkovModelState {

  implicit val mmsEq = new Eq[MarkovModelState] {
    def eqv(x: MarkovModelState, y: MarkovModelState): Boolean = x equals y
  }
}

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

/**
 * http://en.wikipedia.org/wiki/Baum%E2%80%93Welch_algorithm
 *
 * The Baum–Welch algorithm is a particular case of a generalized expectation-maximization (GEM) algorithm.
 * It can compute maximum likelihood estimates and posterior mode estimates for the parameters (transition
 * and emission probabilities) of an HMM, when given only emissions as training data.
 *
 */

object BaumWelch {

  // For a given cell Si in the transition matrix, all paths to that cell are summed.
  // There is a link (transition from that cell to a cell Sj).
  val χ = 0d // the joint probability of Si, the link, and Sj can be calculated and normalized by the probability of the entire string.

  val σ = 0d // probability of all paths with all links emanating from Si, Normalize this by the probability of the entire string.

  val foo = χ / σ
  // This is dividing the expected transition from Si to Sj by the expected transitions from Si.
  // As the corpus grows, and particular transitions are reinforced, they will increase in value, reaching a local maximum.
  // No way to ascertain a global maximum is known.

}
