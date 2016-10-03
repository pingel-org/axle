package axle.game.poker

import util.Random.nextInt

case class RandomPokerPlayer(id: String, description: String = "random")
  extends PokerPlayer() {

  def move(state: PokerState, game: Poker): (PokerMove, PokerState) = {
    val opens = state.moves(game)
    val move = opens(nextInt(opens.length))
    (move, state(move, game).get)
  }
}
