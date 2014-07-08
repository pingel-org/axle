
package axle.game

import scala.Stream.cons
import scala.Stream.empty
import scala.util.Random.nextInt

import spire.compat.integral
import spire.implicits.eqOps
import spire.math.Real

abstract class Game[G <: Game[G]] {

  self: G =>

  type PLAYER <: Player[G]
  type STATE <: State[G]
  type EVENT <: Event[G]
  type MOVE <: Move[G]
  type OUTCOME <: Outcome[G]

  def players: Set[G#PLAYER]

  def introMessage: String

  def startState: G#STATE

  def startFrom(s: G#STATE): Option[G#STATE]

  def minimax(state: G#STATE, depth: Int, heuristic: G#STATE => Map[G#PLAYER, Real]): (G#MOVE, G#STATE, Map[G#PLAYER, Real]) =
    if (state.outcome.isDefined || depth <= 0) {
      (null.asInstanceOf[MOVE], null.asInstanceOf[G#STATE], heuristic(state)) // TODO null
    } else {
      // TODO: .get
      val moveValue = state.moves.map(move => {
        val newState = state(move).get // TODO: .get
        (move, state, minimax(newState, depth - 1, heuristic)._3)
      })
      val bestValue = moveValue.map(mcr => (mcr._3)(state.player)).max
      val matches = moveValue.filter(mcr => (mcr._3)(state.player) === bestValue).toIndexedSeq
      matches(nextInt(matches.length))
    }

  /**
   * α-β pruning generalized for N-player non-zero-sum games
   *
   * 2-player zero-sum version described at:
   *
   *   http://en.wikipedia.org/wiki/Alpha-beta_pruning
   *
   */

  def alphabeta(state: G#STATE, depth: Int, heuristic: G#STATE => Map[G#PLAYER, Double]): (G#MOVE, Map[G#PLAYER, Double]) =
    _alphabeta(state, depth, players.map((_, Double.MinValue)).toMap, heuristic)

  case class AlphaBetaFold(move: G#MOVE, cutoff: Map[G#PLAYER, Double], done: Boolean) {

    def process(m: G#MOVE, state: G#STATE, heuristic: G#STATE => Map[G#PLAYER, Double]): AlphaBetaFold =
      if (done) {
        this
      } else {
        val α = heuristic(state(m).get)
        if (cutoff(state.player) <= α(state.player)) // TODO: forall other players ??
          AlphaBetaFold(m, α, false) // TODO move = m?
        else
          AlphaBetaFold(m, cutoff, true)
      }
  }

  def _alphabeta(state: G#STATE, depth: Int, cutoff: Map[G#PLAYER, Double], heuristic: G#STATE => Map[G#PLAYER, Double]): (G#MOVE, Map[G#PLAYER, Double]) =
    if (state.outcome.isDefined || depth <= 0) {
      (null.asInstanceOf[G#MOVE], heuristic(state)) // TODO null
    } else {
      val result = state.moves.foldLeft(AlphaBetaFold(null.asInstanceOf[G#MOVE], cutoff, false))(
        (in: AlphaBetaFold, move: G#MOVE) => in.process(move, state, heuristic)
      )
      (result.move, result.cutoff)
    }

  def moveStateStream(s0: G#STATE): Stream[(G#MOVE, G#STATE)] =
    if (s0.outcome.isDefined) {
      empty
    } else {
      val s1 = s0.displayEvents(Set(s0.player))
      val (move, _) = s1.player.move(s1) // TODO: figure out why in some cases the second argument (a State) wasn't modified (eg minimax)
      val s2 = s1(move).get // TODO .get
      val s3 = s2.broadcast(players, move)
      cons((move, s3), moveStateStream(s3))
    }

  def scriptedMoveStateStream(state: G#STATE, moveIt: Iterator[G#MOVE]): Stream[(G#MOVE, G#STATE)] =
    if (state.outcome.isDefined || !moveIt.hasNext) {
      empty
    } else {
      val move = moveIt.next
      val nextState = state(move).get // TODO .get
      cons((move, nextState), scriptedMoveStateStream(nextState, moveIt))
    }

  def play(start: G#STATE = startState, intro: Boolean = true): Option[G#STATE] = {
    if (intro) {
      players foreach { player =>
        player.introduceGame()
      }
    }
    moveStateStream(start).lastOption.map({
      case (lastMove, s0) => {
        val s1 = s0.outcome.map(o => s0.broadcast(players, o)).getOrElse(s0)
        val s2 = s1.displayEvents(players)
        players foreach { player =>
          player.endGame(s2)
        }
        s2
      }
    })
  }

  def gameStream(start: G#STATE, intro: Boolean = true): Stream[G#STATE] =
    play(start, intro).flatMap(end => {
      startFrom(end).map(newStart =>
        cons(end, gameStream(newStart, false))
      )
    }).getOrElse(empty)

  def playContinuously(start: G#STATE = startState): G#STATE =
    gameStream(start).last

}
