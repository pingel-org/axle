package axle.visualize

import org.specs2.mutable.Specification

import axle.algebra.modules.doubleDoubleModule
import axle.algebra.modules.doubleRationalModule
import axle.game.Bowling.Bowlers.goodBowler
import axle.game.Bowling.stateDistribution
import axle.jung.directedGraphJung
import axle.quanta.Angle
import axle.stats.Distribution0
import axle.web._
import edu.uci.ics.jung.graph.DirectedSparseGraph
import spire.implicits.DoubleAlgebra
import spire.implicits.IntAlgebra
import spire.math.Rational

class GameChartSpec extends Specification {

  "BarChart" should {
    "chart bowling probability distribution" in {

      val stateD = stateDistribution(goodBowler, 4)

      val scoreD = stateD.map(_.tallied)

      implicit val ac = Angle.converterGraphK2[Double, DirectedSparseGraph]

      // test implicit conjuring:
      PlotDataView.distribution0DataView[Int, Rational]

      val plot = Plot[Int, Rational, Distribution0[Int, Rational]](
        List(("score", scoreD)),
        connect = true,
        drawKey = true,
        xAxis = Some(Rational(0)),
        yAxis = Some(0))

      val filename = "bowl.svg"

      svg(plot, filename)

      new java.io.File(filename).exists must be equalTo true
    }
  }

}