package axle.ml

import axle.algebra.DirectedGraph
import axle.syntax.directedgraph._
import spire.algebra.Eq
import spire.implicits.DoubleAlgebra

import axle.Show

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

case class MarkovModelStartState() extends MarkovModelState

object MarkovModelStartState {
  implicit def showStart: Show[MarkovModelStartState] =
    new Show[MarkovModelStartState] {
      def text(s: MarkovModelStartState): String = "start"
    }
}

case class UnobservableMarkovModelState(label: String) extends MarkovModelState

object UnobservableMarkovModelState {
  implicit def showStart: Show[UnobservableMarkovModelState] =
    new Show[UnobservableMarkovModelState] {
      def text(s: UnobservableMarkovModelState): String = s.label
    }
}

case class ObservableMarkovModelState(label: String) extends MarkovModelState
object ObservableMarkovModelState {
  implicit def showStart: Show[ObservableMarkovModelState] =
    new Show[ObservableMarkovModelState] {
      def text(s: ObservableMarkovModelState): String = s.label
    }
}

class HMMEdge[N](p: N)

case class HiddenMarkovModel[DG](
  states: IndexedSeq[UnobservableMarkovModelState],
  observations: IndexedSeq[ObservableMarkovModelState],
  startProbability: Map[UnobservableMarkovModelState, Double],
  transitionProbability: Map[UnobservableMarkovModelState, Map[UnobservableMarkovModelState, Double]], // can be fully-connected
  emissionProbability: Map[UnobservableMarkovModelState, Map[ObservableMarkovModelState, Double]] // arrows from un-observables to observables
  )(implicit dg: DirectedGraph[DG, MarkovModelState, HMMEdge[Double]]) {

  val startState = MarkovModelStartState()

  val graph = dg.make(
    states ++ observations ++ List(startState),
    {
      val startEdges = startProbability.map({ case (dest, p) => (startState, dest, new HMMEdge(p)) })
      val stateEdges = transitionProbability.flatMap({ case (from, toMap) => toMap.map({ case (to, p) => (from, to, new HMMEdge(p)) }) })
      val emissionEdges = emissionProbability.flatMap({ case (from, toMap) => toMap.map({ case (to, p) => (from, to, new HMMEdge(p)) }) })
      startEdges.toList ++ stateEdges.toList ++ emissionEdges.toList
    })

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
