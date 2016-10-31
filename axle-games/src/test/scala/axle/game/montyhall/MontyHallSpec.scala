package axle.game.montyhall

import axle.dropOutput
import axle.game._
import axle.game.Strategies._
import org.specs2.mutable._

class MontyHallSpec extends Specification {

  import axle.game.montyhall.evGame._
  import axle.game.montyhall.evGameIO._

  val contestant = Player("C", "Contestant")
  val monty = Player("M", "Monty Hall")

  val game = MontyHall(
    contestant, interactiveMove, dropOutput,
    monty, interactiveMove, dropOutput)

  "random game" should {

    val rGame = MontyHall(
      contestant, randomMove, dropOutput,
      monty, randomMove, dropOutput)

    "have an intro message" in {
      introMessage(rGame) must contain("Monty")
    }

    "produce moveStateStream" in {
      moveStateStream(rGame, startState(rGame)).take(2).length must be equalTo 2
    }

    "play" in {
      val endState = play(rGame, startState(rGame), false)
      moves(rGame, endState).length must be equalTo 0
    }

    "product game stream" in {
      val games = gameStream(rGame, startState(rGame), false).take(2)
      games.length must be equalTo 2
    }

  }

  "startFrom" should {
    "simply return the start state" in {
      val state = startState(game)
      val move = moves(game, state).head
      val nextState = applyMove(game, state, move)
      val newStart = startFrom(game, nextState).get
      moves(game, newStart).length must be equalTo 3
    }
  }

  "starting moves" should {
    "be three-fold, display to monty with 'something'" in {

      val startingMoves = moves(game, startState(game))
      val mm = evGame.maskMove(game, startingMoves.head, contestant, monty)

      displayMoveTo(game, mm, contestant, monty) must contain("placed")
      startingMoves.length must be equalTo 3
    }
  }

  "move parser" should {
    "accept and reject strings appropriately" in {

      evGameIO.parseMove(game, "foo") must be equalTo Left("foo is not a valid move.  Please select again")

      evGameIO.parseMove(game, "car 1") must be equalTo (Right(PlaceCar(1)))
      evGameIO.parseMove(game, "car 2") must be equalTo (Right(PlaceCar(2)))
      evGameIO.parseMove(game, "car 3") must be equalTo (Right(PlaceCar(3)))
      evGameIO.parseMove(game, "pick 1") must be equalTo (Right(FirstChoice(1)))
      evGameIO.parseMove(game, "pick 2") must be equalTo (Right(FirstChoice(2)))
      evGameIO.parseMove(game, "pick 3") must be equalTo (Right(FirstChoice(3)))
      evGameIO.parseMove(game, "reveal 1") must be equalTo (Right(Reveal(1)))
      evGameIO.parseMove(game, "reveal 2") must be equalTo (Right(Reveal(2)))
      evGameIO.parseMove(game, "reveal 3") must be equalTo (Right(Reveal(3)))
      evGameIO.parseMove(game, "change") must be equalTo (Right(Change()))
      evGameIO.parseMove(game, "stay") must be equalTo (Right(Stay()))
    }
  }

  "move validator" should {
    "accept and reject moves appropriately" in {

      val firstMove = PlaceCar(1)
      val secondState = applyMove(game, startState(game), firstMove)

      evGameIO.parseMove(game, "pick 1").right.flatMap(move => evGame.isValid(game, secondState, move)).isRight must be equalTo true
      evGameIO.parseMove(game, "pick 3").right.flatMap(move => evGame.isValid(game, secondState, move)).isRight must be equalTo true
    }
  }

}