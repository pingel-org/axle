package axle.game

import axle.stats._
import spire.math.Rational

object Bowling {

  case class Bowler(firstRoll: Distribution0[Int, Rational], spare: Distribution0[Boolean, Rational])

  case class State(tallied: Int, twoAgoStrike: Boolean, oneAgoSpare: Boolean, oneAgoStrike: Boolean)

  def next(current: State, first: Int, makeSpare: Boolean): State = {
    import current._

    // TODO: break 'score' into contributions to current, one ago, and two ago frames

    val score: Int = first +
      (if (twoAgoStrike) first else 0) +
      (if (oneAgoSpare) first else 0) +
      (if (oneAgoStrike) first else 0) +
      (if (first < 10 && makeSpare) (10 - first) else 0) +
      (if (first < 10 && makeSpare && oneAgoStrike) (10 - first) else 0)

    State(tallied + score, oneAgoStrike, first == 10, first != 10 && makeSpare)
  }

  object Bowlers {
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

    // 1% over 0-6, 13% 7, 20% 8, 30% 9, 30% 10
    val goodBowler = Bowler(
      firstRoll = ConditionalProbabilityTable0(Map(
        0 -> Rational(1, 100),
        1 -> Rational(1, 100),
        2 -> Rational(1, 100),
        3 -> Rational(1, 100),
        4 -> Rational(1, 100),
        5 -> Rational(1, 100),
        6 -> Rational(1, 100),
        7 -> Rational(13, 100),
        8 -> Rational(1, 5),
        9 -> Rational(3, 10),
        10 -> Rational(3, 10))),
      spare = binaryDecision(Rational(8, 10)))

  }

  def scoreDistribution(bowler: Bowler, numFrames: Int): Distribution0[State, Rational] = {

    import bowler._

    val startState: Distribution0[State, Rational] =
      ConditionalProbabilityTable0(Map(State(0, false, false, false) -> Rational(1)))

    (1 to numFrames).foldLeft(startState)({
      case (currentState, _) => for {
        c <- currentState
        f <- firstRoll
        s <- spare
      } yield next(c, f, s)
    })
  }

  // val cpt = sd.asInstanceOf[ConditionalProbabilityTable0[Int, Rational]]
  // cpt.p.toList.sortBy(_._1).map( vp => (vp._1, vp._2.toDouble)) foreach println

}