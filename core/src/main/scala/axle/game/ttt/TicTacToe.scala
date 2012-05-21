
package axle.game.ttt

import axle.game._
import scala.util.Random

/**
 * TicTacToe is a 2-player perfect information zero-sum game
 */

case class TicTacToe(boardSize: Int = 3, initializePlayers: Boolean = true) extends Game {

  val numPositions = boardSize * boardSize

  override var state: TicTacToeState

  if (initializePlayers) {
    val x = InteractiveTicTacToePlayer(this, "X")
    addPlayer(x)
    val o = AITicTacToePlayer(this, "O")
    addPlayer(o)
    state = TicTacToeState(this, x)
  }

  def playerAfter(player: Player): Player = {

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
        

