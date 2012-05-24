package axle.game.ttt

import axle.game._

import scala.util.Random

case class AITicTacToePlayer(
  aitttPlayerId: String,
  aitttDescription: String = "my poor AI")
  extends Player[TicTacToe](aitttPlayerId, aitttDescription) {

  def chooseMove(state: State[TicTacToe], game: TicTacToe): Move[TicTacToe] = {
    // pick a move at random.  not so "I"
    val opens = state.asInstanceOf[TicTacToeState].openPositions() // TODO cast
    TicTacToeMove(this, opens(Random.nextInt(opens.length)))
  }

}
