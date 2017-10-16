package axle.game

import cats.implicits._
import spire.math.Rational
import axle.stats._

object OldMontyHall {

  implicit val monad = ProbabilityModel.monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]
  val prob = implicitly[ProbabilityModel[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]]

  val numDoors = 3

  val prizeDoor = uniformDistribution(1 to numDoors, Variable[Int]("prize"))

  val chosenDoor = uniformDistribution(1 to numDoors, Variable[Int]("chosen"))

  def reveal(p: Int, c: Int) =
    uniformDistribution((1 to numDoors).filter(d => d == p || d == c), Variable[Int]("reveal"))

  def switch(probabilityOfSwitching: Rational, c: Int, r: Int) =
    iffy(
      binaryDecision(probabilityOfSwitching),
      uniformDistribution((1 to numDoors).filter(d => d == r || d == c), Variable("switch")), // switch
      uniformDistribution(Seq(c), Variable("switch")) // stay
      )

  // TODO: The relationship between probabilityOfSwitching and outcome can be performed more efficiently and directly.
  val outcome = (probabilityOfSwitching: Rational) => for {
    p <- prizeDoor
    c <- chosenDoor
    r <- reveal(p, c)
    c2 <- switch(probabilityOfSwitching, c, r)
  } yield c2 == p

  // val pos = uniformRealDistribution(Range(r"0", r"1"))
  // pos.probabilityOf(Range(r"3/10", r"4/10")) // should be r"1/10"
  // pos.range
  // val chanceOfWinning = pos map { outcome }
  // cow should also now have a value at pos.min and pos.max

  val chanceOfWinning = (probabilityOfSwitching: Rational) => outcome(probabilityOfSwitching).probabilityOf(true)

}
