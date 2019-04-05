package axle.game.guessriffle

import spire.math._
import axle.stats._
import axle.game._

case class GuessRiffle(
  player: Player,
  strategy: (GuessRiffle, GuessRiffleState) => ConditionalProbabilityTable[GuessRiffleMove, Rational],
  displayer: String => Unit,
  dealerDisplayer: String => Unit) {

  val dealer = Player("D", "Dealer")

  val dealerStrategy: (GuessRiffle, GuessRiffleState) => ConditionalProbabilityTable[GuessRiffleMove, Rational] = 42
}
