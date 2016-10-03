
package axle.game.ttt

import axle.game._
import spire.implicits._

/**
 * TicTacToe is a 2-player perfect information zero-sum game
 */

case class TicTacToe(
  boardSize: Int = 3,
  xClass: String = "human",
  oClass: String = "ai")
  extends Game[TicTacToe] {

  implicit val ttt = this

  type PLAYER = TicTacToePlayer
  type MOVE = TicTacToeMove
  type STATE = TicTacToeState
  type OUTCOME = TicTacToeOutcome

  val x = player("X", "Player X", xClass)
  val o = player("O", "Player O", oClass)

  val playersSeq = Vector(x, o)

  def state(
    player: TicTacToePlayer,
    board: Array[Option[TicTacToePlayer]],
    eventQueue: Map[TicTacToePlayer, List[Event[TicTacToe]]]): Option[TicTacToeState] =
    Some(TicTacToeState(player, board, boardSize, eventQueue))

  def player(id: String, description: String, which: String): TicTacToePlayer =
    which match {
      case "random" => RandomTicTacToePlayer(id, description)
      case "ai"     => AITicTacToePlayer(id, description)
      case _        => InteractiveTicTacToePlayer(id, description)
    }

  def startState: TicTacToeState = TicTacToeState(x, startBoard, boardSize)

  def startFrom(s: TicTacToeState): Option[TicTacToeState] = Some(startState)

  def numPositions: Int = boardSize * boardSize

  def introMessage: String = "Intro message to Tic Tac Toe"

  def startBoard: Array[Option[TicTacToePlayer]] =
    (0 until (boardSize * boardSize)).map(i => None).toArray

  def players: Set[TicTacToePlayer] = Set(x, o)

  def playerAfter(player: TicTacToePlayer): TicTacToePlayer =
    if (player === x) o else x

}
