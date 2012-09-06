
object coin2O {

  import collection._
  import axle.stats._
  import axle.quanta.Information._
  import axle.visualize._
  import Plottable._

  val hm = new immutable.TreeMap[Double, UOM]() ++ (1 to 99).map(i => (i / 100.0, entropy(coin(i / 100.0)))).toMap

  val plot = new Plot(List(("h", hm)),
    connect = true, drawKey = false,
    xAxis = 0.0 *: bit, xAxisLabel = Some("p(x='HEAD)"),
    yAxis = 0.0, yAxisLabel = Some("H"),
    title = Some("Entropy"))(DoublePlottable, new InfoPlottable(bit))

  show(plot)

}
