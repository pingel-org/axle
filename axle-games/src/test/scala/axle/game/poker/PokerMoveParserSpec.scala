package axle.game.poker

import axle.game.Player
import org.scalatest._

class PokerMoveParserSpec extends FunSuite with Matchers {

  val p1 = Player("P1", "Player 1")
  val moveParser = MoveParser()

  test("move parser parses well-formed move strings") {
    moveParser.parse("call") should be(Right(Call()))
    moveParser.parse("fold") should be(Right(Fold()))
    moveParser.parse("raise 1") should be(Right(Raise(1)))
  }

  test("move parser rejects ill-formed move strings") {
    moveParser.parse("raise x") should be(Left("invalid input: raise x"))
    moveParser.parse("asdf") should be(Left("invalid input: asdf"))
  }

}
