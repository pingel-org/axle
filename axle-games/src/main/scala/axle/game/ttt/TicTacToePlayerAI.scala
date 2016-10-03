package axle.game.ttt

import spire.math._

case class AITicTacToePlayer(id: String, description: String = "minimax")
  extends TicTacToePlayer() {

  def heuristic(ttt: TicTacToe) = (state: TicTacToeState) => ttt.players.map(p => {
    (p, state.outcome(ttt).map(out => if (out.winner == Some(p)) Real(1) else Real(-1)).getOrElse(Real(0)))
  }).toMap

  def move(state: TicTacToeState, ttt: TicTacToe): (TicTacToeMove, TicTacToeState) = {
    val (move, newState, values) = ttt.minimax(state, 3, heuristic(ttt))
    (move, newState)
  }
}
