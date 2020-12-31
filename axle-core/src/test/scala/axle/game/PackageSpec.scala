package axle.game

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import cats.kernel.Eq

class PackageSpec extends AnyFunSuite with Matchers {

  val player = Player("P", "player")

  case class TestGame()

  val game = TestGame()

  case class TestGameState(name: String)
  val s0 = TestGameState("s0")
  val s1 = TestGameState("s1")

  implicit val eqS: Eq[TestGameState] = Eq.fromUniversalEquals[TestGameState]
  implicit val eqM: Eq[TestGameMove] = Eq.fromUniversalEquals[TestGameMove]

  case class TestGameMove(name: String)
  val m0 = TestGameMove("m0")
  val m1 = TestGameMove("m1")

  case class TestGameOutcome()

  implicit val evGame: Game[TestGame,TestGameState,TestGameOutcome,TestGameMove,TestGameState,TestGameMove] =
    new Game[TestGame,TestGameState,TestGameOutcome,TestGameMove,TestGameState,TestGameMove] {

      def startState(game: TestGame): TestGameState = s0

      def startFrom(game: TestGame, state: TestGameState): Option[TestGameState] =
        Some(startState(game))

      def players(game: TestGame): IndexedSeq[Player] =
        Vector(player)

      def mover(game: TestGame, state: TestGameState): Either[TestGameOutcome, Player] =
        Right(player)

      def moves(game: TestGame, state: TestGameState): Seq[TestGameMove] =
        Vector(m0, m1)

      def maskState(game: TestGame, state: TestGameState, observer: Player): TestGameState =
        state

      def maskMove(game: TestGame, move: TestGameMove, mover: Player, observer: Player): TestGameMove =
        move

      def isValid(game: TestGame, state: TestGameState, move: TestGameMove): Either[String, TestGameMove] =
        Right(move)

      def applyMove(game: TestGame, state: TestGameState, move: TestGameMove): TestGameState =
        if( move === m0) s0 else state

    }

  implicit val evGameIO: GameIO[TestGame,TestGameOutcome,TestGameMove,TestGameState,TestGameMove] =
    new GameIO[TestGame,TestGameOutcome,TestGameMove,TestGameState,TestGameMove] {

      def parseMove(g: TestGame, input: String): Either[String, TestGameMove] =
        if(input == "0") {
          Right(m0)
        } else if (input == "1" ) {
          Right(m1)
        } else {
          Left("invalid")
        }

      def introMessage(g: TestGame) =
        "Test Game"

      def displayStateTo(game: TestGame, s: TestGameState, observer: Player): String = 
        s.name

      def displayMoveTo(
        game:     TestGame,
        move:     TestGameMove,
        mover:    Player,
        observer: Player): String =
        move.name

      def displayOutcomeTo(
        game:     TestGame,
        outcome:  TestGameOutcome,
        observer: Player): String =
        outcome.toString

    }

  test("userInput") {

    import cats.effect.IO

    val reader: () => IO[String] = () => IO { "1" }
    val writer: String => IO[Unit] = s => IO { () }

    val actual = userInput[TestGame,TestGameState,TestGameOutcome,TestGameMove,TestGameState,TestGameMove,IO](
      game,
      s0,
      reader,
      writer).unsafeRunSync()
      
    val expected: TestGameMove = m1

    actual should be(expected)
  }

}
