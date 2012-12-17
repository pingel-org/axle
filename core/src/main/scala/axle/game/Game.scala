
package axle.game

import collection._
import Stream.{ empty, cons }

trait Game {

  game =>

  type PLAYER <: Player
  type MOVE <: Move
  type STATE <: State
  type OUTCOME <: Outcome

  def players(): immutable.Set[PLAYER]

  def introMessage(): Unit

  def minimax(state: STATE, depth: Int, heuristic: STATE => Map[PLAYER, Double]): (MOVE, Map[PLAYER, Double]) =
    if (state.outcome.isDefined || depth <= 0) {
      (null.asInstanceOf[MOVE], heuristic(state)) // TODO null
    } else {
      state.moves
        .map(move => (move, minimax(state(move), depth - 1, heuristic)._2))
        .maxBy(mcr => (mcr._2)(state.player))
    }

  def moveStateStream(state: STATE): Stream[(MOVE, STATE)] = state.outcome.isDefined match {
    case true => empty
    case false => {
      val move = state.player.chooseMove(state)
      players.map(_.notify(move))
      val nextState = state(move)
      cons((move, nextState), moveStateStream(nextState))
    }
  }

  def scriptedMoveStateStream(state: STATE, moveIt: Iterator[MOVE]): Stream[(MOVE, STATE)] = (state.outcome.isDefined || !moveIt.hasNext) match {
    case true => Stream.empty
    case false => {
      val move = moveIt.next
      val nextState = state(move)
      cons((move, nextState), scriptedMoveStateStream(nextState, moveIt))
    }
  }

  def play(start: STATE): Option[OUTCOME] = {
    for (player <- players()) {
      player.introduceGame()
    }
    val lastMoveState = moveStateStream(start).last
    lastMoveState._2.outcome.map(outcome =>
      for (player <- players()) {
        player.notify(outcome)
        player.endGame(lastMoveState._2)
      }
    )
    lastMoveState._2.outcome
  }

  trait Event { // game: Game

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
