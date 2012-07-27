
object coin2O {

  import collection._
  import axle.visualize.{ Plot, Plottable, AxleFrame }
  import Plottable._
  import axle.quanta.Information._
  import axle.Statistics._
  import axle.InformationTheory._

  val hm = new immutable.TreeMap[Double, UOM]() ++ (0 to 100).map(i => (i / 100.0, entropy(coin(i / 100.0)) )).toMap

  new AxleFrame().add(new Plot(List(("h", hm)),
    connect = true, drawKey = false,
    xAxis = 0.0 *: bit, xAxisLabel = Some("p(x='HEAD)"),
    yAxis = 0.0, yAxisLabel = Some("H"),
    title = Some("Entropy"))(DoublePlottable, new InfoPlottable(bit)))

}