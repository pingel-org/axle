package axle.game

import cats.kernel.Eq

import spire.math._
import spire.random.Dist
import spire.random.Generator.rng
import axle.stats._

import org.scalatest._

class MoveFromRandomStateSpec extends FunSuite with Matchers {

  val player = Player("P", "player")

  case class TestGame()

  val game = TestGame()

  case class TestGameState(name: String)
  val Sa = TestGameState("a")
  val Sb = TestGameState("b")
  val Sc = TestGameState("c")

  implicit val eqS: Eq[TestGameState] = Eq.fromUniversalEquals[TestGameState]

  case class TestGameMove(name: String)
  val Maa = TestGameMove("aa")
  val Mab = TestGameMove("ab")
  val Mac = TestGameMove("ac")
  val Mba = TestGameMove("ba")
  val Mbb = TestGameMove("bb")
  val Mbc = TestGameMove("bc")
  val Mca = TestGameMove("ca")
  val Mcb = TestGameMove("cb")
  val Mcc = TestGameMove("cc")

  case class TestGameOutcome()

  val movesMap: Map[TestGameState, Map[TestGameMove, (TestGameState, Rational)]] = Map(
    Sa -> Map(
      (Maa, (Sa, Rational(1, 2))),
      (Mab, (Sb, Rational(1, 4))),
      (Mac, (Sc, Rational(1, 4)))),
    Sb -> Map(
      (Mba, (Sa, Rational(0))),
      (Mbb, (Sb, Rational(1))),
      (Mbc, (Sc, Rational(0)))),
    Sc -> Map(
      (Mca, (Sa, Rational(0))),
      (Mcb, (Sb, Rational(0))),
      (Mcc, (Sc, Rational(1))))
  )

  implicit val evGame: Game[TestGame,TestGameState,TestGameOutcome,TestGameMove,TestGameState,TestGameMove,Rational,ConditionalProbabilityTable] =
    new Game[TestGame,TestGameState,TestGameOutcome,TestGameMove,TestGameState,TestGameMove,Rational,ConditionalProbabilityTable] {

      def probabilityDist: Dist[Rational] =
        rationalProbabilityDist

      def startState(game: TestGame): TestGameState =
        Sa

      def startFrom(game: TestGame, state: TestGameState): Option[TestGameState] =
        Some(startState(game))

      def players(game: TestGame): IndexedSeq[Player] =
        Vector(player)

      def mover(game: TestGame, state: TestGameState): Option[Player] =
        Some(player)

      def moverM(game: TestGame, state: TestGameState): Option[Player] =
        Some(player)

      def moves(game: TestGame, state: TestGameState): Seq[TestGameMove] =
        movesMap(state).keys.toVector

      def maskState(game: TestGame, state: TestGameState, observer: Player): TestGameState =
        state

      def maskMove(game: TestGame, move: TestGameMove, mover: Player, observer: Player): TestGameMove =
        move

      def strategyFor(game: TestGame, player: Player): (TestGame, TestGameState) => ConditionalProbabilityTable[TestGameMove, Rational] =
        (game: TestGame, state: TestGameState) => {
          val mm = movesMap(state)
          pm.construct(Variable("S"), mm.keys, s => mm(s)._2)
        }

      def isValid(game: TestGame, state: TestGameState, move: TestGameMove): Either[String, TestGameMove] =
        Right(move)

      def applyMove(game: TestGame, state: TestGameState, move: TestGameMove): TestGameState =
        movesMap(state)(move)._1

      def outcome(game: TestGame, state: TestGameState): Option[TestGameOutcome] =
        None

      implicit def probabilityModelPM: ProbabilityModel[ConditionalProbabilityTable] =
        ConditionalProbabilityTable.probabilityWitness
    }

  import evGame._

  implicit val distV = probabilityDist

  val pm = ConditionalProbabilityTable.probabilityWitness

  val currentStateModelMap = Map(Sa -> Rational(1, 3), Sb -> Rational(2, 3))
  val currentStateModel = pm.construct(Variable("S"), currentStateModelMap.keys, currentStateModelMap)

  test("moveFromRandomState on hard-coded graph from start state") {

    // Randomly select Sa then Mab
    //   Sa: (1/3, -1/4) => 1/3 + (1/3 . -1/4) = 1/4
    //   Sb: (2/3,  1/4) => 2/3 + (1/3 . -1/4) = 3/4

    // (Sa, Mac) yields
    //   Sa: (1/3, -1/4) => 1/3 + (1/3 . -1/4) = 1/4
    //   Sb: (2/3,  0  ) => 2/3 + (1/3 .  0  ) = 2/3
    //   Sc: (0  ,  1/4) => 0   + (1/3 .  1/4) = 1/12

    val actualResult = ((1 to 1000) map { i =>
      moveFromRandomState(game, currentStateModel, rng)
    }) toSet

    val expectedResult = Set(
      (Some((Sa, Maa)), currentStateModel),
      (Some((Sa, Mab)), pm.construct(Variable("S"), List(Sa, Sb), Map(
        Sa -> Rational(1, 4),
        Sb -> Rational(3, 4)))),
      (Some((Sa, Mac)), pm.construct(Variable("S"), List(Sa, Sb, Sc), Map(
        Sa -> Rational(1, 4),
        Sb -> Rational(2, 3),
        Sc -> Rational(1, 12)))),
      (Some((Sb, Mbb)), currentStateModel)
    )

    actualResult should be(expectedResult)
  }

  // (Sa, Maa) or (Sb, Mbb) or (Sc, Mcc) leaves distribution untouched

}