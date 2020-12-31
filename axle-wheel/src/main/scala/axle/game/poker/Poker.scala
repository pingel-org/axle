package axle.game.poker

import axle.game._

case class Poker(betters: Vector[Player])(
  implicit val evGame: Game[Poker, PokerState, PokerOutcome, PokerMove, PokerStateMasked, PokerMove]) {

  implicit val rat = new spire.math.RationalAlgebra()

  val numPlayers = betters.length

  val dealer = Player("D", "Dealer")

  implicit val eqMove = cats.kernel.Eq.fromUniversalEquals[PokerMove]

  val allPlayers = dealer +: betters

}
