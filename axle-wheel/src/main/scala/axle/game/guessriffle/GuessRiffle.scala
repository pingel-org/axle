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

  val dealerMoveVariable = Variable[GuessRiffleMove]("dealer move")

  val dealerStrategy: (GuessRiffle, GuessRiffleState) => ConditionalProbabilityTable[GuessRiffleMove, Rational] =
    (game: GuessRiffle, state: GuessRiffleState) => {
      if ( state.remaining.isEmpty ) {
        ConditionalProbabilityTable[GuessRiffleMove, Rational](Map(Riffle() -> Rational(1)), dealerMoveVariable)
      } else {
        assert(! state.guess.isEmpty)
        ConditionalProbabilityTable[GuessRiffleMove, Rational](Map(RevealAndScore() -> Rational(1)), dealerMoveVariable)
      }
    }
}
