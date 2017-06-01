package axle.data

import axle.quanta.Distance
import axle.quanta.DistanceConverter
import axle.quanta.Flow
import axle.quanta.FlowConverter
import axle.quanta.UnittedQuantity

case class River(
  name: String,
  length: UnittedQuantity[Distance, Double],
  averageDischarge: UnittedQuantity[Flow, Double],
  wikpediaUrl: String)

/**
 * Rivers
 *
 * Source: https://en.wikipedia.org/wiki/List_of_rivers_by_length
 *
 */

case class Rivers()(implicit dc: DistanceConverter[Double], fc: FlowConverter[Double]) {

  import dc._
  import fc._

  lazy val nile =
    River(
      "Nile",
      6853d *: km,
      2830d *: m3s,
      "https://en.wikipedia.org/wiki/Nile")

  lazy val mississippi =
    River(
      "Mississippi",
      6027d *: km, // Note: Not for "Mississippi–Missouri–Jefferson", which is longer
      16792d *: m3s,
      "https://en.wikipedia.org/wiki/Mississippi_River")

  /**
   * http://en.wikipedia.org/wiki/Niagara_Falls
   */
  lazy val niagaraFalls = 1834d *: m3s

}
