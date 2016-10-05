package axle.game.ttt

import spire.math._
import axle.game._

case class AITicTacToePlayer(id: String, description: String = "minimax", lookahead: Int = 3)
    extends TicTacToePlayer() {

  def heuristic(ttt: TicTacToe) = (state: TicTacToeState) => ttt.players.map(p => {
    (p, state.outcome(ttt).map(out => if (out.winner == Some(p)) Real(1) else Real(-1)).getOrElse(Real(0)))
  }).toMap

  def move(state: TicTacToeState, ttt: TicTacToe): (TicTacToeMove, TicTacToeState) = {
    val (move, newState, values) = minimax(ttt, state, lookahead, heuristic(ttt))
    (move, newState)
  }
}
