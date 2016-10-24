package axle.game.poker

import axle.game.Player
import org.specs2.mutable._

class PokerMoveParserSpec extends Specification {

  val p1 = Player("P1", "Player 1")
  val moveParser = MoveParser()

  "move parser" should {
    "parse well-formed move strings" in {
      moveParser.parse("call") must be equalTo Right(Call())
      moveParser.parse("fold") must be equalTo Right(Fold())
      moveParser.parse("raise 1") must be equalTo Right(Raise(1))
    }
    "reject ill-formed move strings" in {
      moveParser.parse("raise x") must be equalTo Left("invalid input: raise x")
      moveParser.parse("asdf") must be equalTo Left("invalid input: asdf")
    }
  }

}