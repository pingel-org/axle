package axle.game.prisoner

import axle.dropOutput
import axle.game._
import axle.game.Strategies._
import org.scalatest._

class PrisonersDilemmaSpec extends FunSuite with Matchers {

  import axle.game.prisoner.evGame._
  import axle.game.prisoner.evGameIO._

  val p1 = Player("P1", "Prisoner 1")
  val p2 = Player("P2", "Prisoner 2")

  val game = PrisonersDilemma(
    p1, interactiveMove, dropOutput,
    p2, interactiveMove, dropOutput)

  def silence(game: PrisonersDilemma, state: PrisonersDilemmaState): String =
    "silence"

  def betrayal(game: PrisonersDilemma, state: PrisonersDilemmaState): String =
    "betrayal"

  val start = startState(game)

  val rGame = PrisonersDilemma(
    p1, randomMove, dropOutput,
    p2, randomMove, dropOutput)

  test("random game has an intro message") {
    introMessage(game) should contain("Prisoner")
  }

  test("random game produces moveStateStream") {
    moveStateStream(rGame, startState(rGame)).take(2) should have length 2
  }

  test("random game plays") {
    val endState = play(rGame, startState(rGame), false)
    moves(rGame, endState) should have length 0
  }

  test("random game produces game stream") {
    val games = gameStream(rGame, startState(rGame), false).take(2)
    games should have length 2
  }

  test("startFrom return the start state") {
    val state = startState(game)
    val move = moves(game, state).head
    val nextState = applyMove(game, state, move)
    val nextMove = moves(game, state).head
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

    displayMoveTo(game, None, p1, p2) should contain("something")
    startingMoves should have length 2
  }

  test("interactive player prints various messages") {

    val firstMove = Silence()
    val secondState = applyMove(game, startState(game), firstMove)

    evGameIO.parseMove(game, "foo") should be(Left("foo is not a valid move.  Please select again"))

    evGameIO.parseMove(game, "silence").right.flatMap(move => evGame.isValid(game, secondState, move)).isRight should be(true)
    evGameIO.parseMove(game, "betrayal").right.flatMap(move => evGame.isValid(game, secondState, move)).isRight should be(true)
  }

  //  test("A.I. strategy") {
  //
  //      val firstMove = Silence()
  //
  //      val h = (outcome: PrisonersDilemmaOutcome, p: Player) =>
  //        outcome.winner.map(wp => if (wp == p) 1d else -1d).getOrElse(0d)
  //
  //      import spire.implicits.DoubleAlgebra
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

    val silenceGame = PrisonersDilemma(
      p1, hardCodedStringStrategy(silence), dropOutput,
      p2, hardCodedStringStrategy(silence), dropOutput)

    val betrayalGame = PrisonersDilemma(
      p1, hardCodedStringStrategy(betrayal), dropOutput,
      p2, hardCodedStringStrategy(betrayal), dropOutput)

    val silentOutcome = outcome(silenceGame, moveStateStream(silenceGame, start).last._3).get
    val betrayalOutcome = outcome(betrayalGame, moveStateStream(betrayalGame, start).last._3).get

    silentOutcome.p1YearsInPrison should be < betrayalOutcome.p1YearsInPrison
    silentOutcome.p2YearsInPrison should be < betrayalOutcome.p2YearsInPrison
  }

  test("silence/betrayal inverse asymmetry") {

    val p1silent = PrisonersDilemma(
      p1, hardCodedStringStrategy(silence), dropOutput,
      p2, hardCodedStringStrategy(betrayal), dropOutput)

    val p2silent = PrisonersDilemma(
      p1, hardCodedStringStrategy(betrayal), dropOutput,
      p2, hardCodedStringStrategy(silence), dropOutput)

    val lastStateP1Silent = moveStateStream(p1silent, start).last._3
    val p1silentOutcome = outcome(p1silent, lastStateP1Silent).get
    val p2silentOutcome = outcome(p2silent, moveStateStream(p2silent, start).last._3).get

    p1silentOutcome.p1YearsInPrison should be(p2silentOutcome.p2YearsInPrison)
    p1silentOutcome.p2YearsInPrison should be(p2silentOutcome.p1YearsInPrison)
    moverM(game, lastStateP1Silent) should be(None)
  }

}
