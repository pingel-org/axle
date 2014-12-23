package axle.game.ttt

import axle.algebra._
import spire.math._
import spire.implicits._

case class AITicTacToePlayer(aitttPlayerId: String, aitttDescription: String = "minimax")(implicit ttt: TicTacToe)
  extends TicTacToePlayer(aitttPlayerId, aitttDescription) {

  val heuristic = (state: TicTacToeState) => ttt.players.map(p => {
    (p, state.outcome.map(out => if (out.winner === Some(p)) Real(1) else Real(-1)).getOrElse(Real(0)))
  }).toMap

  def move(state: TicTacToeState): (TicTacToeMove, TicTacToeState) = {
    val (move, newState, values) = ttt.minimax(state, 3, heuristic)
    (move, newState)
  }
}
