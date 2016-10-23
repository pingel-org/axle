
package axle.game.ttt

import axle.game._
import spire.implicits._

/**
 * TicTacToe is a 2-player perfect information zero-sum game
 */

case class TicTacToe(
    boardSize: Int = 3,
    x: Player,
    xStrategy: (TicTacToeState, TicTacToe) => TicTacToeMove,
    xDisplayer: String => Unit,
    o: Player,
    oStrategy: (TicTacToeState, TicTacToe) => TicTacToeMove,
    oDisplayer: String => Unit) {

  val players = Vector(x, o)

  val playerToDisplayer = Map(x -> xDisplayer, o -> oDisplayer)

  val playerToStrategy = Map(x -> xStrategy, o -> oStrategy)

  def numPositions: Int = boardSize * boardSize

  def startBoard: Array[Option[Player]] =
    (0 until (boardSize * boardSize)).map(i => None).toArray

  def playerAfter(player: Player): Player =
    if (player === x) o else x

  def markFor(player: Player): Char =
    if (player === x) 'X' else 'O'

  def state(
    player: Player,
    board: Array[Option[Player]]): TicTacToeState =
    TicTacToeState(player, board, boardSize)

}

object TicTacToe {

  implicit val game: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] =
    new Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] {

      def introMessage(ttt: TicTacToe) = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(ttt.numPositions)

      def startState(ttt: TicTacToe): TicTacToeState =
        TicTacToeState(ttt.x, ttt.startBoard, ttt.boardSize)

      def startFrom(ttt: TicTacToe, s: TicTacToeState): Option[TicTacToeState] =
        Some(startState(ttt))

      def players(g: TicTacToe): IndexedSeq[Player] =
        g.players

      def strategyFor(g: TicTacToe, player: Player): (TicTacToeState, TicTacToe) => TicTacToeMove =
        g.playerToStrategy(player)

      def displayerFor(g: TicTacToe, player: Player): String => Unit =
        g.playerToDisplayer(player)

      def parseMove(g: TicTacToe, input: String, mover: Player): Either[String, TicTacToeMove] = {
        val eitherI: Either[String, Int] = try {
          val position = input.toInt
          if (position >= 1 && position <= g.numPositions) {
            Right(position)
          } else {
            Left("Please enter a number between 1 and " + g.numPositions)
          }
        } catch {
          case e: Exception => {
            Left(input + " is not a valid move.  Please select again")
          }
        }
        eitherI.right.map { position =>
          TicTacToeMove(mover, position, g.boardSize)
        }
      }

      def isValid(g: TicTacToe, state: TicTacToeState, move: TicTacToeMove): Either[String, TicTacToeMove] =
        if (state(move.position).isEmpty) {
          Right(move)
        } else {
          Left("That space is occupied.")
        }

    }
}
