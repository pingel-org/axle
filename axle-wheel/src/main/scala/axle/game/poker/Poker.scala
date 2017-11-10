package axle.game.poker

import axle.game._
import axle.game.Strategies._
import axle.stats.ConditionalProbabilityTable0
import spire.math.Rational

case class Poker(
    playersStrategiesDisplayers: IndexedSeq[(Player, (Poker, PokerStateMasked) => ConditionalProbabilityTable0[PokerMove, Rational], String => Unit)],
    dealerDisplayer: String => Unit)(implicit evGame: Game[Poker, PokerState, PokerOutcome, PokerMove, PokerStateMasked, PokerMove]) {

  val players = playersStrategiesDisplayers.map(_._1)

  val numPlayers = players.length

  val dealer = Player("D", "Dealer")

  val allPlayers = (dealer, randomMove(evGame), dealerDisplayer) +: playersStrategiesDisplayers

  val playerToStrategy = allPlayers.map(tuple => tuple._1 -> tuple._2).toMap

  val playerToDisplayer = allPlayers.map(tuple => tuple._1 -> tuple._3).toMap

}
