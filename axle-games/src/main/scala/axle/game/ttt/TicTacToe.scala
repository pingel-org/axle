
package axle.game.ttt

import axle.game._
import axle.matrix._
import axle.algebra._
import util.Random.{ nextInt }
import spire.implicits._
import axle.jblas.JblasMatrixModule

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

  def state(player: TicTacToePlayer, board: Matrix[Option[TicTacToePlayer]], eventQueue: Map[TicTacToePlayer, List[Event[TicTacToe]]]): Option[TicTacToeState] =
    Some(new TicTacToeState(player, board, eventQueue))

  def move(player: TicTacToePlayer, position: Int): TicTacToeMove = TicTacToeMove(player, position)

  def player(id: String, description: String, which: String): TicTacToePlayer = which match {
    case "random" => new RandomTicTacToePlayer(id, description)
    case "ai" => new AITicTacToePlayer(id, description)
    case _ => new InteractiveTicTacToePlayer(id, description)
  }

  def startState: TicTacToeState = new TicTacToeState(x, startBoard)

  def startFrom(s: TicTacToeState): Option[TicTacToeState] = Some(startState)

  def numPositions: Int = boardSize * boardSize

  def introMessage: String = "Intro message to Tic Tac Toe"

  // tttmm.C[Option[String]]
  implicit val convertPlayerId = new FunctionPair[Double, Option[TicTacToePlayer]] {
    def apply(v: Double): Option[TicTacToePlayer] = v match {
      case -1D => None
      case _ => Some(playersSeq(v.toInt))
    }
    def unapply(v: Option[TicTacToePlayer]): Double = v match {
      case None => -1D
      case Some(player) => playersSeq.indexOf(player).toDouble
    }
  }

  def startBoard: Matrix[Option[TicTacToePlayer]] =
    tttmm.matrix[Option[TicTacToePlayer]](
        boardSize,
        boardSize,
        (r: Int, c: Int) => Option[TicTacToePlayer](null))(convertPlayerId)

  def players: Set[TicTacToePlayer] = Set(x, o)

  def playerAfter(player: TicTacToePlayer): TicTacToePlayer =
    if (player === x) o else x

}
