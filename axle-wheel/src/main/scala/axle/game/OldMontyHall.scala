package axle.game

//import scala.language.higherKinds

//import cats.syntax.functor._
//import cats.syntax.flatMap._
//import cats.syntax.applicative._
import cats.implicits._

import spire.math.Rational
import axle.stats._

object OldMontyHall {

  val prob = implicitly[ProbabilityModel[ConditionalProbabilityTable0]]
  implicit val monad = implicitly[cats.Monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ]]

  val numDoors = 3

  val prizeDoorModel = uniformDistribution(1 to numDoors, Variable("prize"))

  val chosenDoorModel = uniformDistribution(1 to numDoors, Variable("chosen"))

  def reveal(prizeDoor: Int, chosenDoor: Int) =
    uniformDistribution((1 to numDoors).filterNot(d => d === prizeDoor || d === chosenDoor), Variable("reveal"))

  def switch(probabilityOfSwitching: Rational, chosenDoor: Int, revealedDoor: Int) = {

    val availableDoors = (1 to numDoors).filterNot(d => d === revealedDoor || d === chosenDoor)

    iffy( // iffy[Int, Rational, ConditionalProbabilityTable0, ConditionalProbabilityTable0]
      binaryDecision(probabilityOfSwitching),
      uniformDistribution(availableDoors, Variable("switch")), // switch
      uniformDistribution(Seq(chosenDoor), Variable("switch")) // stay
    )
  }

  // TODO: The relationship between probabilityOfSwitching and outcome can be performed more efficiently and directly.
  // val outcomeX = (probabilityOfSwitching: Rational) => for {
  //  prizeDoor <- prizeDoorModel
  //  chosenDoor <- chosenDoorModel
  //  revealedDoor <- reveal(prizeDoor, chosenDoor)
  //  finalChosenDoor <- switch(probabilityOfSwitching, chosenDoor, revealedDoor)
  // } yield finalChosenDoor === prizeDoor

  // TODO monad syntax
  val outcome = (probabilityOfSwitching: Rational) =>
    monad.flatMap(prizeDoorModel)(prizeDoor =>
      monad.flatMap(chosenDoorModel)(chosenDoor =>
        monad.flatMap(reveal(prizeDoor, chosenDoor))(revealedDoor =>
          monad.map(switch(probabilityOfSwitching, chosenDoor, revealedDoor))(finalChosenDoor =>
            finalChosenDoor === prizeDoor))))

  val chanceOfWinning =
    (probabilityOfSwitching: Rational) => prob.probabilityOf(outcome(probabilityOfSwitching), true)

}
