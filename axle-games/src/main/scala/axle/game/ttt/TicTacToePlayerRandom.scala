package axle.game.ttt

import util.Random.nextInt

case class RandomTicTacToePlayer(id: String, description: String = "random")
  extends TicTacToePlayer() {

  def move(state: TicTacToeState, game: TicTacToe): (TicTacToeMove, TicTacToeState) = {
    val opens = state.moves(game).toList
    val move = opens(nextInt(opens.length))
    (move, state(move, game).get) // TODO: .get
  }
}
