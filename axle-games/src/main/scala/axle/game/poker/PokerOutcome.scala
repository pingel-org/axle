package axle.game.poker

import axle.game.Player

case class PokerOutcome(winner: Option[Player], hand: Option[PokerHand])
