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
    import axle.visualize._
    import axle.quanta.Information._
    import axle.InformationTheory._

    val hl: Seq[(Double, Double)] = (1 to 99).map(i => (i / 100.0, coin(i / 100.0).entropy().conversion.get.getPayload.doubleValue))

    val hm: SortedMap[Double, Double] = new immutable.TreeMap[Double, Double]() ++ hl.toMap

    val lfs: Seq[(String, SortedMap[Double, Double])] = List(("h", hm))

    val frame = new AxleFrame()

    val zeroBits = 0.0 // : UOM = 0.0 *: bit

    val vis = new Plot(lfs, connect = true, drawKey = false,
      xAxis = zeroBits, xAxisLabel = Some("p(x='HEAD)"),
      yAxis = 0.0, yAxisLabel = Some("H"),
      title = Some("Entropy"))

    frame.add(vis)

  }

}