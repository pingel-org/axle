package axle.game.poker

import axle.algebra._
import spire.math._
import spire.implicits._

case class PokerPlayerAI(id: String, description: String = "minimax")(implicit game: Poker)
  extends PokerPlayer() {

  val heuristic: PokerState => Map[PokerPlayer, Real] = (state: PokerState) => game.players.map(p => {
    (p, state.outcome.map(out => if (out.winner === p) Real(1) else Real(-1)).getOrElse(Real(0)))
  }).toMap

  def move(state: PokerState): (PokerMove, PokerState) = {
    val (move, newState, values) = game.minimax(state, 3, heuristic)
    (move, newState)
  }
}
