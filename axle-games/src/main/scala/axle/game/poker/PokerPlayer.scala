package axle.game.poker

import axle.game._
import axle.Show
import spire.algebra.Eq

object PokerPlayer {

  implicit val ppShow: Show[PokerPlayer] =
    new Show[PokerPlayer] {
      def text(p: PokerPlayer): String = p.description + " (" + p.id + ")"
    }

  implicit def ppEq: Eq[PokerPlayer] =
    new Eq[PokerPlayer] {
      def eqv(x: PokerPlayer, y: PokerPlayer): Boolean = x.equals(y)
    }

}

trait PokerPlayer extends Player[Poker]
