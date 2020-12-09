package axle.game.guessriffle

import cats.syntax.all._

import spire.math._

import axle.probability._
import axle.game._
import axle.game.cards._

case class GuessRiffle(player: Player)

object GuessRiffle {

  val dealer = Player("D", "Dealer")

  val dealerStrategy: GuessRiffleState => ConditionalProbabilityTable[GuessRiffleMove, Rational] =
    (state: GuessRiffleState) =>
      if ( state.remaining.isEmpty ) {
        ConditionalProbabilityTable[GuessRiffleMove, Rational](Map(Riffle() -> Rational(1)))
      } else {
        assert(! state.guess.isEmpty)
        ConditionalProbabilityTable[GuessRiffleMove, Rational](Map(RevealAndScore() -> Rational(1)))
      }

  val perfectOptionsPlayerStrategy: GuessRiffleState => ConditionalProbabilityTable[GuessRiffleMove, Rational] =
    (state: GuessRiffleState) => {
      // If the Game API allowed for customizing the State, we could avoid re-computing the
      // "pointers" each time
      val (topPointer, bottomPointerOpt) =
        state.history.reverse.foldLeft((state.initialDeck.cards, Option.empty[List[Card]]))({
          case ((tp, bpo), c) => {
            if(tp.head === c) {
              (tp.tail, bpo)
            } else {
              bpo.map { bp => 
                assert(bp.head === c)
                (tp, Some(bp.tail))
              }.getOrElse({
                (tp, Some(tp.dropWhile(_ =!= c).tail))
              })
            }
          }
        })
        if(bottomPointerOpt.isEmpty) {
          uniformDistribution[GuessRiffleMove](topPointer.map(GuessCard))
        } else {
          uniformDistribution[GuessRiffleMove]((List(topPointer.headOption, bottomPointerOpt.get.headOption).flatten).map(GuessCard))
        }
    }

}
