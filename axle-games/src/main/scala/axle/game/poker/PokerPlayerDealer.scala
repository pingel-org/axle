package axle.game.poker

import spire.implicits._

case class PokerPlayerDealer(id: String, description: String = "dealer")(implicit game: Poker)
  extends PokerPlayer() {

  def move(state: PokerState): (PokerMove, PokerState) = {
    val move = state.numShown match {
      case 0 =>
        if (state.inFors.size === 0) {
          Deal()
        } else {
          Flop()
        }
      case 3 => Turn()
      case 4 => River()
      case 5 => Payout()
    }
    (move, state(move).get) // TODO .get
  }
}
