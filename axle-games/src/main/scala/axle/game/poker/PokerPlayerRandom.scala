package axle.game.poker

import util.Random.nextInt
import axle.game._

object RandomPokerPlayer {

  def move(state: PokerState, game: Poker, evGame: Game[Poker, PokerState, PokerOutcome, PokerMove]): PokerMove = {
    val opens = state.moves(game)
    opens(nextInt(opens.length))
  }
}
