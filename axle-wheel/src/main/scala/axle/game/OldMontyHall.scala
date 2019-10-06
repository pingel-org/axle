package axle.game

import cats.implicits._

import spire.math.Rational

import axle.algebra.RegionEq
import axle.stats._
import axle.syntax.probabilitymodel._

object OldMontyHall {

  val numDoors = 3

  type F[T] = ConditionalProbabilityTable[T, Rational]

  val prizeDoorModel: F[Int] = uniformDistribution(1 to numDoors)

  val chosenDoorModel: F[Int] = uniformDistribution(1 to numDoors)

  def reveal(prizeDoor: Int, chosenDoor: Int): F[Int] =
    uniformDistribution((1 to numDoors).filterNot(d => d === prizeDoor || d === chosenDoor))

  def switch(probabilityOfSwitching: Rational, chosenDoor: Int, revealedDoor: Int): F[Int] = {

    val availableDoors = (1 to numDoors).filterNot(d => d === revealedDoor || d === chosenDoor)

    iffy( // iffy[Int, Rational, ConditionalProbabilityTable, ConditionalProbabilityTable]
      binaryDecision(probabilityOfSwitching),
      uniformDistribution(availableDoors), // switch
      uniformDistribution(Seq(chosenDoor)) // stay
    )
  }

  import cats.syntax.all._

  val prob = ProbabilityModel[ConditionalProbabilityTable]

  // TODO: The relationship between probabilityOfSwitching and outcome can be performed more efficiently and directly.
  val outcome = (probabilityOfSwitching: Rational) => 
    prob.flatMap(prizeDoorModel) { prizeDoor =>
      prob.flatMap(chosenDoorModel) { chosenDoor =>
        prob.flatMap(reveal(prizeDoor, chosenDoor)) { revealedDoor =>
          prob.map(switch(probabilityOfSwitching, chosenDoor, revealedDoor)) { finalChosenDoor =>
            finalChosenDoor === prizeDoor
          }
        }
      }
    }
 
  val chanceOfWinning =
    (probabilityOfSwitching: Rational) => outcome(probabilityOfSwitching).P(RegionEq(true))

}
