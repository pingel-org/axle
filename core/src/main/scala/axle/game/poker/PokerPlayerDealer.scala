package axle.game.poker

class PokerPlayerDealer(id: String, description: String = "dealer")(implicit game: Poker)
  extends PokerPlayer(id, description) {

  def move(state: PokerState): (PokerMove, PokerState) = {
    val move = state.numShown match {
      case 0 =>
        if (state.inFors.size == 0)
          Deal()
        else
          Flop()
      case 3 => Turn()
      case 4 => River()
    }
    (move, state(move).get) // TODO .get
  }
}
