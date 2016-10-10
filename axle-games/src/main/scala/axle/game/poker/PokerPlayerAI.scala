package axle.game.poker

import spire.implicits.eqOps
import spire.math.Real
import axle.game.minimax
import axle.game.Game
import axle.game.Player
import axle.game.State

object PokerPlayerAI {

  def heuristic(game: Poker, state: PokerState): PokerState => Map[Player, Real] =
    (state: PokerState) => game.players.map(p => {
      (p, state.outcome(game).map(out => if (out.winner.get === p) Real(1) else Real(-1)).getOrElse(Real(0)))
    }).toMap

  def move(state: PokerState,
           game: Poker)(
             implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove],
             evState: State[Poker, PokerState, PokerOutcome, PokerMove]): (PokerMove, PokerState) = {
    val (move, newState, values) = minimax(game, state, 3, heuristic(game, state))
    (move, newState)
  }
}
