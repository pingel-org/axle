package axle.game.poker

import spire.implicits.eqOps
import spire.math.Real
import axle.game._

case class PokerPlayerAI(id: String, description: String = "minimax")
    extends PokerPlayer() {

  def heuristic(game: Poker): PokerState => Map[PokerPlayer, Real] = (state: PokerState) => game.players.map(p => {
    (p, state.outcome(game).map(out => if (out.winner.get === p) Real(1) else Real(-1)).getOrElse(Real(0)))
  }).toMap

  def move(state: PokerState, game: Poker): (PokerMove, PokerState) = {
    val (move, newState, values) = minimax(game, state, 3, heuristic(game))
    (move, newState)
  }
}
