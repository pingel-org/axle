package axle.game.poker

import org.scalatest._
import axle.game._
import axle.game.Strategies._

class PokerMoveValidationSpec extends FunSuite with Matchers {

  import axle.game.poker.evGame._

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
