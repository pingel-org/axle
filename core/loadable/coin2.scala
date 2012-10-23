
object coin2O {

  import collection._
  import axle.stats._
  import axle.visualize._
  import axle.quanta.Information._
  import Plottable._

  val hm = new immutable.TreeMap[Double, UOM]() ++ (0 to 100).map(i => (i / 100.0, entropy(coin(i / 100.0)))).toMap

  val plot = Plot(List(("h", hm)),
    connect = true, drawKey = false,
    xAxis = 0.0 *: bit, xAxisLabel = Some("p(x='HEAD)"),
    yAxis = 0.0, yAxisLabel = Some("H"),
    title = Some("Entropy"))(DoublePlottable, InfoPlottable(bit))

  show(plot)

}
