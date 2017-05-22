package axle.game.poker

import org.scalatest._
import axle.game._
import axle.game.Strategies._

class PokerSpec extends FunSuite with Matchers {

  import axle.game.poker.evGame._
  import axle.game.poker.evGameIO._

  val p1 = Player("P1", "Player 1")
  val p2 = Player("P2", "Player 2")

  test("start state displays something") {

    val game = Poker(Vector(
      (p1, interactiveMove, println),
      (p2, interactiveMove, println)),
      println)

    val state = startState(game)
    val ms = evGame.maskState(game, state, p1)
    displayStateTo(game, ms, p1) should include("Current bet: 0")
    outcome(game, state) should be(None)
  }

  test("masked-sate mover is the same as raw state mover") {

    val game = Poker(Vector(
      (p1, interactiveMove, println),
      (p2, interactiveMove, println)),
      println)

    val state = startState(game)
    val msp1 = maskState(game, state, p1)
    val move = moves(game, msp1).head
    val nextState = applyMove(game, state, move)
    moverM(game, maskState(game, state, p1)) should be(mover(game, state))
    moverM(game, maskState(game, state, p2)) should be(mover(game, state))
    moverM(game, maskState(game, nextState, p1)) should be(mover(game, nextState))
    moverM(game, maskState(game, nextState, p2)) should be(mover(game, nextState))
  }

  test("only 1 player 'still in', not allow another game to begin") {

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

    startFrom(game, state) should be(None)
  }

  test("p2 folding after river => victory for p1") {

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
      (p1, hardCodedStringStrategy(p1Move), axle.ignore),
      (p2, hardCodedStringStrategy(p2Move), axle.ignore)),
      ignore)

    val start = startState(game)
    val history = moveStateStream(game, start).toVector
    val lastState = history.last._3
    val lastStateByPlay = play(game) // TODO make use of this

    val o = outcome(game, lastState).get
    val newGameState = startFrom(game, lastState).get
    val ms = maskState(game, history.drop(1).head._1, p1)

    // TODO lastState should be equalTo lastStateByPlay
    history.map({
      case (from, move, to) => {
        displayMoveTo(game, move, mover(game, from).get, p1)
      }
    }).mkString(", ") should include("call")
    // TODO these messages should include amounts
    moves(game, ms) should contain(Fold())
    displayOutcomeTo(game, o, p1) should include("Winner: Player 1") // TODO show P1 his own hand
    displayOutcomeTo(game, o, p2) should include("Winner: Player 1")
    introMessage(game) should include("Texas")
    o.winner.get should be(p1)
    val mngs = evGame.maskState(game, newGameState, p1)
    moves(game, mngs) should have length 1 // new deal
  }

  //    val rGame: Poker = Poker(Vector(
  //      (p1, randomMove, dropOutput),
  //      (p2, randomMove, dropOutput)),
  //      dropOutput)
  //
  //    test("produce moveStateStream") {
  //      val stream = moveStateStream(rGame, startState(rGame))
  //      stream.take(3).length should be equalTo 3
  //    }
  //
  //    test("terminate in a state with no further moves") {
  //      val endState = play(rGame)
  //      endState.moves(rGame).length should be equalTo 0
  //    }
  //
  //    test("produce game stream") {
  //      val stream = gameStream(rGame, startState(rGame), false)
  //      stream.take(2).length should be equalTo 2
  //    }

}
