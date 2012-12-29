
package axle.game.ttt

import axle.game._
import axle.matrix.ArrayMatrixFactory._
import util.Random.{nextInt}
import collection._
import scalaz._
import Scalaz._

/**
 * TicTacToe is a 2-player perfect information zero-sum game
 */

case class TicTacToe(boardSize: Int = 3, xClass: String = "human", oClass: String = "ai")
  extends Game[TicTacToe] {

  implicit val ttt = this

  type PLAYER = TicTacToePlayer
  type MOVE = TicTacToeMove
  type STATE = TicTacToeState
  type OUTCOME = TicTacToeOutcome

  val x = player("X", "Player X", xClass)
  val o = player("O", "Player O", oClass)

  def state(player: TicTacToePlayer, board: Matrix[Option[String]]) =
    Some(new TicTacToeState(player, board))

  def move(player: TicTacToePlayer, position: Int) = TicTacToeMove(player, position)

  def player(id: String, description: String, which: String) = which match {
    case "random" => new RandomTicTacToePlayer(id, description)
    case "ai" => new AITicTacToePlayer(id, description)
    case _ => new InteractiveTicTacToePlayer(id, description)
  }

  def startState() = new TicTacToeState(x, startBoard())

  def startFrom(s: TicTacToeState) = startState()
  
  def numPositions() = boardSize * boardSize

  def introMessage() = "Intro message to Tic Tac Toe"

  def startBoard() = matrix[Option[String]](boardSize, boardSize, None)

  def players(): immutable.Set[TicTacToePlayer] = immutable.Set(x, o)

  def playerAfter(player: TicTacToePlayer): TicTacToePlayer =
    if (player == x) o else x

}
