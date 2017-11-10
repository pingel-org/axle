package axle.game

import cats.implicits._
import spire.math.Rational
import axle.stats._

object OldMontyHall {

  implicit val monad = ProbabilityModel.monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]
  val prob = implicitly[ProbabilityModel[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]]

  val numDoors = 3

  val prizeDoorModel = uniformDistribution(1 to numDoors, Variable[Int]("prize"))

  val chosenDoorModel = uniformDistribution(1 to numDoors, Variable[Int]("chosen"))

  def reveal(prizeDoor: Int, chosenDoor: Int) =
    uniformDistribution((1 to numDoors).filterNot(d => d === prizeDoor || d === chosenDoor), Variable[Int]("reveal"))

  def switch(probabilityOfSwitching: Rational, chosenDoor: Int, revealedDoor: Int) = {

    val availableDoors = (1 to numDoors).filterNot(d => d === revealedDoor || d === chosenDoor)

    iffy[Int, Rational, ({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, ({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ](
      binaryDecision(probabilityOfSwitching),
      uniformDistribution(availableDoors, Variable("switch")), // switch
      uniformDistribution(Seq(chosenDoor), Variable("switch")) // stay
    )
  }

  // TODO: The relationship between probabilityOfSwitching and outcome can be performed more efficiently and directly.
  val outcome = (probabilityOfSwitching: Rational) => for {
    prizeDoor <- prizeDoorModel
    chosenDoor <- chosenDoorModel
    revealedDoor <- reveal(prizeDoor, chosenDoor)
    finalChosenDoor <- switch(probabilityOfSwitching, chosenDoor, revealedDoor)
  } yield finalChosenDoor === prizeDoor

  val chanceOfWinning =
    (probabilityOfSwitching: Rational) => prob.probabilityOf(outcome(probabilityOfSwitching), true)

}
