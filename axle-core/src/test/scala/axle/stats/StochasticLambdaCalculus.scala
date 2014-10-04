package axle.stats

import org.specs2.mutable.Specification

import axle.game.Dice.die
import axle.Σ
import spire.implicits.IntAlgebra
import spire.math.Rational

object StochasticLambdaCalculus extends Specification {

  "iffy (stochastic if)" should {
    "map fair boolean to d6 + (d6+d6)" in {

      val distribution =
        iffy(
          binaryDecision(Rational(1, 3)),
          die(6),
          for { a <- die(6); b <- die(6) } yield a + b)

      distribution.probabilityOf(1) must be equalTo Rational(1, 18)

      distribution.probabilityOf(12) must be equalTo Rational(1, 54)

      Σ(distribution.values)(distribution.probabilityOf) must be equalTo Rational(1)
    }
  }

  "Monty Hall contestant" should {

    "always pick the other door" in {

      val numDoors = 3

      val prizeDoor = uniformDistribution(1 to numDoors, "prize")

      val chosenDoor = uniformDistribution(1 to numDoors, "chosen")

      def reveal(p: Int, c: Int) =
        if (p == c) uniformDistribution((1 to numDoors).filter(_ == p), "reveal")
        else uniformDistribution((1 to numDoors).filter(d => d == p || d == c), "reveal")

      def switch(probabilityOfSwitching: Rational, c: Int, r: Int): Distribution0[Int, Rational] =
        iffy(
          binaryDecision(probabilityOfSwitching),
          uniformDistribution((1 to numDoors).filter(d => d == r || d == c), "switch"), // switch
          uniformDistribution(Seq(c), "switch") // stay
          )

      def win(probabilityOfSwitching: Rational) = for {
        p <- prizeDoor
        c <- chosenDoor
        r <- reveal(p, c)
        c2 <- switch(probabilityOfSwitching, c, r)
      } yield c2 == p

      win(Rational(1)).probabilityOf(true) must be equalTo (Rational(1, 2))

      win(Rational(0)).probabilityOf(true) must be equalTo (Rational(1, 3))

      // TODO: for any two probabilities p1 and p2, 
      // p1 > p2 implies win(p1).probabilityOf(true) > win(p2).probabilityOf(true)
    }
  }

}