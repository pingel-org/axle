package axle.game.prisoner

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import spire.random.Generator.rng

import axle.probability._
import axle.game._
import axle.game.Strategies._

class PrisonersDilemmaSpec extends AnyFunSuite with Matchers {

  import axle.game.prisoner.evGame._
  import axle.game.prisoner.evGameIO._

  implicit val rat = new spire.math.RationalAlgebra()

  val p1 = Player("P1", "Prisoner 1")
  val p2 = Player("P2", "Prisoner 2")

  val game = PrisonersDilemma(p1, p2)

  def silence(game: PrisonersDilemma, state: PrisonersDilemmaState): String =
    "silence"

  def betrayal(game: PrisonersDilemma, state: PrisonersDilemmaState): String =
    "betrayal"

  val start = startState(game)

  test("random game has an intro message") {
    introMessage(game) should include("Prisoner")
  }

  test("random game produces moveStateStream") {
    moveStateStream(game, startState(game), _ => randomMove, rng).take(2) should have length 2
  }

  test("random game plays") {
    val endState = play(
      game,
      _ => randomMove,
      _ => (s: String) => axle.IO.printLine[cats.effect.IO](s),
      startState(game),
      false,
      rng)
    moves(game, endState) should have length 0
  }

  test("random game produces game stream") {
    val games = gameStream(
      game,
      _ => randomMove,
      _ => (s: String) => axle.IO.printLine[cats.effect.IO](s),
      startState(game),
      false,
      rng).take(2)
    games should have length 2
  }

  test("startFrom return the start state") {
    val state = startState(game)
    val move = moves(game, state).head
    val nextState = applyMove(game, state, move)
    val _ = moves(game, state).head // dropping "nextMove"
    val newStart = startFrom(game, nextState).get
    moves(game, newStart) should have length 2
    outcome(game, state) should be(None)
  }

  test("masked-sate mover are the same as raw state mover") {
    val state = startState(game)
    val move = moves(game, state).head
    val nextState = applyMove(game, state, move)
    moverM(game, state) should be(mover(game, state))
    moverM(game, nextState) should be(mover(game, nextState))
  }

  test("starting moves are two-fold, display to p2 with 'something'") {

    val startingMoves = moves(game, startState(game))

    displayMoveTo(game, None, p1, p2) should include("something")
    startingMoves should have length 2
  }

  test("interactive player prints various messages") {

    val firstMove = Silence()
    val secondState = applyMove(game, startState(game), firstMove)

    evGameIO.parseMove(game, "foo") should be(Left("foo is not a valid move.  Please select again"))

    evGameIO.parseMove(game, "silence").flatMap(move => evGame.isValid(game, secondState, move)).isRight should be(true)
    evGameIO.parseMove(game, "betrayal").flatMap(move => evGame.isValid(game, secondState, move)).isRight should be(true)
  }

  //  test("A.I. strategy") {
  //
  //      val firstMove = Silence()
  //
  //      val h = (outcome: PrisonersDilemmaOutcome, p: Player) =>
  //        outcome.winner.map(wp => if (wp == p) 1d else -1d).getOrElse(0d)
  //
  //      implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  //      val ai4 = aiMover[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove, Double](
  //        4, outcomeRingHeuristic(game, h))
  //
  //      val secondState = applyMove(game, startState(game), firstMove)
  //
  //      val move = ai4(game, secondState)
  //
  //      move.position should be > 0
  //  }

  test("dual silence > dual betrayal for both") {

    val silentOutcome = outcome(game, moveStateStream(game, start, _ => hardCodedStringStrategy(silence), rng).last._3).get

    val betrayalOutcome = outcome(game, moveStateStream(game, start, _ => hardCodedStringStrategy(betrayal), rng).last._3).get

    silentOutcome.p1YearsInPrison should be < betrayalOutcome.p1YearsInPrison
    silentOutcome.p2YearsInPrison should be < betrayalOutcome.p2YearsInPrison
  }

  test("silence/betrayal inverse asymmetry") {

    val lastStateP1Silent = moveStateStream(
      game,
      start,
      player =>
        if ( player === p1 ) {
          hardCodedStringStrategy(silence)
        } else if ( player === p2 ) {
          hardCodedStringStrategy(betrayal)
        } else {
          ???
        },
      rng).last._3

    val p1silentOutcome = outcome(game, lastStateP1Silent).get

    val p2silentOutcome = outcome(game, moveStateStream(
      game,
      start,
      player =>
        if ( player === p1 ) {
          hardCodedStringStrategy(betrayal)
        } else if ( player === p2 ) {
          hardCodedStringStrategy(silence)
        } else {
          ???
        },
      rng).last._3).get

    p1silentOutcome.p1YearsInPrison should be(p2silentOutcome.p2YearsInPrison)
    p1silentOutcome.p2YearsInPrison should be(p2silentOutcome.p1YearsInPrison)
    moverM(game, lastStateP1Silent) should be(None)
  }

}
