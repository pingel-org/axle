package axle.game.ttt

import axle.game._
import util.Random.nextInt

object RandomTicTacToePlayer {

  def move(
    state: TicTacToeState,
    game: TicTacToe,
    evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]): TicTacToeMove = {
    val opens = state.moves(game).toList
    opens(nextInt(opens.length))
  }
}
