
package axle.game.ttt

import axle.game._
import scala.util.Random
import axle.matrix.ArrayMatrixFactory._

/**
 * TicTacToe is a 2-player perfect information zero-sum game
 */

case class TicTacToe(
  boardSize: Int = 3,
  x: Player[TicTacToe] = InteractiveTicTacToePlayer("X"),
  o: Player[TicTacToe] = AITicTacToePlayer("O"))
  extends Game {

  def numPositions() = boardSize * boardSize

  def startBoard() = matrix[Option[String]](boardSize, boardSize, None)

  val playas = Map[String, Player[G]]("X" -> x, "O" -> o)

  def players() = playas

  def playerAfter(player: Player[TicTacToe]): Player[TicTacToe] = {

    // In more complex games, this would be a function of the move or state as well
    // This method might evolve up into the superclass.
    // There's an unchecked assertion in this class that there are exactly 2 players.
    // I'll leave this very crude implementation here for now, since this is beyond
    // the scope of what this needs to do for Tic Tac Toe.

    // find someone who isn't 'player'

    for (other <- players.values) {
      if (other != player) {
        return other
      }
    }
    null
  }

}
        

