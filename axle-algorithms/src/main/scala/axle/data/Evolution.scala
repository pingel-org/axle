package axle.data

import axle.quanta._
import spire.implicits._
import axle.algebra.Vec

/**
 * http://en.wikipedia.org/wiki/Timeline_of_evolution
 *
 * Simple Cells -> Anatomically Modern Human
 */

case class Event[N, V[_]: Vec, E](timestamp: V[UnittedQuantity[Time, N]], e: E)

case class Evolution()(implicit tc: TimeConverter[Double]) {

  type V[T] = (T, T)

  import tc._

  val commonEra = frameOfReference

  private[this] def ce(t: UnittedQuantity[Time, Double]): (UnittedQuantity[Time, Double], UnittedQuantity[Time, Double]) =
    (commonEra.zero, t)

  def toEvent(ts: (UnittedQuantity[Time, Double], String)) =
    Event[Double, V, String](ce(ts._1), ts._2)

  lazy val history: List[Event[Double, V, String]] =
    List(
      (-3.8 *: gy, "Simple cells"),
      (-1d *: gy, "Multi-cellular life"),
      (-560d *: my, "Fungi"),
      (-215d *: my, "Mammals"),
      (-60d *: my, "Primate"),
      (-4d *: my, "Australopithecus"),
      (-200d *: ky, "Modern Humans")).map(toEvent)

}