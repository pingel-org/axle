
import collection._
import axle.visualize._
import axle.quanta.Information._
import axle.InformationTheory._

val hl: Seq[(Double, Double)] = (1 to 99).map(i => (i / 100.0, coin(i / 100.0).entropy().conversion.get.getPayload.doubleValue))

val hm: SortedMap[Double, Double] = new immutable.TreeMap[Double, Double]() ++ hl.toMap

val lfs: Seq[(String, SortedMap[Double, Double])] = List(("h", hm))

val frame = new AxleFrame()

// xAxis = 0.0 *: bit

val vis = new Plot(lfs, connect = true, drawKey = false,
      xAxis = 0.0, xAxisLabel = Some("p(x='HEAD)"), yAxis = 0.0, yAxisLabel = Some("H"), title = Some("Entropy"))

frame.add(vis)
