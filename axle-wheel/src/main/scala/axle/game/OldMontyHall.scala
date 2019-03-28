package axle.game

import cats.implicits._

import spire.math.Rational
import axle.stats._
import axle.syntax.probabilitymodel._

object OldMontyHall {

  val numDoors = 3

  type F[T] = ConditionalProbabilityTable0[T, Rational]

  val prizeDoorModel: F[Int] = uniformDistribution(1 to numDoors, Variable("prize"))

  val chosenDoorModel: F[Int] = uniformDistribution(1 to numDoors, Variable("chosen"))

  def reveal(prizeDoor: Int, chosenDoor: Int): F[Int] =
    uniformDistribution((1 to numDoors).filterNot(d => d === prizeDoor || d === chosenDoor), Variable("reveal"))

  def switch(probabilityOfSwitching: Rational, chosenDoor: Int, revealedDoor: Int): F[Int] = {

    val availableDoors = (1 to numDoors).filterNot(d => d === revealedDoor || d === chosenDoor)

    iffy( // iffy[Int, Rational, ConditionalProbabilityTable0, ConditionalProbabilityTable0]
      binaryDecision(probabilityOfSwitching),
      uniformDistribution(availableDoors, Variable("switch")), // switch
      uniformDistribution(Seq(chosenDoor), Variable("switch")) // stay
    )
  }

  import cats.syntax.all._

  // TODO: The relationship between probabilityOfSwitching and outcome can be performed more efficiently and directly.
  val outcome = (probabilityOfSwitching: Rational) => for {
   prizeDoor <- prizeDoorModel
   chosenDoor <- chosenDoorModel
   revealedDoor <- reveal(prizeDoor, chosenDoor)
   finalChosenDoor <- switch(probabilityOfSwitching, chosenDoor, revealedDoor)
  } yield finalChosenDoor === prizeDoor

  val chanceOfWinning =
    (probabilityOfSwitching: Rational) => outcome(probabilityOfSwitching).P(true)

}
