package axle.game.poker

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import axle.game._

class PokerMoveValidationSpec extends AnyFunSuite with Matchers {

  import axle.game.poker.evGame._
  implicit val rat = new spire.math.RationalAlgebra()

  val p1 = Player("P1", "Player 1")
  val p2 = Player("P2", "Player 2")

  val game = Poker(Vector(p1, p2))

  test("move validator reject River as first move") {
    val ms = evGame.maskState(game, startState(game), game.dealer)
    isValid(game, ms, River()) should be(Left("invalid move"))
  }
}
