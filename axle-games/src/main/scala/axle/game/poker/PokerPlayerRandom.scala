package axle.game.poker

import util.Random.nextInt
import axle.game._

object RandomPokerPlayer {

  def move(state: PokerState, game: Poker, evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): (PokerMove, PokerState) = {
    val opens = state.moves(game)
    val m = opens(nextInt(opens.length))
    (m, state(m, game).get)
  }
}
