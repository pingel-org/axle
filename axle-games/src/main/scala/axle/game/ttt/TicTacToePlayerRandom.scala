package axle.game.ttt

// import axle.game._
import util.Random.nextInt

object RandomTicTacToePlayer {

  def move(
    state: TicTacToeState,
    game: TicTacToe): TicTacToeMove = {
    val opens = state.moves(game).toList
    opens(nextInt(opens.length))
  }
}
