package axle.game.poker

import org.specs2.mutable._
import axle.game._

class PokerSpec extends Specification {

  val p1 = Player("P1", "Player 1")
  val p2 = Player("P2", "Player 2")

  val game = Poker(Vector(
    (p1, interactiveMove, println),
    (p2, interactiveMove, println)),
    println)

  "start state" should {
    "display something" in {
      startState(game).displayTo(p1, game) must contain("Current bet: 0")
    }
  }

  "deal, flop, bet(p1,1), raise(p2,1), call, turn, call, call, river, call, fold, payout" should {
    "be a victory for p1" in {

      // small and big blinds are built in

      def p1Move(state: PokerState, game: Poker): String =
        (state.numShown, state.currentBet) match {
          case (0, _) => "call"
          case (3, 2) => "raise 1"
          case (3, _) => "call"
          case (4, _) => "call"
          case (5, _) => "call"
        }

      def p2Move(state: PokerState, game: Poker): String =
        (state.numShown, state.currentBet) match {
          case (0, _) => "call"
          case (3, 2) => "raise 1"
          case (3, _) => "call"
          case (4, _) => "call"
          case (5, _) => "fold"
        }

      val game = Poker(Vector(
        (p1, hardCodedStrategy(p1Move), dropOutput),
        (p2, hardCodedStrategy(p2Move), dropOutput)),
        dropOutput)

      val start = startState(game)
      val lastState = moveStateStream(game, start).last._3

      val outcome = lastState.outcome(game).get
      val newGameState = startFrom(game, lastState).get
      // TODO these messages should include amounts
      evOutcome.displayTo(game, outcome, p1) must contain("You beat")
      evOutcome.displayTo(game, outcome, p2) must contain("beat You")
      outcome.winner.get should be equalTo p1
      newGameState.moves(game).length must be equalTo 0 // TODO
    }
  }

  "random game" should {

    val rGame: Poker = Poker(Vector(
      (p1, randomMove, dropOutput),
      (p2, randomMove, dropOutput)),
      dropOutput)

    "produce moveStateStream" in {
      val stream = moveStateStream(rGame, startState(rGame))
      stream.take(3).length must be equalTo 3
    }

    "terminate in a state with no further moves" in {
      val endState = play(rGame)
      endState.moves(rGame).length must be equalTo 0
    }

    "produce game stream" in {
      val stream = gameStream(rGame, startState(rGame), false)
      stream.take(2).length must be equalTo 2
    }

  }

}
