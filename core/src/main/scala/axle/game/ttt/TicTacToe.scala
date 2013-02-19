
package axle.game.ttt

import axle.game._
import axle.matrix._
import axle.algebra.FunctionPair
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

  val playersSeq = Vector(x, o)

  def state(player: TicTacToePlayer, board: Matrix[Option[TicTacToePlayer]], eventQueue: immutable.Map[TicTacToePlayer, List[Event[TicTacToe]]]) =
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
  implicit val convertPlayerId = new FunctionPair[Double, Option[TicTacToePlayer]] {
    val forward = (v: Double) => v match {
      case -1D => None
      case _ => Some(playersSeq(v.toInt))
    }
    val backward = (v: Option[TicTacToePlayer]) => v match {
      case None => -1D
      case Some(player) => playersSeq.indexOf(player).toDouble
    }
  }

  def startBoard() = tttmm.matrix[Option[TicTacToePlayer]](boardSize, boardSize, (r: Int, c: Int) => Option[TicTacToePlayer](null))(convertPlayerId)

  def players(): immutable.Set[TicTacToePlayer] = immutable.Set(x, o)

  def playerAfter(player: TicTacToePlayer): TicTacToePlayer =
    if (player == x) o else x

}
