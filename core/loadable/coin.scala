
object coinO {

  import collection._
  import axle.visualize._
  import axle.stats._
  import axle.quanta.Information._

  val hm = new immutable.TreeMap[Double, Double]() ++ (1 to 99).map(i => (i / 100.0, entropy(coin(i / 100.0)).conversion.get.payload.doubleValue)).toMap

  val plot = Plot(List(("h", hm)), connect = true, drawKey = false,
    xAxis = 0.0, xAxisLabel = Some("p(x='HEAD)"), yAxis = 0.0, yAxisLabel = Some("H"), title = Some("Entropy"))

  show(plot)

}
