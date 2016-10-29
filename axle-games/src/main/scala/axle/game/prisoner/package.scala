package axle.game

/**
 * Prisoner's Dilemma
 *
 * https://en.wikipedia.org/wiki/Prisoner%27s_dilemma
 */

package object prisoner {

  implicit val evGame: Game[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove] =
    new Game[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove] {

      def startState(game: PrisonersDilemma): PrisonersDilemmaState =
        PrisonersDilemmaState(None, None)

      // TODO iterated PD will provide the move/outcome history
      def startFrom(game: PrisonersDilemma, s: PrisonersDilemmaState): Option[PrisonersDilemmaState] =
        Some(startState(game))

      def players(game: PrisonersDilemma): IndexedSeq[Player] =
        Vector(game.p1, game.p2)

      def strategyFor(
        game: PrisonersDilemma,
        player: Player): (PrisonersDilemma, PrisonersDilemmaState) => PrisonersDilemmaMove =
        player match {
          case game.p1 => game.p1Strategy
          case game.p2 => game.p2Strategy
        }

      def isValid(
        g: PrisonersDilemma,
        state: PrisonersDilemmaState,
        move: PrisonersDilemmaMove): Either[String, PrisonersDilemmaMove] =
        Right(move)

      def applyMove(
        game: PrisonersDilemma,
        state: PrisonersDilemmaState,
        move: PrisonersDilemmaMove): PrisonersDilemmaState =
        mover(game, state).get match {
          case game.p1 => PrisonersDilemmaState(Some(move), state.p2Move)
          case _       => PrisonersDilemmaState(state.p1Move, Some(move))
        }

      def mover(
        game: PrisonersDilemma,
        s: PrisonersDilemmaState): Option[Player] =
        if (s.p1Move.isEmpty) {
          Some(game.p1)
        } else if (s.p2Move.isEmpty) {
          Some(game.p2)
        } else {
          None
        }

      def moves(
        game: PrisonersDilemma,
        s: PrisonersDilemmaState): Seq[PrisonersDilemmaMove] =
        mover(game, s) match {
          case game.p1 => s.p1Move match {
            case Some(_) => List.empty
            case None    => List(Silence(), Betrayal())
          }
          case _ => s.p2Move match {
            case Some(_) => List.empty
            case None    => List(Silence(), Betrayal())
          }
        }

      def outcome(
        game: PrisonersDilemma,
        state: PrisonersDilemmaState): Option[PrisonersDilemmaOutcome] = {
        (state.p1Move, state.p2Move) match {
          case (Some(m1), Some(m2)) => (m1, m2) match {
            case (Silence(), Silence())   => Some(PrisonersDilemmaOutcome(1, 1))
            case (Silence(), Betrayal())  => Some(PrisonersDilemmaOutcome(3, 0))
            case (Betrayal(), Silence())  => Some(PrisonersDilemmaOutcome(0, 3))
            case (Betrayal(), Betrayal()) => Some(PrisonersDilemmaOutcome(2, 2))
          }
          case _ => None
        }
      }

    }

  implicit val evGameIO: GameIO[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove] =
    new GameIO[PrisonersDilemma, PrisonersDilemmaState, PrisonersDilemmaOutcome, PrisonersDilemmaMove] {

      def displayerFor(g: PrisonersDilemma, player: Player): String => Unit =
        player match {
          case g.p1 => g.p1Displayer
          case g.p2 => g.p2Displayer
        }

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
        game: PrisonersDilemma,
        move: PrisonersDilemmaMove,
        mover: Player,
        observer: Player): String =
        mover.referenceFor(observer) + " chose " + {
          if (mover == observer) {
            move.description
          } else {
            "something"
          }
        }

      def displayOutcomeTo(
        game: PrisonersDilemma,
        outcome: PrisonersDilemmaOutcome,
        observer: Player): String =
        s"${game.p1.referenceFor(observer)} is imprisoned for ${outcome.p1YearsInPrison} years\n" +
          s"${game.p2.referenceFor(observer)} is imprisoned for ${outcome.p2YearsInPrison} years\n"

    }
}