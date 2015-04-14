package axle.visualize

import java.awt.Color.blue

import org.specs2.mutable.Specification

import axle.algebra.modules.doubleDoubleModule
import axle.algebra.modules.doubleRationalModule
import axle.game.Bowling.Bowlers
import axle.game.Bowling.stateDistribution
import axle.jung.JungDirectedGraph
import axle.quanta.Angle
import axle.stats.Distribution0
import spire.implicits.DoubleAlgebra
import spire.math.Rational

class GameChartSpec extends Specification {

  "BarChart" should {
    "chart bowling probability distribution" in {

      val stateD = stateDistribution(Bowlers.goodBowler, 4)

      val scoreD = stateD.map(_.tallied)

      implicit val ac = Angle.converterGraph[Double, JungDirectedGraph]
      import ac.degree

      val chart = BarChart[Int, Rational, Distribution0[Int, Rational]](
        scoreD,
        xAxis = Rational(0),
        title = Some("bowling scores"),
        labelAngle = 36d *: degree,
        colors = List(blue),
        drawKey = false)

      implicit val dc = axle.visualize.BarChart.drawBarChart[Int, Rational, Distribution0[Int, Rational]]

      1 must be equalTo 1
    }
  }

}