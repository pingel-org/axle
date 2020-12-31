package axle.game

/**
 * Monty Hall Game
 *
 *
 */

import cats.implicits._

package object montyhall {

  implicit val eqMove = cats.kernel.Eq.fromUniversalEquals[MontyHallMove]

  implicit val evGame: Game[MontyHall, MontyHallState, MontyHallOutcome, MontyHallMove, MontyHallState, Option[MontyHallMove]] =
    new Game[MontyHall, MontyHallState, MontyHallOutcome, MontyHallMove, MontyHallState, Option[MontyHallMove]] {

      def startState(game: MontyHall): MontyHallState =
        MontyHallState(None, false, None, None, None)

      def startFrom(game: MontyHall, s: MontyHallState): Option[MontyHallState] =
        Some(startState(game))

      def players(game: MontyHall): IndexedSeq[Player] =
        Vector(game.contestant, game.monty)

      def isValid(
        g:     MontyHall,
        state: MontyHallState,
        move:  MontyHallMove): Either[String, MontyHallMove] =
        Right(move) // TODO

      def applyMove(
        game:  MontyHall,
        state: MontyHallState,
        move:  MontyHallMove): MontyHallState =
        move match {
          case place @ PlaceCar(d) => state.copy(placement = Some(place), placed = true)
          case fc @ FirstChoice(d) => state.copy(firstChoice = Some(fc))
          case reveal @ Reveal(d)  => state.copy(reveal = Some(reveal))
          case change @ Change()   => state.copy(secondChoice = Some(Left(change)))
          case stay @ Stay()       => state.copy(secondChoice = Some(Right(stay)))
        }

      def mover(
        game: MontyHall,
        state: MontyHallState): Either[MontyHallOutcome, Player] =
        if (state.placement.isEmpty) {
          Right(game.monty)
        } else if (state.firstChoice.isEmpty) {
          Right(game.contestant)
        } else if (state.reveal.isEmpty) {
          Right(game.monty)
        } else if (state.secondChoice.isEmpty) {
          Right(game.contestant)
        } else {
          state match {
            case MontyHallState(Some(PlaceCar(c)), true, Some(FirstChoice(f)), Some(Reveal(r)), Some(sc)) =>
              sc match {
                case Left(Change()) => Left(MontyHallOutcome(c != f))
                case Right(Stay())  => Left(MontyHallOutcome(c == f))
              }
            case _ => ???
          }
        }

      def moves(
        game: MontyHall,
        state: MontyHallState): Seq[MontyHallMove] =
        if (!state.placed) {
          (1 to 3).map(PlaceCar.apply)
        } else if (state.firstChoice.isEmpty) {
          (1 to 3).map(FirstChoice.apply)
        } else if (state.reveal.isEmpty) {
          (1 to 3).filter(d => (d != state.firstChoice.get.door && d != state.placement.get.door)).map(Reveal.apply)
        } else if (state.secondChoice.isEmpty) {
          List(Change(), Stay())
        } else {
          List.empty
        }

      def maskState(game: MontyHall, state: MontyHallState, observer: Player): MontyHallState =
        if (observer === game.monty) {
          state
        } else {
          state.copy(placement = None)
        }

      def maskMove(game: MontyHall, move: MontyHallMove, mover: Player, observer: Player): Option[MontyHallMove] =
        if (observer === game.monty) {
          Some(move)
        } else {
          move match {
            case PlaceCar(_) => None
            case _           => Some(move)
          }
        }

    }

  implicit val evGameIO: GameIO[MontyHall, MontyHallOutcome, MontyHallMove, MontyHallState, Option[MontyHallMove]] =
    new GameIO[MontyHall, MontyHallOutcome, MontyHallMove, MontyHallState, Option[MontyHallMove]] {

      def parseMove(g: MontyHall, input: String): Either[String, MontyHallMove] =
        input match {
          case "car 1"    => Right(PlaceCar(1))
          case "car 2"    => Right(PlaceCar(2))
          case "car 3"    => Right(PlaceCar(3))
          case "pick 1"   => Right(FirstChoice(1))
          case "pick 2"   => Right(FirstChoice(2))
          case "pick 3"   => Right(FirstChoice(3))
          case "reveal 1" => Right(Reveal(1))
          case "reveal 2" => Right(Reveal(2))
          case "reveal 3" => Right(Reveal(3))
          case "change"   => Right(Change())
          case "stay"     => Right(Stay())
          case _          => Left(input + " is not a valid move.  Please select again")
        }

      def introMessage(mh: MontyHall) =
        "Monty Hall Game"

      def displayStateTo(game: MontyHall, s: MontyHallState, observer: Player): String = {

        if (observer === game.contestant) {

          def mark(d: Int): String =
            s.firstChoice.map(f => if (d === f.door) "first choice" else "").getOrElse("") +
              s.reveal.map(r => if (d === r.door) ", revealed goat" else "").getOrElse("") +
              s.secondChoice.map(sc =>
                if (sc.isLeft) {
                  if (d =!= s.firstChoice.get.door && d =!= s.reveal.get.door) ", changed to" else ""
                } else {
                  if (d === s.firstChoice.get.door && d =!= s.reveal.get.door) ", stuck with" else ""
                }).getOrElse("")

          (1 to 3).map(d => s"Door #${d}: ${mark(d)}").mkString("\n")
        } else {

          def mark(d: Int): String =
            s.placement.map(c => if (d === c.door) "car" else "goat").getOrElse("???") +
              s.firstChoice.map(f => if (d === f.door) ", first choice" else "").getOrElse("") +
              s.reveal.map(r => if (d === r.door) ", revealed" else "").getOrElse("") +
              s.secondChoice.map(sc =>
                if (sc.isLeft) {
                  if (d =!= s.firstChoice.get.door && d =!= s.reveal.get.door) ", changed to" else ""
                } else {
                  if (d === s.firstChoice.get.door && d =!= s.reveal.get.door) ", stuck with" else ""
                }).getOrElse("")

          (1 to 3).map(d => s"Door #${d}: ${mark(d)}").mkString("\n")
        }
      }

      def displayMoveTo(
        game:     MontyHall,
        move:     Option[MontyHallMove],
        mover:    Player,
        observer: Player): String =
        mover.referenceFor(observer) + " did " + move.map(_.description).getOrElse("something")

      def displayOutcomeTo(
        game:     MontyHall,
        outcome:  MontyHallOutcome,
        observer: Player): String =
        game.contestant.referenceFor(observer) + (if (outcome.car) " won the car!" else " won a goat")

    }
}
