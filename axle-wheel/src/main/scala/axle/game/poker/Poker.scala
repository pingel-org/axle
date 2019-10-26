package axle.game.poker

//import cats.implicits._
import spire.math.Rational

import axle.game._
import axle.game.Strategies._
import axle.stats.ConditionalProbabilityTable

case class Poker(
  playersStrategiesDisplayers: IndexedSeq[(Player, (Poker, PokerStateMasked) => ConditionalProbabilityTable[PokerMove, Rational], String => Unit)],
  dealerDisplayer:             String => Unit)(implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove, PokerStateMasked, PokerMove, Rational, ConditionalProbabilityTable]) {

  implicit val rat = new spire.math.RationalAlgebra()

  val players = playersStrategiesDisplayers.map(_._1)

  val numPlayers = players.length

  val dealer = Player("D", "Dealer")

  implicit val eqMove = cats.kernel.Eq.fromUniversalEquals[PokerMove]

  val allPlayers = (dealer, randomMove, dealerDisplayer) +: playersStrategiesDisplayers

  val playerToStrategy = allPlayers.map(tuple => tuple._1 -> tuple._2).toMap

  val playerToDisplayer = allPlayers.map(tuple => tuple._1 -> tuple._3).toMap

}
