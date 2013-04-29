package axle.visualize

import org.specs2.mutable._

class TimeSeriesPlotSpec extends Specification {

  "Tics for units" should {
    "work" in {

      import axle.quanta._
      import Information._

      bit.plottable.tics(0 *: bit, 1 *: bit) must be equalTo Vector(
        (0.0 *: bit, "0.0"),
        (0.1 *: bit, "0.1"),
        (0.2 *: bit, "0.2"),
        (0.3 *: bit, "0.3"),
        (0.4 *: bit, "0.4"),
        (0.5 *: bit, "0.5"),
        (0.6 *: bit, "0.6"),
        (0.7 *: bit, "0.7"),
        (0.8 *: bit, "0.8"),
        (0.9 *: bit, "0.9"),
        (1.0 *: bit, "1.0")
      )
    }
  }

  def t1(): Unit = {

    import util.Random
    import collection._
    import math.{ Pi, cos, sin }
    import axle.visualize._
    import axle.algebra.Plottable._
    import org.joda.time.DateTime

    val now = new DateTime()

    def randomTimeSeries(i: Int) = {
      val phase = Random.nextDouble
      val amp = Random.nextDouble
      val f = Random.nextDouble
      ("series " + i,
        new immutable.TreeMap[DateTime, Double]() ++
        (0 to 100).map(j => (now.plusMinutes(2 * j) -> amp * sin(phase + (j / (10 * f))))).toMap)
    }

    val lfs = (0 until 20).map(i => randomTimeSeries(i)).toList

    val plot = new Plot(lfs, connect = true, drawKey = true, xAxis = Some(0.0), yAxis = Some(now))

    // show(plot)
  }

  def t2(): Unit = {

    import collection._
    import axle.visualize.Plot
    import axle.algebra.Plottable
    import Plottable._
    import axle.quanta._
    import Information._
    import axle.stats._

    val hm = new immutable.TreeMap[Double, Q]() ++ (0 to 100).map(i => (i / 100.0, H(coin(i / 100.0)))).toMap

    val plot = new Plot(List(("h", hm)),
      connect = true, drawKey = false,
      xAxis = Some(0.0 *: bit), xAxisLabel = Some("p(x='HEAD)"),
      yAxis = Some(0.0), yAxisLabel = Some("H"),
      title = Some("Entropy"))(DoublePlottable, Information.UnitPlottable(bit))

    // show(plot)

  }

}
