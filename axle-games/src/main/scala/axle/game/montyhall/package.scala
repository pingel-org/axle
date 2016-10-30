package axle.game

/**
 * Monty Hall Game
 *
 *
 */

package object montyhall {

  implicit val evGame: Game[MontyHall, MontyHallState, MontyHallOutcome, MontyHallMove] =
    new Game[MontyHall, MontyHallState, MontyHallOutcome, MontyHallMove] {

      def startState(game: MontyHall): MontyHallState =
        MontyHallState(None, None, None, None)

      def startFrom(game: MontyHall, s: MontyHallState): Option[MontyHallState] =
        Some(startState(game))

      def players(game: MontyHall): IndexedSeq[Player] =
        Vector(game.contestant, game.monty)

      def strategyFor(
        game: MontyHall,
        player: Player): (MontyHall, MontyHallState) => MontyHallMove =
        player match {
          case game.contestant => game.contestantStrategy
          case game.monty      => game.montyStrategy
        }

      def isValid(
        g: MontyHall,
        state: MontyHallState,
        move: MontyHallMove): Either[String, MontyHallMove] =
        Right(move) // TODO

      def applyMove(
        game: MontyHall,
        state: MontyHallState,
        move: MontyHallMove): MontyHallState =
        move match {
          case place @ PlaceCar(d) => state.copy(placement = Some(place))
          case fc @ FirstChoice(d) => state.copy(firstChoice = Some(fc))
          case reveal @ Reveal(d)  => state.copy(reveal = Some(reveal))
          case change @ Change()   => state.copy(secondChoice = Some(Left(change)))
          case stay @ Stay()       => state.copy(secondChoice = Some(Right(stay)))
        }

      def mover(
        game: MontyHall,
        s: MontyHallState): Option[Player] =
        if (s.placement.isEmpty) {
          Some(game.monty)
        } else if (s.firstChoice.isEmpty) {
          Some(game.contestant)
        } else if (s.reveal.isEmpty) {
          Some(game.monty)
        } else if (s.secondChoice.isEmpty) {
          Some(game.contestant)
        } else {
          None
        }

      def moves(
        game: MontyHall,
        s: MontyHallState): Seq[MontyHallMove] =
        if (s.placement.isEmpty) {
          (1 to 3).map(PlaceCar.apply)
        } else if (s.firstChoice.isEmpty) {
          (1 to 3).map(FirstChoice.apply)
        } else if (s.reveal.isEmpty) {
          (1 to 3).filter(d => (d != s.firstChoice.get.door && d != s.placement.get.door)).map(Reveal.apply)
        } else {
          assert(s.secondChoice.isEmpty)
          List(Change(), Stay())
        }

      def outcome(
        game: MontyHall,
        state: MontyHallState): Option[MontyHallOutcome] = {
        state match {
          case MontyHallState(Some(PlaceCar(c)), Some(FirstChoice(f)), Some(Reveal(r)), Some(sc)) =>
            sc match {
              case Left(Change()) => Some(MontyHallOutcome(c != f))
              case Right(Stay())  => Some(MontyHallOutcome(c == f))
            }
          case _ => None
        }
      }

    }

  implicit val evGameIO: GameIO[MontyHall, MontyHallState, MontyHallOutcome, MontyHallMove] =
    new GameIO[MontyHall, MontyHallState, MontyHallOutcome, MontyHallMove] {

      def displayerFor(g: MontyHall, player: Player): String => Unit =
        player match {
          case g.contestant => g.contestantDisplayer
          case g.monty      => g.montyDisplayer
        }

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

      def introMessage(ttt: MontyHall) =
        "Monty Hall Game"

      def displayStateTo(game: MontyHall, s: MontyHallState, observer: Player): String = {
        if (observer == game.contestant) {
          def mark(d: Int): String = "TODO"
          (1 to 3).map(d => s"Door #${d}: ${mark(d)}").mkString(", ")
        } else {
          def mark(d: Int): String = "TODO"
          (1 to 3).map(d => s"Door #${d}: ${mark(d)}").mkString(", ")
        }
      }

      def displayMoveTo(
        game: MontyHall,
        move: MontyHallMove,
        mover: Player,
        observer: Player): String =
        mover.referenceFor(observer) + " did " + move.description

      def displayOutcomeTo(
        game: MontyHall,
        outcome: MontyHallOutcome,
        observer: Player): String =
        game.contestant.referenceFor(observer) + (if (outcome.car) " won the car!" else " won a goat")

    }
}