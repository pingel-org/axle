package axle.game.prisoner

import axle.dropOutput
import axle.game._
import axle.game.Strategies._
import org.specs2.mutable._

class PrisonersDilemmaSpec extends Specification {

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

  "random game" should {

    val rGame = PrisonersDilemma(
      p1, randomMove, dropOutput,
      p2, randomMove, dropOutput)

    "have an intro message" in {
      introMessage(game) must contain("Prisoner")
    }

    "produce moveStateStream" in {
      moveStateStream(rGame, startState(rGame)).take(2).length must be equalTo 2
    }

    "play" in {
      val endState = play(rGame, startState(rGame), false)
      moves(rGame, endState).length must be equalTo 0
    }

    "product game stream" in {
      val games = gameStream(rGame, startState(rGame), false).take(2)
      games.length must be equalTo 2
    }

  }

  "startFrom" should {
    "simply return the start state" in {
      val state = startState(game)
      val move = moves(game, state).head
      val nextState = applyMove(game, state, move)
      val nextMove = moves(game, state).head
      val newStart = startFrom(game, nextState).get
      moves(game, newStart).length must be equalTo 2
    }
  }

  "starting moves" should {
    "be two-fold, display to p2 with 'something'" in {

      val startingMoves = moves(game, startState(game))

      displayMoveTo(game, None, p1, p2) must contain("something")
      startingMoves.length must be equalTo 2
    }
  }

  "interactive player" should {
    "print various messages" in {

      val firstMove = Silence()
      val secondState = applyMove(game, startState(game), firstMove)

      evGameIO.parseMove(game, "foo") must be equalTo Left("foo is not a valid move.  Please select again")

      evGameIO.parseMove(game, "silence").right.flatMap(move => evGame.isValid(game, secondState, move)).isRight must be equalTo true
      evGameIO.parseMove(game, "betrayal").right.flatMap(move => evGame.isValid(game, secondState, move)).isRight must be equalTo true
    }
  }

  //  "A.I. strategy" should {
  //    "make a move" in {
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
  //      move.position must be greaterThan 0
  //    }
  //  }

  "outcomes" should {

    "dual silence > dual betrayal for both" in {

      val silenceGame = PrisonersDilemma(
        p1, hardCodedStringStrategy(silence), dropOutput,
        p2, hardCodedStringStrategy(silence), dropOutput)

      val betrayalGame = PrisonersDilemma(
        p1, hardCodedStringStrategy(betrayal), dropOutput,
        p2, hardCodedStringStrategy(betrayal), dropOutput)

      val silentOutcome = outcome(silenceGame, moveStateStream(silenceGame, start).last._3).get
      val betrayalOutcome = outcome(betrayalGame, moveStateStream(betrayalGame, start).last._3).get

      silentOutcome.p1YearsInPrison must be lessThan betrayalOutcome.p1YearsInPrison
      silentOutcome.p2YearsInPrison must be lessThan betrayalOutcome.p2YearsInPrison
    }

    "silence/betrayal inverse asymmetry" in {

      val p1silent = PrisonersDilemma(
        p1, hardCodedStringStrategy(silence), dropOutput,
        p2, hardCodedStringStrategy(betrayal), dropOutput)

      val p2silent = PrisonersDilemma(
        p1, hardCodedStringStrategy(betrayal), dropOutput,
        p2, hardCodedStringStrategy(silence), dropOutput)

      val p1silentOutcome = outcome(p1silent, moveStateStream(p1silent, start).last._3).get
      val p2silentOutcome = outcome(p2silent, moveStateStream(p2silent, start).last._3).get

      p1silentOutcome.p1YearsInPrison must be equalTo p2silentOutcome.p2YearsInPrison
      p1silentOutcome.p2YearsInPrison must be equalTo p2silentOutcome.p1YearsInPrison
    }
  }

}