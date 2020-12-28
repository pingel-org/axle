package axle.game

import cats.kernel.Eq

import spire.math._
import spire.math.Rational.RationalAlgebra
import spire.random.Generator.rng

import axle.probability._

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

class MoveFromRandomStateSpec extends AnyFunSuite with Matchers {

  val player = Player("P", "player")

  case class TestGame()

  val game = TestGame()

  case class TestGameState(name: String)
  val Sa = TestGameState("a")
  val Sb = TestGameState("b")
  val Sc = TestGameState("c")

  implicit val eqS: Eq[TestGameState] = Eq.fromUniversalEquals[TestGameState]
  implicit val eqM: Eq[TestGameMove] = Eq.fromUniversalEquals[TestGameMove]

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

  implicit val evGame: Game[TestGame,TestGameState,TestGameOutcome,TestGameMove,TestGameState,TestGameMove] =
    new Game[TestGame,TestGameState,TestGameOutcome,TestGameMove,TestGameState,TestGameMove] {

      def startState(game: TestGame): TestGameState =
        Sa

      def startFrom(game: TestGame, state: TestGameState): Option[TestGameState] =
        Some(startState(game))

      def players(game: TestGame): IndexedSeq[Player] =
        Vector(player)

      def mover(game: TestGame, state: TestGameState): Either[TestGameOutcome, Player] =
        Right(player)

      def moves(game: TestGame, state: TestGameState): Seq[TestGameMove] =
        movesMap(state).keys.toVector

      def maskState(game: TestGame, state: TestGameState, observer: Player): TestGameState =
        state

      def maskMove(game: TestGame, move: TestGameMove, mover: Player, observer: Player): TestGameMove =
        move

      def isValid(game: TestGame, state: TestGameState, move: TestGameMove): Either[String, TestGameMove] =
        Right(move)

      def applyMove(game: TestGame, state: TestGameState, move: TestGameMove): TestGameState =
        movesMap(state)(move)._1

    }


  // val pm = ConditionalProbabilityTable.probabilityWitness

  val currentStateModelMap = Map(Sa -> Rational(1, 3), Sb -> Rational(2, 3))
  val currentStateModel = ConditionalProbabilityTable(currentStateModelMap)

  test("moveFromRandomState on hard-coded graph from start state") {

    // Original non-zero transitions:
    // from \ to
    //   a    b    c
    // a 1/2  1/4  1/4
    // b 0    1    0
    // c 0    0    1

    // Current state distribution
    // a 1/3
    // b 2/3

    // Randomly select Sa then Mab
    //  Sa: (1/3, -1/4) => 1/3 + (1/3 . -1/4) = 1/4
    //  Sb: (2/3,  1/4) => 2/3 + (1/3 . -1/4) = 3/4

    // Randomly select Sa then Mac
    //  Sa: (1/3, -1/4) => 1/3 + (1/3 . -1/4) = 1/4
    //  Sb: (2/3,  0  ) => 2/3 + (1/3 .  0  ) = 2/3
    //  Sc: (0  ,  1/4) => 0   + (1/3 .  1/4) = 1/12

    // val rm = randomMove[TestGame,TestGameState,TestGameOutcome,TestGameMove,TestGameState,TestGameMove,Rational,ConditionalProbabilityTable](game).andThen(Option.apply _)


// found   :
//   Map[MoveFromRandomStateSpec.this.TestGameMove,spire.math.Rational] => axle.probability.ConditionalProbabilityTable[MoveFromRandomStateSpec.this.TestGameMove,spire.math.Rational]
//
// required:
//   Map[MoveFromRandomStateSpec.this.TestGameMove,(MoveFromRandomStateSpec.this.TestGameState, spire.math.Rational)] => axle.probability.ConditionalProbabilityTable[MoveFromRandomStateSpec.this.TestGameMove,spire.math.Rational]
//

    val rm: TestGameState => Option[ConditionalProbabilityTable[TestGameMove,Rational]] =
      (s: TestGameState) =>
        movesMap.get(s).map(m =>
          ConditionalProbabilityTable.apply[TestGameMove, Rational](
            m.map( kv => kv._1 -> kv._2._2 )
          )
        )

    // movesMap: Map[TestGameState, Map[TestGameMove, (TestGameState, Rational)]]

    val actualResult = ((1 to 1000) map { i =>
      moveFromRandomState(
        game,
        currentStateModel,
        _ => rm,
        ConditionalProbabilityTable.apply[TestGameState, Rational],
        rng).get
    }) toSet

    val expectedResult = Set(
      (Some((Sa, Maa)), currentStateModel),
      (Some((Sa, Mab)), ConditionalProbabilityTable(Map(
        Sa -> Rational(1, 4),
        Sb -> Rational(3, 4)))),
      (Some((Sa, Mac)), ConditionalProbabilityTable(Map(
        Sa -> Rational(1, 4),
        Sb -> Rational(2, 3),
        Sc -> Rational(1, 12)))),
      (Some((Sb, Mbb)), currentStateModel)
    )

    actualResult should be(expectedResult)
  }

  // (Sa, Maa) or (Sb, Mbb) or (Sc, Mcc) leaves distribution untouched

}