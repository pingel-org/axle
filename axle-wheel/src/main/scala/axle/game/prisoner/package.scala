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
        move:  PrisonersDilemmaMove): PrisonersDilemmaState =
        mover(game, state).get match {
          case game.p1 => state.copy(p1Move = Some(move), p1Moved = true)
          case _       => state.copy(p2Move = Some(move))
        }

      def mover(
        game: PrisonersDilemma,
        s:    PrisonersDilemmaState): Option[Player] =
        if (!s.p1Moved) {
          assert(s.p1Move.isEmpty)
          Some(game.p1)
        } else if (s.p2Move.isEmpty) {
          Some(game.p2)
        } else {
          None
        }

      def moverM(
        game: PrisonersDilemma,
        s:    PrisonersDilemmaState): Option[Player] =
        if (!s.p1Moved) {
          Some(game.p1)
        } else if (s.p2Move.isEmpty) {
          Some(game.p2)
        } else {
          None
        }

      def moves(
        game: PrisonersDilemma,
        s:    PrisonersDilemmaState): Seq[PrisonersDilemmaMove] =
        (mover(game, s), s.p1Move, s.p2Move) match {
          case (Some(game.p1), None, _) => List(Silence(), Betrayal())
          case (Some(game.p2), _, None) => List(Silence(), Betrayal())
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

      def outcome(
        game:  PrisonersDilemma,
        state: PrisonersDilemmaState): Option[PrisonersDilemmaOutcome] = {
        (state.p1Move, state.p2Move) match {
          case (Some(m1), Some(m2)) => (m1, m2) match {
            case (Silence(), Silence())   => Some(PrisonersDilemmaOutcome(1, 1))
            case (Silence(), Betrayal())  => Some(PrisonersDilemmaOutcome(3, 0))
            case (Betrayal(), Silence())  => Some(PrisonersDilemmaOutcome(0, 3))
            case (Betrayal(), Betrayal()) => Some(PrisonersDilemmaOutcome(2, 2))
            case (_, _)                   => None // TODO unreachable
          }
          case _ => None
        }
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
