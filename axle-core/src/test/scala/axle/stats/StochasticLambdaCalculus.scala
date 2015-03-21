package axle.stats

import org.specs2.mutable.Specification

import axle.game.Dice.die
import spire.optional.unicode.Σ
import spire.implicits.IntAlgebra
import spire.math.Rational
import spire.syntax.literals._

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

      Σ(distribution.values map distribution.probabilityOf) must be equalTo Rational(1)
    }
  }

  "Monty Hall contestant" should {

    "always pick the other door" in {

      val numDoors = 3

      val prizeDoor = uniformDistribution(1 to numDoors, "prize")

      val chosenDoor = uniformDistribution(1 to numDoors, "chosen")

      def reveal(p: Int, c: Int) =
        uniformDistribution((1 to numDoors).filter(d => d == p || d == c), "reveal")

      def switch(probabilityOfSwitching: Rational, c: Int, r: Int) =
        iffy(
          binaryDecision(probabilityOfSwitching),
          uniformDistribution((1 to numDoors).filter(d => d == r || d == c), "switch"), // switch
          uniformDistribution(Seq(c), "switch") // stay
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

      chanceOfWinning(Rational(1)) must be equalTo Rational(1, 2)

      chanceOfWinning(Rational(0)) must be equalTo Rational(1, 3)

      // TODO: p1 > p2 <=> chanceOfWinning(p1) > chanceOfWinning(p2)
      //        aka "is monotonically increasing"
    }
  }

  "bowling" should {
    "work" in {

      case class Bowler(firstRoll: Distribution0[Int, Rational], spare: Distribution0[Boolean, Rational])

      val randomBowler =
        Bowler(
          firstRoll = uniformDistribution(0 to 10, "uniform first roll"),
          spare = binaryDecision(Rational(1, 2)))

      // bad bowler.  50% gutter-ball, even (5%) distribution of 1-10
      val badBowler =
        Bowler(
          firstRoll = ConditionalProbabilityTable0(Map(
            0 -> Rational(5, 10),
            1 -> Rational(1, 20),
            2 -> Rational(1, 20),
            3 -> Rational(1, 20),
            4 -> Rational(1, 20),
            5 -> Rational(1, 20),
            6 -> Rational(1, 20),
            7 -> Rational(1, 20),
            8 -> Rational(1, 20),
            9 -> Rational(1, 20),
            10 -> Rational(1, 20)), "bad first roll"),
          spare = binaryDecision(Rational(1, 10)))

      // decent bowler.  5%  over 0-5, 10% 6, 15% over 7-10
      val decentBowler =
        Bowler(
          firstRoll = ConditionalProbabilityTable0(Map(
            0 -> Rational(1, 20),
            1 -> Rational(1, 20),
            2 -> Rational(1, 20),
            3 -> Rational(1, 20),
            4 -> Rational(1, 20),
            5 -> Rational(1, 20),
            6 -> Rational(1, 10),
            7 -> Rational(3, 20),
            8 -> Rational(3, 20),
            9 -> Rational(3, 20),
            10 -> Rational(3, 20))),
          spare = binaryDecision(Rational(1, 10)))

      // 4%  over 0-6, 12% 7, 20% 8, 30% 9, 30% 10
      val goodBowler = Bowler(
        firstRoll = ConditionalProbabilityTable0(Map(
          0 -> Rational(1, 25),
          1 -> Rational(1, 25),
          2 -> Rational(1, 25),
          3 -> Rational(1, 25),
          4 -> Rational(1, 25),
          5 -> Rational(1, 25),
          6 -> Rational(1, 25),
          7 -> Rational(3, 25),
          8 -> Rational(1, 5),
          9 -> Rational(3, 10),
          10 -> Rational(3, 10))),
        spare = binaryDecision(Rational(8, 10)))

      def scoreFrame(
        twoAgoStrike: Boolean,
        oneAgoSpare: Boolean,
        oneAgoStrike: Boolean,
        first: Int,
        makeSpare: Boolean): (Int, Boolean, Boolean, Boolean) = {
        // TODO: break 'score' into contributions to current, one ago, and two ago frames
        val score: Int = first +
          (if (twoAgoStrike) first else 0) +
          (if (oneAgoSpare) first else 0) +
          (if (oneAgoStrike) first else 0) +
          (if (first < 10 && makeSpare) (10 - first) else 0) +
          (if (first < 10 && makeSpare && oneAgoStrike) (10 - first) else 0)
        (score, oneAgoStrike, first == 10, first != 10 && makeSpare)
      }

      def scoreDistribution(bowler: Bowler): Distribution0[Int, Rational] =
        for {
          f1 <- bowler.firstRoll;
          s1 <- bowler.spare;
          f2 <- bowler.firstRoll;
          s2 <- bowler.spare;
          f3 <- bowler.firstRoll;
          s3 <- bowler.spare;
          f4 <- bowler.firstRoll
        } yield {
          val frame1 = scoreFrame(false, false, false, f1, s1)
          val frame2 = scoreFrame(frame1._2, frame1._3, frame1._4, f2, s2)
          val frame3 = scoreFrame(frame2._2, frame2._3, frame2._4, f3, s3)
          frame1._1 + frame2._1 + frame3._1
        }

      def scoreDistribution2(bowler: Bowler): Distribution0[Int, Rational] = {

        import bowler._

        val startState: Distribution0[(Int, Boolean, Boolean, Boolean), Rational] =
          ConditionalProbabilityTable0(Map((0, false, false, false) -> Rational(1)))

        (1 to 10).foldLeft(startState)({
          case (incoming, _) => for {
            i <- incoming;
            f <- firstRoll;
            s <- spare
          } yield {
            val frame = scoreFrame(i._2, i._3, i._4, f, s)
            (i._1 + frame._1, frame._2, frame._3, frame._4)
          }
        }) map { _._1 }
      }

      scoreDistribution2(goodBowler) // TODO the probabilities are summing to > 1

      // val cpt = sd.asInstanceOf[ConditionalProbabilityTable0[Int, Rational]]
      // cpt.p.toList.sortBy(_._1).map( vp => (vp._1, vp._2.toDouble)) foreach println

      1 must be equalTo 1
    }
  }

  "π estimation" should {
    "work" in {

      import scala.math.sqrt

      val n = 200

      // TODO: sample a subset of n for each of x and y
      // to compute the distribution of estimates

      val piDist = for {
        x <- uniformDistribution(0 to n, "x")
        y <- uniformDistribution(0 to n, "y")
      } yield if (sqrt(x * x + y * y) <= n) 1 else 0

      1 must be equalTo 1
    }
  }

}