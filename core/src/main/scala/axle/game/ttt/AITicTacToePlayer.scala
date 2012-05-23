package axle.game.ttt

import axle.game._

import scala.util.Random

case class AITicTacToePlayer(
  aitttPlayerId: String,
  aitttDescription: String = "my poor AI")
  extends Player[TicTacToe](aitttPlayerId, aitttDescription) {

  def chooseMove(state: TicTacToeState, game: TicTacToe): Move[TicTacToe] = {
    // pick a move at random.  not so "I"
    val opens = state.openPositions()
    TicTacToeMove(this, opens(Random.nextInt(opens.length)), game)
  }

}
