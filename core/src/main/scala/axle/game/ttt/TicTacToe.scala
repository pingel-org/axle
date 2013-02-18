
package axle.game.ttt

import axle.game._
import axle.matrix._
import util.Random.{ nextInt }
import collection._

/**
 * TicTacToe is a 2-player perfect information zero-sum game
 */

case class TicTacToe(boardSize: Int = 3, xClass: String = "human", oClass: String = "ai")
  extends Game[TicTacToe] {

  implicit val ttt = this

  val tttmm = new JblasMatrixModule() {}

  type Matrix[T] = tttmm.Matrix[T]

  type PLAYER = TicTacToePlayer
  type MOVE = TicTacToeMove
  type STATE = TicTacToeState
  type OUTCOME = TicTacToeOutcome

  val x = player("X", "Player X", xClass)
  val o = player("O", "Player O", oClass)

  def state(player: TicTacToePlayer, board: Matrix[Option[String]], eventQueue: immutable.Map[TicTacToePlayer, List[Event[TicTacToe]]]) =
    Some(new TicTacToeState(player, board, eventQueue))

  def move(player: TicTacToePlayer, position: Int) = TicTacToeMove(player, position)

  def player(id: String, description: String, which: String) = which match {
    case "random" => new RandomTicTacToePlayer(id, description)
    case "ai" => new AITicTacToePlayer(id, description)
    case _ => new InteractiveTicTacToePlayer(id, description)
  }

  def startState() = new TicTacToeState(x, startBoard())

  def startFrom(s: TicTacToeState) = Some(startState())

  def numPositions() = boardSize * boardSize

  def introMessage(): String = "Intro message to Tic Tac Toe"

  // tttmm.C[Option[String]]
  implicit val convertPlayerId = new axle.algebra.FunctionPair[Double, Option[String]] {
    val forward = (v: Double) => v match {
      case 0D => None
      case 1D => Some("X")
      case 2D => Some("O")
      case _ => None // or throw exception
    }
    val backward = (v: Option[String]) => v match {
      case None => 0D
      case Some("X") => 1D // TODO hard-coded player id
      case Some("O") => 2D
      case Some(_) => -1D
    }
  }

  def startBoard() = tttmm.matrix[Option[String]](boardSize, boardSize, (r: Int, c: Int) => Option[String](null))(convertPlayerId)

  def players(): immutable.Set[TicTacToePlayer] = immutable.Set(x, o)

  def playerAfter(player: TicTacToePlayer): TicTacToePlayer =
    if (player == x) o else x

}
