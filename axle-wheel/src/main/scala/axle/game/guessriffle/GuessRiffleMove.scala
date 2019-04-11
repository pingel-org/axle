package axle.game.guessriffle

import cats.kernel.Eq
import cats.syntax.all._
import axle.game.cards._

trait GuessRiffleMove

object GuessRiffleMove {

  implicit val eqGuessRiffleMove: Eq[GuessRiffleMove] =
    (a: GuessRiffleMove, b: GuessRiffleMove) => (a, b) match {
        case (GuessCard(cardA), GuessCard(cardB)) => { cardA === cardB }
        case (Riffle(), Riffle()) => true
        case (RevealAndScore(), RevealAndScore()) => true
        case _ => false
    }
}

// Player move

case class GuessCard(card: Card) extends GuessRiffleMove

// Two Dealer moves

case class Riffle() extends GuessRiffleMove

case class RevealAndScore() extends GuessRiffleMove
