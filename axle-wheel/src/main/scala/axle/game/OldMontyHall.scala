package axle.game

import cats.implicits._

import spire.math.Rational

import axle.algebra.RegionEq
import axle.probability._
import axle.syntax.kolmogorov._

object OldMontyHall {

  // import cats.syntax.all._

  val numDoors = 3

  val prizeDoorModel: CPTR[Int] = uniformDistribution(1 to numDoors)

  val chosenDoorModel: CPTR[Int] = uniformDistribution(1 to numDoors)

  def reveal(prizeDoor: Int, chosenDoor: Int): CPTR[Int] =
    uniformDistribution((1 to numDoors).filterNot(d => d === prizeDoor || d === chosenDoor))

  def switch(probabilityOfSwitching: Rational, chosenDoor: Int, revealedDoor: Int): CPTR[Int] = {

    val availableDoors = (1 to numDoors).filterNot(d => d === revealedDoor || d === chosenDoor)

    (binaryDecision(probabilityOfSwitching): CPTR[Boolean]).flatMap { cond =>
      if( cond ) {
        uniformDistribution(availableDoors) // switch
      } else {
        uniformDistribution(Seq(chosenDoor)) // stay
      }
    }
  }

  // TODO: The relationship between probabilityOfSwitching and outcome can be performed more efficiently and directly.
  val outcome = (probabilityOfSwitching: Rational) => 
    prizeDoorModel.flatMap { prizeDoor =>
      chosenDoorModel.flatMap { chosenDoor =>
        reveal(prizeDoor, chosenDoor).flatMap { revealedDoor =>
          switch(probabilityOfSwitching, chosenDoor, revealedDoor).map { finalChosenDoor =>
            finalChosenDoor === prizeDoor
          }
        }
      }
    }
 
  val chanceOfWinning =
    (probabilityOfSwitching: Rational) => outcome(probabilityOfSwitching).P(RegionEq(true))

}
