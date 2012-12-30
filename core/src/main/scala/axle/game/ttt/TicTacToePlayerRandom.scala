package axle.game.ttt

import util.Random.nextInt

class RandomTicTacToePlayer(aitttPlayerId: String, aitttDescription: String = "random")(implicit ttt: TicTacToe)
  extends TicTacToePlayer(aitttPlayerId, aitttDescription) {

  def move(state: TicTacToeState): (TicTacToeMove, TicTacToeState) = {
    val opens = state.moves.toList
    val move = opens(nextInt(opens.length))
    (move, state(move).get) // TODO: .get
  }
}
