package axle.game.ttt

import spire.math._
import axle.game._

object AITicTacToePlayer {

  def heuristic(ttt: TicTacToe) = (state: TicTacToeState) => ttt.players.map(p => {
    (p, state.outcome(ttt).map(out => if (out.winner == Some(p)) Real(1) else Real(-1)).getOrElse(Real(0)))
  }).toMap

  def mover(lookahead: Int)(
    implicit evGame: Game[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove],
    evState: State[TicTacToe, TicTacToeState, TicTacToeOutcome, TicTacToeMove]) =
    (state: TicTacToeState, ttt: TicTacToe) => {
      val (move, newState, values) = minimax(ttt, state, lookahead, heuristic(ttt))
      (move, newState)
    }
}
