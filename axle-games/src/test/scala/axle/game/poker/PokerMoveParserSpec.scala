package axle.game.poker

import axle.game.Player
import org.specs2.mutable._

class PokerMoveParserSpec extends Specification {

  val p1 = Player("P1", "Player 1")
  val moveParser = MoveParser()

  "move parser" should {
    "parse well-formed move strings" in {
      moveParser.parse("call")(p1) must be equalTo Right(Call(p1))
      moveParser.parse("fold")(p1) must be equalTo Right(Fold(p1))
      moveParser.parse("raise 1")(p1) must be equalTo Right(Raise(p1, 1))
    }
    "reject ill-formed move strings" in {
      moveParser.parse("raise x")(p1) must be equalTo Left("invalid input: raise x")
      moveParser.parse("asdf")(p1) must be equalTo Left("invalid input: asdf")
    }
  }

}