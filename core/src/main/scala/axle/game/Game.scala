
package axle.game

import axle._
import collection._
import Stream.{ empty, cons }
import util.Random.shuffle

trait Game {

  game =>

  type PLAYER <: Player
  type MOVE <: Move
  type STATE <: State
  type OUTCOME <: Outcome

  def players(): immutable.Set[PLAYER]

  def introMessage(): Unit

  def startState(): STATE

  def minimax(state: STATE, depth: Int, heuristic: STATE => Map[PLAYER, Double]): (MOVE, Map[PLAYER, Double]) =
    if (state.outcome.isDefined || depth <= 0) {
      (null.asInstanceOf[MOVE], heuristic(state)) // TODO null
    } else {
      val moveValue = state.moves.map(move => (move, minimax(state(move), depth - 1, heuristic)._2))
      val bestValue = moveValue.map(mcr => (mcr._2)(state.player)).max
      moveValue.filter(mcr => (mcr._2)(state.player) == bestValue).toIndexedSeq.random
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
        val α = heuristic(state(m))
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
      val move = state.player.chooseMove(state)
      players.map(_.notify(move))
      val nextState = state(move)
      cons((move, nextState), moveStateStream(nextState))
    }

  def scriptedMoveStateStream(state: STATE, moveIt: Iterator[MOVE]): Stream[(MOVE, STATE)] =
    if (state.outcome.isDefined || !moveIt.hasNext) {
      empty
    } else {
      val move = moveIt.next
      val nextState = state(move)
      cons((move, nextState), scriptedMoveStateStream(nextState, moveIt))
    }

  def play(start: STATE): Option[OUTCOME] = {
    for (player <- players()) {
      player.introduceGame()
    }
    moveStateStream(start).lastOption.flatMap({
      case (lastMove, lastState) => {
        lastState.outcome.map(outcome =>
          for (player <- players()) {
            player.notify(outcome)
            player.endGame(lastState)
          }
        )
        lastState.outcome
      }
    })
  }

  trait Event {

    def displayTo(player: PLAYER): String

  }

  abstract class Move(player: PLAYER)
    extends Event {

  }

  class Outcome(winner: Option[PLAYER])
    extends Event {

    def displayTo(player: PLAYER): String =
      winner.map(wp =>
        if (wp equals player) {
          "You have beaten " + game.players().filter(p => !(p equals player)).map(_.toString).toList.mkString(" and ") + "!"
        } else {
          "%s beat you!".format(wp)
        }
      ).getOrElse("The game was a draw.")
  }

  abstract class Player(_id: String, description: String) {

    def id() = _id

    def chooseMove(state: STATE): MOVE

    override def toString(): String = description

    def introduceGame(): Unit = {}

    def notify(event: Event): Unit = {}

    def endGame(state: STATE): Unit = {}
  }

  trait State {

    def player(): PLAYER

    def apply(move: MOVE): STATE

    def outcome(): Option[OUTCOME]

    def moves(): Seq[MOVE]

  }

}
