package axle.game.poker

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
//import cats.implicits._
import axle.game._
import axle.game.Strategies._

class PokerMoveValidationSpec extends AnyFunSuite with Matchers {

  import axle.game.poker.evGame._
  implicit val rat = new spire.math.RationalAlgebra()

  val p1 = Player("P1", "Player 1")
  val p2 = Player("P2", "Player 2")

  val game = Poker(
    Vector(
    (p1, randomMove, println),
    (p2, randomMove, println)),
    println)

  test("move validator reject River as first move") {
    val ms = evGame.maskState(game, startState(game), game.dealer)
    isValid(game, ms, River()) should be(Left("invalid move"))
  }
}
