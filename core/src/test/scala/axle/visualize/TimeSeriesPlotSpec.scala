package axle.visualize

class TimeSeriesPlotSpec {

  def t1(): Unit = {

    import util.Random
    import collection._
    import math.{ Pi, cos, sin }
    import axle.visualize._
    import axle.visualize.Plottable._
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

    val lfs: Seq[(String, SortedMap[DateTime, Double])] = (0 until 20).map(i => randomTimeSeries(i)).toList

    val plot = new Plot(lfs, connect = true, drawKey = true, xAxis = 0.0, yAxis = now)

    // show(plot)
  }

  def t2(): Unit = {

    import collection._
    import axle.visualize.{ Plot, Plottable }
    import Plottable._
    import axle.quanta._
    import Information._
    import axle.stats._

    val hm: SortedMap[Double, Q] = new immutable.TreeMap[Double, Q]() ++ (0 to 100).map(i => (i / 100.0, H(coin(i / 100.0)))).toMap

    val plot = new Plot(() => List(("h", hm)),
      connect = true, drawKey = false,
      xAxis = 0.0 *: bit, xAxisLabel = Some("p(x='HEAD)"),
      yAxis = 0.0, yAxisLabel = Some("H"),
      title = Some("Entropy"))(DoublePlottable, Information.UnitPlottable(bit))

    // show(plot)

  }

}
