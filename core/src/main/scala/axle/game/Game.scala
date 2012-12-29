
package axle.game

import axle._
import collection._
import Stream.{ empty, cons }
import util.Random.shuffle

trait Game {

  game =>

  type PLAYER <: Player[this.type]
  type STATE <: State[this.type]
  type MOVE <: Move[this.type]
  type OUTCOME <: Outcome[this.type]

  def players(): immutable.Set[PLAYER]

  def introMessage(): Unit

  def startState(): STATE

  def startFrom(s: STATE): STATE

  def minimax(state: STATE, depth: Int, heuristic: STATE => Map[PLAYER, Double]): (MOVE, STATE, Map[PLAYER, Double]) =
    if (state.outcome.isDefined || depth <= 0) {
      (null.asInstanceOf[MOVE], null.asInstanceOf[STATE], heuristic(state)) // TODO null
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

  def alphabeta(state: STATE, depth: Int, heuristic: STATE => Map[PLAYER, Double]): (MOVE, Map[PLAYER, Double]) =
    _alphabeta(state, depth, players.map((_, Double.MinValue)).toMap, heuristic)

  case class AlphaBetaFold(move: MOVE, cutoff: Map[PLAYER, Double], done: Boolean) {

    def process(m: MOVE, state: STATE, heuristic: STATE => Map[PLAYER, Double]): AlphaBetaFold =
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

  def _alphabeta(state: STATE, depth: Int, cutoff: Map[PLAYER, Double], heuristic: STATE => Map[PLAYER, Double]): (MOVE, Map[PLAYER, Double]) =
    if (state.outcome.isDefined || depth <= 0) {
      (null.asInstanceOf[MOVE], heuristic(state)) // TODO null
    } else {
      val result = state.moves.foldLeft(AlphaBetaFold(null.asInstanceOf[MOVE], cutoff, false))(
        (in: AlphaBetaFold, move: MOVE) => in.process(move, state, heuristic)
      )
      (result.move, result.cutoff)
    }

  def moveStateStream(state: STATE): Stream[(MOVE, STATE)] =
    if (state.outcome.isDefined) {
      empty
    } else {
      val (move, nextState) = state.player.move(state)
      players.map(_.notify(move))
      cons((move, nextState), moveStateStream(nextState))
    }

  def scriptedMoveStateStream(state: STATE, moveIt: Iterator[MOVE]): Stream[(MOVE, STATE)] =
    if (state.outcome.isDefined || !moveIt.hasNext) {
      empty
    } else {
      val move = moveIt.next
      val nextState = state(move).get // TODO .get
      cons((move, nextState), scriptedMoveStateStream(nextState, moveIt))
    }

  def play(start: STATE = startState(), intro: Boolean = true): Option[STATE] = {
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

  def gameStream(start: STATE, intro: Boolean = true): Stream[STATE] =
    game.play(start, intro).map(end => {
      println(end.outcome.getOrElse("no winner")) // TODO
      cons(end, gameStream(game.startFrom(end), false))
    }).getOrElse(empty)

  def playContinuously(start: STATE = game.startState()): STATE =
    gameStream(start).last

}
