
import collection._
import axle.visualize.{ Plot, Plottable, AxleFrame }
import Plottable._
import axle.quanta.Information
import Information._
import axle.InformationTheory._

val hm: SortedMap[Double, UOM] = new immutable.TreeMap[Double, UOM]() ++ (1 to 99).map(i => (i / 100.0, coin(i / 100.0).entropy())).toMap

val ip = InfoPlottable.asInstanceOf[Plottable[Information.UOM]]
// TODO remove cast
// TODO generalize QuantumPlottable
// TODO divide by zero issue with xAxis = 0.0 *: bit

new AxleFrame().add(new Plot(List(("h", hm)), connect = true, drawKey = false,
			     xAxis = 0.001 *: bit, xAxisLabel = Some("p(x='HEAD)"), yAxis = 0.0, yAxisLabel = Some("H"),
			     title = Some("Entropy"))(DoublePlottable, ip))
