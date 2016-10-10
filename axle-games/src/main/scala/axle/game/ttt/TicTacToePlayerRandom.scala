package axle.game.ttt

import axle.game._
import util.Random.nextInt

object RandomTicTacToePlayer {

  def move(state: TicTacToeState, game: TicTacToe, evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]): (TicTacToeMove, TicTacToeState) = {
    val opens = state.moves(game).toList
    val move = opens(nextInt(opens.length))
    (move, state(move, game).get) // TODO: .get
  }
}
