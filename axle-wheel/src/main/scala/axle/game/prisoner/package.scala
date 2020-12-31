package axle.game

import cats.implicits._

/**
 * Prisoner's Dilemma
 *
 * https://en.wikipedia.org/wiki/Prisoner%27s_dilemma
 */

package object prisoner {

  implicit val eqMove = cats.kernel.Eq.fromUniversalEquals[PrisonersDilemmaMove]

  implicit val evGame: Game[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove, PrisonersDilemmaState, Option[PrisonersDilemmaMove]] =
    new Game[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove, PrisonersDilemmaState, Option[PrisonersDilemmaMove]] {

      def startState(game: PrisonersDilemma): PrisonersDilemmaState =
        PrisonersDilemmaState(None, false, None)

      // TODO iterated PD will provide the move/outcome history
      def startFrom(game: PrisonersDilemma, s: PrisonersDilemmaState): Option[PrisonersDilemmaState] =
        Some(startState(game))

      def players(game: PrisonersDilemma): IndexedSeq[Player] =
        Vector(game.p1, game.p2)

      def isValid(
        g:     PrisonersDilemma,
        state: PrisonersDilemmaState,
        move:  PrisonersDilemmaMove): Either[String, PrisonersDilemmaMove] =
        Right(move)

      def applyMove(
        game:  PrisonersDilemma,
        state: PrisonersDilemmaState,
        move:  PrisonersDilemmaMove): PrisonersDilemmaState = {
          import scala.annotation.nowarn
          mover(game, state).map( mover =>
            mover match {
              case game.p1 => state.copy(p1Move = Some(move), p1Moved = true)
              case _       => state.copy(p2Move = Some(move))
            }
          ).right.get : @nowarn
        }

      def mover(
        game: PrisonersDilemma,
        state: PrisonersDilemmaState): Either[PrisonersDilemmaOutcome, Player] =
        if (!state.p1Moved) {
          assert(state.p1Move.isEmpty)
          Right(game.p1)
        } else if (state.p2Move.isEmpty) {
          Right(game.p2)
        } else {
          (state.p1Move, state.p2Move) match {
            case (Some(m1), Some(m2)) => (m1, m2) match {
              case (Silence(), Silence())   => Left(PrisonersDilemmaOutcome(1, 1))
              case (Silence(), Betrayal())  => Left(PrisonersDilemmaOutcome(3, 0))
              case (Betrayal(), Silence())  => Left(PrisonersDilemmaOutcome(0, 3))
              case (Betrayal(), Betrayal()) => Left(PrisonersDilemmaOutcome(2, 2))
              case (_, _)                   => ??? // TODO unreachable
            }
            case _ => ???
          }
        }

      def moves(
        game: PrisonersDilemma,
        state: PrisonersDilemmaState): Seq[PrisonersDilemmaMove] =
        (mover(game, state), state.p1Move, state.p2Move) match {
          case (Right(game.p1), None, _) => List(Silence(), Betrayal())
          case (Right(game.p2), _, None) => List(Silence(), Betrayal())
          case _                        => List.empty
        }

      def maskState(game: PrisonersDilemma, state: PrisonersDilemmaState, observer: Player): PrisonersDilemmaState =
        if (game.p1 === observer) {
          state.copy(p2Move = None)
        } else {
          state.copy(p1Move = None)
        }

      def maskMove(game: PrisonersDilemma, move: PrisonersDilemmaMove, mover: Player, observer: Player): Option[PrisonersDilemmaMove] =
        if (mover === observer) {
          Some(move)
        } else {
          None
        }

    }

  implicit val evGameIO: GameIO[PrisonersDilemma, PrisonersDilemmaOutcome, PrisonersDilemmaMove, PrisonersDilemmaState, Option[PrisonersDilemmaMove]] =
    new GameIO[PrisonersDilemma, PrisonersDilemmaOutcome, PrisonersDilemmaMove, PrisonersDilemmaState, Option[PrisonersDilemmaMove]] {

      def parseMove(g: PrisonersDilemma, input: String): Either[String, PrisonersDilemmaMove] =
        input match {
          case "betrayal" => Right(Betrayal())
          case "silence"  => Right(Silence())
          case _          => Left(input + " is not a valid move.  Please select again")
        }

      def introMessage(ttt: PrisonersDilemma) =
        """Prisoner's Dilemma"""

      def displayStateTo(game: PrisonersDilemma, s: PrisonersDilemmaState, observer: Player): String =
        "You have been caught"

      def displayMoveTo(
        game:     PrisonersDilemma,
        move:     Option[PrisonersDilemmaMove],
        mover:    Player,
        observer: Player): String =
        mover.referenceFor(observer) + " chose " +
          move.map(_.description).getOrElse("something")

      def displayOutcomeTo(
        game:     PrisonersDilemma,
        outcome:  PrisonersDilemmaOutcome,
        observer: Player): String =
        s"${game.p1.referenceFor(observer)} is imprisoned for ${outcome.p1YearsInPrison} years\n" +
          s"${game.p2.referenceFor(observer)} is imprisoned for ${outcome.p2YearsInPrison} years\n"

    }
}
