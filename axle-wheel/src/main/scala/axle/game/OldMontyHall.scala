package axle.game

import cats.implicits._

import spire.math.Rational

import axle.algebra.RegionEq
import axle.probability._
import axle.syntax.kolmogorov._

object OldMontyHall {

  // import cats.syntax.all._

  val numDoors = 3

  val prizeDoorModel = uniformDistribution(1 to numDoors)

  val chosenDoorModel = uniformDistribution(1 to numDoors)

  def reveal(prizeDoor: Int, chosenDoor: Int): ConditionalProbabilityTable[Int, Rational] =
    uniformDistribution((1 to numDoors).filterNot(d => d === prizeDoor || d === chosenDoor))

  def switch(probabilityOfSwitching: Rational, chosenDoor: Int, revealedDoor: Int): ConditionalProbabilityTable[Int, Rational] = {
 
    val availableDoors = (1 to numDoors).filterNot(d => d === revealedDoor || d === chosenDoor)

    binaryDecision(probabilityOfSwitching).events.flatMap({ cond =>
      if( cond ) {
        uniformDistribution(availableDoors) // switch
      } else {
        uniformDistribution(Seq(chosenDoor)) // stay
      }
    })
  }

  // TODO: The relationship between probabilityOfSwitching and outcome can be performed more efficiently and directly.
  val outcome: Rational => ConditionalProbabilityTable[Boolean, Rational] =
    (probabilityOfSwitching: Rational) => 
      prizeDoorModel.events.flatMap { prizeDoor =>
        chosenDoorModel.events.flatMap { chosenDoor =>
          reveal(prizeDoor, chosenDoor).events.flatMap { revealedDoor =>
            switch(probabilityOfSwitching, chosenDoor, revealedDoor).events.map { finalChosenDoor =>
              finalChosenDoor === prizeDoor
            }
          }
        }
      }
 
  val chanceOfWinning: Rational => Rational =
    (probabilityOfSwitching: Rational) => outcome(probabilityOfSwitching).P(RegionEq(true))

}
