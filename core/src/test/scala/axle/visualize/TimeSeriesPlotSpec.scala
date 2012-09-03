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

    val frame = new AxleFrame(width = 1000, height = 600, bgColor = java.awt.Color.white)

    val vis = new Plot(lfs, connect = true, drawKey = true, xAxis = 0.0, yAxis = now)

    frame.add(vis)
  }

  def t2(): Unit = {

    import collection._
    import axle.visualize.{ Plot, Plottable }
    import Plottable._
    import axle.quanta.Information
    import Information._
    import axle.stats._

    val hm: SortedMap[Double, UOM] = new immutable.TreeMap[Double, UOM]() ++ (1 to 99).map(i => (i / 100.0, entropy(coin(i / 100.0)))).toMap

    new AxleFrame().add(new Plot(List(("h", hm)),
      connect = true, drawKey = false,
      xAxis = zero(), xAxisLabel = Some("p(x='HEAD)"),
      yAxis = 0.0, yAxisLabel = Some("H"),
      title = Some("Entropy"))(DoublePlottable, new InfoPlottable(bit)))

  }

}
