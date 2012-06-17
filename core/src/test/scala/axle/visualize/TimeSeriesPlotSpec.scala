package axle.visualize

class TimeSeriesPlotSpec {

  import util.Random
  import collection._
  import math.{ Pi, cos, sin }
  import axle.visualize._
  import axle.visualize.Plot._
  import org.joda.time.DateTime

  def randomTimeSeries() = {
    val phase = Random.nextDouble
    val amp = Random.nextDouble
    val f = Random.nextDouble
    val now = new DateTime()
    new immutable.TreeMap[DateTime, Double]() ++
      (0 to 100).map(i => (now.plusMinutes(2 * i) -> amp * sin(phase + (i / (10 * f))))).toMap
  }

  val tss = (0 until 20).map(i => randomTimeSeries()).toList

  val frame = new AxleFrame()

  val vis = new Plot(tss, true)

  frame.add(vis)

}