package axle.game.poker

import spire.implicits._

case class PokerPlayerDealer(id: String, description: String = "dealer")
  extends PokerPlayer() {

  def move(state: PokerState, game: Poker): (PokerMove, PokerState) = {
    val move = state.numShown match {
      case 0 =>
        if (state.inFors.size === 0) {
          Deal(game.dealer)
        } else {
          Flop(game.dealer)
        }
      case 3 => Turn(game.dealer)
      case 4 => River(game.dealer)
      case 5 => Payout(game.dealer)
    }
    (move, state(move, game).get) // TODO .get
  }
}
