package axle.game.prisoner

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import spire.math.Rational
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

  val rm = randomMove(game).andThen(Option.apply _)

  def silence(game: PrisonersDilemma, state: PrisonersDilemmaState): String =
    "silence"

  def betrayal(game: PrisonersDilemma, state: PrisonersDilemmaState): String =
    "betrayal"

  val start = startState(game)

  test("random game has an intro message") {
    introMessage(game) should include("Prisoner")
  }

  test("random game produces moveStateStream") {
    val mss = moveStateStream(game, startState(game), _ => rm, rng).get
    
    mss.take(2) should have length 2
  }

  test("random game plays") {

    val endState = play(
      game,
      _ => rm,
      startState(game),
      rng).get

    moves(game, endState) should have length 0
  }

  test("random game produces game stream") {

    val games = gameStream(
      game,
      _ => rm,
      startState(game),
      i => i < 10,
      rng).get

    games should have length 10
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

    val silentOutcome = outcome(
      game,
      moveStateStream(
        game,
        start,
        _ => hardCodedStringStrategy[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove, PrisonersDilemmaState, Option[PrisonersDilemmaMove], Rational, ConditionalProbabilityTable](game)(silence).andThen(Option.apply),
        rng
      ).get.last._3).get

    val betrayalOutcome = outcome(
      game,
      moveStateStream(
        game,
        start,
        _ => hardCodedStringStrategy[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove, PrisonersDilemmaState, Option[PrisonersDilemmaMove], Rational, ConditionalProbabilityTable](game)(betrayal).andThen(Option.apply),
        rng
      ).get.last._3).get

    silentOutcome.p1YearsInPrison should be < betrayalOutcome.p1YearsInPrison
    silentOutcome.p2YearsInPrison should be < betrayalOutcome.p2YearsInPrison
  }

  test("silence/betrayal inverse asymmetry") {

    def hardCoding1(player: Player): PrisonersDilemmaState => ConditionalProbabilityTable[PrisonersDilemmaMove, Rational] =
      if ( player === p1 ) {
        hardCodedStringStrategy[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove, PrisonersDilemmaState, Option[PrisonersDilemmaMove], Rational, ConditionalProbabilityTable](game)(silence _)
      } else if ( player === p2 ) {
        hardCodedStringStrategy[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove, PrisonersDilemmaState, Option[PrisonersDilemmaMove], Rational, ConditionalProbabilityTable](game)(betrayal _)
      } else {
        ???
      }

    val lastStateP1Silent = moveStateStream(
      game,
      start,
      p => hardCoding1(p).andThen(Option.apply _),
      rng).get.last._3

    val p1silentOutcome = outcome(game, lastStateP1Silent).get

    def hardCoding2(player: Player): PrisonersDilemmaState => ConditionalProbabilityTable[PrisonersDilemmaMove, Rational] =
      if ( player === p1 ) {
        hardCodedStringStrategy[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove, PrisonersDilemmaState, Option[PrisonersDilemmaMove], Rational, ConditionalProbabilityTable](game)(betrayal _)
      } else if ( player === p2 ) {
        hardCodedStringStrategy[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove, PrisonersDilemmaState, Option[PrisonersDilemmaMove], Rational, ConditionalProbabilityTable](game)(silence _)
      } else {
        ???
      }

    val p2silentOutcome = outcome(game, moveStateStream(
      game,
      start,
      p => hardCoding2(p).andThen(Option.apply _),
      rng).get.last._3).get

    p1silentOutcome.p1YearsInPrison should be(p2silentOutcome.p2YearsInPrison)
    p1silentOutcome.p2YearsInPrison should be(p2silentOutcome.p1YearsInPrison)
    mover(game, lastStateP1Silent) should be(None)
  }

}
