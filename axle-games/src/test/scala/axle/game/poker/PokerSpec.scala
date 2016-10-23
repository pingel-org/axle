package axle.game.poker

import org.specs2.mutable._
import axle.game._

class PokerSpec extends Specification {

  val dropOutput = (s: String) => {}

  val p1 = Player("P1", "Player 1")
  val p2 = Player("P2", "Player 2")

  val game = Poker(Vector(
    (p1, interactiveMove, println),
    (p2, interactiveMove, println)),
    println)

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
      val newGameState = startFrom(game, lastState).get
      // TODO these messages should include amounts
      evOutcome.displayTo(game, outcome, p1) must contain("You beat")
      evOutcome.displayTo(game, outcome, p2) must contain("beat You")
      outcome.winner.get should be equalTo p1
      newGameState.moves(game).length must be equalTo 0 // TODO
      newGameState.setEventQueues(Map.empty).eventQueues.size must be equalTo 0
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
      val endState = play(rGame).get
      endState.moves(rGame).length must be equalTo 0
    }

    "produce game stream" in {
      val stream = gameStream(rGame, startState(rGame), false)
      stream.take(2).length must be equalTo 2
    }

  }

}
