package axle.game.poker

import util.Random.nextInt

class RandomPokerPlayer(id: String, description: String = "random")(implicit game: Poker)
  extends PokerPlayer(id, description) {

  def move(state: PokerState): (PokerMove, PokerState) = {
    val opens = state.moves
    val move = opens(nextInt(opens.length))
    (move, state(move).get)
  }
}
