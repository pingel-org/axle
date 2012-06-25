
import util.Random
import collection._
import math.{ Pi, cos, sin }
import axle.visualize._
import axle.visualize.Plottable._
import org.joda.time.DateTime

val now = new DateTime()

def randomTimeSeries(i: Int) = {
  val (phase, amp, f) = (Random.nextDouble, Random.nextDouble, Random.nextDouble)
  ("series %d %1.2f %1.2f %1.2f".format(i, phase, amp, f),
     new immutable.TreeMap[DateTime, Double]() ++
       (0 to 100).map(j => (now.plusMinutes(2 * j) -> amp * sin(phase + (j / (10 * f))))).toMap)
}

val lfs = (0 until 20).map(i => randomTimeSeries(i)).toList
val frame = new AxleFrame()
val vis = new Plot(lfs, true, title = Some("Random Waves"),
    xAxis=0.0, xAxisLabel=Some("time (t)"), yAxis=now, yAxisLabel=Some("a * sin(p + t/10f)"))

frame.add(vis)

