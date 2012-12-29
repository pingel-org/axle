
package axle.game

import axle._
import collection._
import Stream.{ empty, cons }
import util.Random.shuffle

abstract class Game[G <: Game[G]] {

  self: G =>

  type PLAYER <: Player[G]
  type STATE <: State[G]
  type MOVE <: Move[G]
  type OUTCOME <: Outcome[G]

  def players(): immutable.Set[G#PLAYER]

  def introMessage(): Unit

  def startState(): G#STATE

  def startFrom(s: G#STATE): G#STATE

  def minimax(state: G#STATE, depth: Int, heuristic: G#STATE => Map[G#PLAYER, Double]): (G#MOVE, G#STATE, Map[G#PLAYER, Double]) =
    if (state.outcome.isDefined || depth <= 0) {
      (null.asInstanceOf[MOVE], null.asInstanceOf[G#STATE], heuristic(state)) // TODO null
    } else {
      // TODO: .get
      val moveValue = state.moves.map(move => {
        val newState = state(move).get // TODO: .get
        (move, state, minimax(newState, depth - 1, heuristic)._3)
      })
      val bestValue = moveValue.map(mcr => (mcr._3)(state.player)).max
      moveValue.filter(mcr => (mcr._3)(state.player) == bestValue).toIndexedSeq.random
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

  def moveStateStream(state: G#STATE): Stream[(G#MOVE, G#STATE)] =
    if (state.outcome.isDefined) {
      empty
    } else {
      val (move, nextState) = state.player.move(state)
      players.map(_.notify(move))
      cons((move, nextState), moveStateStream(nextState))
    }

  def scriptedMoveStateStream(state: G#STATE, moveIt: Iterator[G#MOVE]): Stream[(G#MOVE, G#STATE)] =
    if (state.outcome.isDefined || !moveIt.hasNext) {
      empty
    } else {
      val move = moveIt.next
      val nextState = state(move).get // TODO .get
      cons((move, nextState), scriptedMoveStateStream(nextState, moveIt))
    }

  def play(start: G#STATE = startState(), intro: Boolean = true): Option[G#STATE] = {
    if (intro) {
      for (player <- players()) {
        player.introduceGame()
      }
    }
    moveStateStream(start).lastOption.map({
      case (lastMove, lastState) => {
        for (player <- players()) {
          lastState.outcome.map(outcome =>
            player.notify(outcome)
          )
          player.endGame(lastState)
        }
        lastState
      }
    })
  }

  def gameStream(start: G#STATE, intro: Boolean = true): Stream[G#STATE] =
    play(start, intro).map(end => {
      println(end.outcome.getOrElse("no winner")) // TODO
      cons(end, gameStream(startFrom(end), false))
    }).getOrElse(empty)

  def playContinuously(start: G#STATE = startState()): G#STATE =
    gameStream(start).last

}
