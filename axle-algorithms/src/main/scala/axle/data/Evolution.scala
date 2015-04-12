package axle.data

import axle.quanta._
import spire.algebra.Module
import spire.implicits._

/**
 * http://en.wikipedia.org/wiki/Timeline_of_evolution
 *
 * Simple Cells -> Anatomically Modern Human
 */

case class Evolution()(implicit tc: TimeConverter[Double]) {

  import tc._

  val commonEra = frameOfReference

  private[this] def ce(t: UnittedQuantity[Time, Double]): (UnittedQuantity[Time, Double], UnittedQuantity[Time, Double]) =
    (commonEra.zero, t)

  lazy val simpleCellsAge = ce(-3.8 *: gy)

  lazy val multiCellularLifeAge = ce(-1d *: gy)

  lazy val fungiAge = ce(-560d *: my)

  lazy val classMammalAge = ce(-215d *: my)

  lazy val primateAge = ce(-60d *: my)

  lazy val australopithecusAge = ce(-4d *: my)

  lazy val modernHumanAge = ce(-200d *: ky)

}