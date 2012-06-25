
import util.Random
import collection._
import math.{ Pi, cos, sin }
import axle.visualize._
import axle.visualize.Plottable._
import org.joda.time.DateTime

val now = new DateTime()

def randomTimeSeries() = {
  val phase = Random.nextDouble
  val amp = Random.nextDouble
  val f = Random.nextDouble
  (phase + " " + amp + " " + f, new immutable.TreeMap[DateTime, Double]() ++
      (0 to 100).map(j => (now.plusMinutes(2 * j) -> amp * sin(phase + (j / (10 * f))))).toMap)
}

val lfs = (0 until 20).map(i => randomTimeSeries()).toList
val frame = new AxleFrame()
val vis = new Plot(lfs, true, title = Some("Random Sin Waves"), xAxis=0.0, xAxisLabel=Some("x"), yAxis=now, yAxisLabel=Some("random f(x) involving sin"))

frame.add(vis)

