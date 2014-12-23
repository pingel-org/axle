package axle.game.ttt

import util.Random.nextInt

case class RandomTicTacToePlayer(id: String, description: String = "random")(implicit ttt: TicTacToe)
  extends TicTacToePlayer() {

  def move(state: TicTacToeState): (TicTacToeMove, TicTacToeState) = {
    val opens = state.moves.toList
    val move = opens(nextInt(opens.length))
    (move, state(move).get) // TODO: .get
  }
}
