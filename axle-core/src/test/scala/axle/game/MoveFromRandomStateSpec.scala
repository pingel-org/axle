package axle.game

//import axle.stats._
import spire.math._

class MoveFromRandomStateSpec {

  case class TestGameState(name: String)
  val Sa = TestGameState("a")
  val Sb = TestGameState("b")
  val Sc = TestGameState("c")

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

  val movesMap: Map[TestGameState, Map[TestGameMove, Rational]] = Map(
    Sa -> Map(Maa -> Rational(1, 2), Mab -> Rational(1, 4), Mac -> Rational(1, 4)),
    Sb -> Map(Mba -> Rational(0),    Mbb -> Rational(1),    Mbc -> Rational(0)),
    Sc -> Map(Mca -> Rational(0),    Mcb -> Rational(0),    Mcc -> Rational(1))
  )

  //val currentStateModelMap = Map(Sa -> Rational(1, 3), Sb -> Rational(2, 3))
  //val currentStateModel = prob(Variable("S"), currentStateModelMap.keys, currentStateModelMap)

  //   * (Sa, Maa) or (Sb, Mbb) leaves distribution untouched
//     * Special case leaves distribution untouched
//     * Sa:                                   = 1/3
//     * Sb:                                   = 2/3

//   * Randomly select Sa then Mac
//     * Sa: (1/3, -1/4) => 1/3 + (1/3 . -1/4) = 1/4
//     * Sb: (2/3,  0  ) => 2/3 + (1/3 .  0  ) = 2/3
//     * Sc: (0  ,  1/4) => 0   + (1/3 .  1/4) = 1/12

//   * Randomly select Sa then Mab
//     * Sa: (1/3, -1/4) => 1/3 + (1/3 . -1/4) = 1/4
//     * Sb: (2/3,  1/4) => 2/3 + (1/3 . -1/4) = 3/4

// * Sb then Mbb

}