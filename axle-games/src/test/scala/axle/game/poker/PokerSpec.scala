package axle.game.poker

import org.specs2.mutable._
import axle.dropOutput
import axle.game._
import axle.game.Strategies._

class PokerSpec extends Specification {

  import axle.game.poker.evGame._
  import axle.game.poker.evGameIO._

  val p1 = Player("P1", "Player 1")
  val p2 = Player("P2", "Player 2")

  "start state" should {
    "display something" in {
      val game = Poker(Vector(
        (p1, interactiveMove, println),
        (p2, interactiveMove, println)),
        println)
      val ms = evGame.maskState(game, startState(game), p1)
      displayStateTo(game, ms, p1) must contain("Current bet: 0")
    }
  }

  "only 1 player 'still in'" should {
    "not allow another game to begin" in {

      val game = Poker(Vector(
        (p1, interactiveMove, println),
        (p2, interactiveMove, println)),
        println)

      val state = PokerState(
        _ => Some(p1),
        axle.game.cards.Deck(),
        Vector.empty, // shared
        0,
        Map.empty,
        0, // pot
        5, // currentBet
        Set(p1), // stillIn
        Map(p1 -> 5), // in for
        Map(p1 -> 200, p2 -> 0), // piles
        Some(PokerOutcome(Some(p1), None)))

      startFrom(game, state) must be equalTo None
    }
  }

  "p2 folding after river" should {
    "result in victory for p1" in {

      // small and big blinds are built in

      def p1Move(game: Poker, state: PokerStateMasked): String =
        (state.shownShared.length, state.currentBet) match {
          case (0, _)              => "call"
          case (3, bet) if bet < 3 => "raise 1"
          case (3, _)              => "call"
          case (4, _)              => "call"
          case (5, _)              => "call"
        }

      def p2Move(game: Poker, state: PokerStateMasked): String =
        (state.shownShared.length, state.currentBet) match {
          case (0, _) => "call"
          case (3, _) => "call"
          case (4, _) => "call"
          case (5, _) => "fold"
        }

      val game = Poker(Vector(
        (p1, hardCodedStringStrategy(p1Move), dropOutput),
        (p2, hardCodedStringStrategy(p2Move), dropOutput)),
        dropOutput)

      val start = startState(game)
      val history = moveStateStream(game, start).toVector
      val lastState = history.last._3
      val lastStateByPlay = play(game) // TODO make use of this

      val o = outcome(game, lastState).get
      val newGameState = startFrom(game, lastState).get
      val ms = maskState(game, history.drop(1).head._1, p1)

      // TODO lastState must be equalTo lastStateByPlay
      history.map({
        case (from, move, to) => {
          displayMoveTo(game, move, mover(game, from).get, p1)
        }
      }).mkString(", ") must contain("call")
      // TODO these messages should include amounts
      moves(game, ms) must contain(Fold())
      displayOutcomeTo(game, o, p1) must contain("Winner: Player 1") // TODO show P1 his own hand
      displayOutcomeTo(game, o, p2) must contain("Winner: Player 1")
      introMessage(game) must contain("Texas")
      o.winner.get should be equalTo p1
      val mngs = evGame.maskState(game, newGameState, p1)
      moves(game, mngs).length must be equalTo 1 // new deal
    }
  }

  //  "random game" should {
  //
  //    val rGame: Poker = Poker(Vector(
  //      (p1, randomMove, dropOutput),
  //      (p2, randomMove, dropOutput)),
  //      dropOutput)
  //
  //    "produce moveStateStream" in {
  //      val stream = moveStateStream(rGame, startState(rGame))
  //      stream.take(3).length must be equalTo 3
  //    }
  //
  //    "terminate in a state with no further moves" in {
  //      val endState = play(rGame)
  //      endState.moves(rGame).length must be equalTo 0
  //    }
  //
  //    "produce game stream" in {
  //      val stream = gameStream(rGame, startState(rGame), false)
  //      stream.take(2).length must be equalTo 2
  //    }
  //
  //  }

}
