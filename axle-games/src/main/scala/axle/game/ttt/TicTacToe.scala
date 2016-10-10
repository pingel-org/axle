
package axle.game.ttt

import axle.game._
import spire.implicits._

/**
 * TicTacToe is a 2-player perfect information zero-sum game
 */

case class TicTacToe(
    boardSize: Int = 3,
    x: Player,
    o: Player) {

  val players = Vector(x, o)

  def numPositions: Int = boardSize * boardSize

  def startBoard: Array[Option[Player]] =
    (0 until (boardSize * boardSize)).map(i => None).toArray

  def playerAfter(player: Player): Player =
    if (player === x) o else x

  def state(
    player: Player,
    board: Array[Option[Player]],
    eventQueue: Map[Player, List[Either[TicTacToeOutcome, TicTacToeMove]]]): Option[TicTacToeState] =
    Some(TicTacToeState(player, board, boardSize, eventQueue))

}

object TicTacToe {

  implicit val game: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] =
    new Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove] {

      // def introMessage: String = "Intro message to Tic Tac Toe"
      def introMessage(ttt: TicTacToe) = """
Tic Tac Toe
Moves are numbers 1-%s.""".format(ttt.numPositions)

      def startState(ttt: TicTacToe): TicTacToeState =
        TicTacToeState(ttt.x, ttt.startBoard, ttt.boardSize)

      def startFrom(ttt: TicTacToe, s: TicTacToeState): Option[TicTacToeState] =
        Some(startState(ttt))

    }
}
