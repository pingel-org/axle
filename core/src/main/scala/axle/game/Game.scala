
package axle.game

import scala.collection._

trait Game {

  game =>
  
  type PLAYER <: Player
  type MOVE <: Move
  type STATE <: State
  type OUTCOME <: Outcome

  def players(): immutable.Set[PLAYER]

  def introMessage(): Unit

  def moveStateStream(state: STATE): Stream[(MOVE, STATE)] = state.isTerminal match {
    case true => Stream.empty
    case false => {
      val move = state.player.chooseMove(state)
      players.map(_.notify(move))
      val nextState = state(move)
      Stream.cons((move, nextState), moveStateStream(nextState))
    }
  }

  def scriptedMoveStateStream(state: STATE, moveIt: Iterator[MOVE]): Stream[(MOVE, STATE)] = (state.isTerminal || !moveIt.hasNext) match {
    case true => Stream.empty
    case false => {
      val move = moveIt.next
      val nextState = state(move)
      Stream.cons((move, nextState), scriptedMoveStateStream(nextState, moveIt))
    }
  }

  def play(start: STATE): Unit = {
    for (player <- players()) {
      player.introduceGame()
    }
    val lastMoveState = moveStateStream(start).last
    lastMoveState._2.getOutcome.map(outcome =>
      for (player <- players()) {
        player.notify(outcome)
        player.endGame(lastMoveState._2)
      }
    )

  }

  trait Event { // game: Game

    def displayTo(player: PLAYER): String

  }

  abstract class Move(player: PLAYER)
    extends Event {

  }

  class Outcome(winner: Option[PLAYER])
    extends Event {

    def displayTo(player: PLAYER): String = winner match {

      case None => "The game was a draw."

      case Some(player) => "You have beaten " + game.players().filter(_ != winner).map(_.toString).toList.mkString(" and ") + "!"

      case _ => "%s beat you!".format(winner)
    }

  }

  abstract class Player(id: String, description: String) {

    def getId() = id

    def chooseMove(state: STATE): MOVE

    override def toString(): String = description

    def introduceGame(): Unit = {}

    def notify(event: Event): Unit = {}

    def endGame(state: STATE): Unit = {}
  }

  trait State {

    def player(): PLAYER

    def apply(move: MOVE): STATE

    def isTerminal(): Boolean

    def getOutcome(): Option[OUTCOME]

  }

}
