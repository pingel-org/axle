package axle.game.poker

import org.specs2.mutable._
import axle.game._
import spire.algebra.Eq
import spire.compat.ordering

class PokerSpec extends Specification {

  val p1 = Player("P1", "Player 1")
  val p2 = Player("P2", "Player 2")

  val game = Poker(Vector(
    (p1, PokerPlayerInteractive.move, println),
    (p2, PokerPlayerInteractive.move, println)))

  implicitly[Game[Poker, PokerState, PokerOutcome, PokerMove]]

  import game.dealer

  "start state" should {
    "display something" in {
      startState(game).displayTo(p1, game) must contain("Current bet: 0")
    }
  }

  "deal, flop, bet(p1,1), raise(p2,1), call, turn, call, call, river, call, fold, payout" should {
    "be a victory for p1" in {
      val moves: List[PokerMove] = List(
        // small and big blinds are built in
        Deal(dealer), Call(p1), Call(p2),
        Flop(dealer), Raise(p1, 1), Raise(p2, 1), Call(p1),
        Turn(dealer), Call(p1), Call(p2),
        River(dealer), Call(p1), Fold(p2),
        Payout(dealer))
      val (_, lastState) = scriptToLastMoveState(game, moves)
      val outcome = lastState.outcome(game).get
      // TODO these messages should include amounts
      evOutcome.displayTo(game, outcome, p1) must contain("You have beaten")
      evOutcome.displayTo(game, outcome, p2) must contain("beat you")
      outcome.winner.get should be equalTo p1
    }
  }

  "poker hand ranking" should {

    "work" in {

      val shared = PokerHand.fromString("J♡,T♠,6♡,6♢,8♡")
      val personals = Vector("J♠,4♠", "A♠,T♢", "K♠,Q♢").map(PokerHand.fromString)

      val hands = personals map { personal =>
        (personal.cards ++ shared.cards).combinations(5).map(PokerHand(_)).max
      }

      val jacksAndSixes = PokerHand.fromString("6♡,6♢,T♠,J♠,J♡")

      true must be equalTo Eq[PokerHand].eqv(hands.max, jacksAndSixes)
    }
  }

  "poker hand comparison" should {

    "work for 2 pair" in {
      PokerHand.fromString("6♡,6♢,T♠,T♡,A♡") must be lessThan PokerHand.fromString("6♡,6♢,T♠,J♠,J♡")
    }

    "work for pair" in {
      PokerHand.fromString("6♡,6♢,8♠,9♡,K♡") must be lessThan PokerHand.fromString("K♡,K♢,2♠,3♠,5♡")
    }

    "work for three-of-a-kind" in {
      PokerHand.fromString("6♡,6♢,6♠,Q♡,K♡") must be lessThan PokerHand.fromString("7♡,7♢,7♠,3♠,4♡")
    }

    "work for four-of-a-kind" in {
      PokerHand.fromString("6♡,6♢,6♠,6♣,Q♡") must be lessThan PokerHand.fromString("7♡,7♢,7♠,7♣,2♡")
    }

  }

}
