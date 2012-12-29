package axle.game.poker

import axle.game._
import Stream.{cons, empty}
import collection._
import util.Random.nextInt

abstract class PokerPlayer(id: String, _description: String)(implicit game: Poker)
extends Player[Poker](id, _description) {

  def description() = _description
}
