package axle.game.poker

import axle.game._

abstract class PokerPlayer(id: String, _description: String)(implicit game: Poker)
extends Player[Poker](id, _description) {

  def description: String = _description
}
