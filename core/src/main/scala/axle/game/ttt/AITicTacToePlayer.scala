package axle.game.ttt

import axle.game._

import scala.util.Random

case class AITicTacToePlayer(
  game: TicTacToe,
  playerId: String,
  description: String = "my poor AI")
  extends TicTacToePlayer(game, playerId, description) {

  def chooseMove(): TicTacToeMove = {
    // pick a move at random.  not so "I"
    val opens = game.state.openPositions()
    TicTacToeMove(game, this, opens(Random.nextInt(opens.length)))
  }

}
