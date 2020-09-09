package axle.game

import cats.implicits._

import spire.math.Rational

import axle.algebra.RegionEq
import axle.probability._
import axle.syntax.kolmogorov._

object OldMontyHall {

  import cats.syntax.all._
  implicit val mcpt = ConditionalProbabilityTable.monadWitness[Rational]

  val numDoors = 3

  val prizeDoorModel = uniformDistribution(1 to numDoors)

  val chosenDoorModel = uniformDistribution(1 to numDoors)

  def reveal(prizeDoor: Int, chosenDoor: Int) =
    uniformDistribution((1 to numDoors).filterNot(d => d === prizeDoor || d === chosenDoor))

  def switch(probabilityOfSwitching: Rational, chosenDoor: Int, revealedDoor: Int) = {

    val availableDoors = (1 to numDoors).filterNot(d => d === revealedDoor || d === chosenDoor)

    mcpt.flatMap(binaryDecision(probabilityOfSwitching)) { cond =>
      if( cond ) {
        uniformDistribution(availableDoors) // switch
      } else {
        uniformDistribution(Seq(chosenDoor)) // stay
      }
    }
  }

  // TODO: The relationship between probabilityOfSwitching and outcome can be performed more efficiently and directly.
  val outcome = (probabilityOfSwitching: Rational) => 
    mcpt.flatMap(prizeDoorModel) { prizeDoor =>
      mcpt.flatMap(chosenDoorModel) { chosenDoor =>
        mcpt.flatMap(reveal(prizeDoor, chosenDoor)) { revealedDoor =>
          mcpt.map(switch(probabilityOfSwitching, chosenDoor, revealedDoor)) { finalChosenDoor =>
            finalChosenDoor === prizeDoor
          }
        }
      }
    }
 
  val chanceOfWinning =
    (probabilityOfSwitching: Rational) => outcome(probabilityOfSwitching).P(RegionEq(true))

}
