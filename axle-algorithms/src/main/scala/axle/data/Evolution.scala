package axle.data

import axle.quanta._

/**
 * http://en.wikipedia.org/wiki/Timeline_of_evolution
 *
 * Simple Cells -> Anatomically Modern Human
 */

case class Evolution()(implicit tc: TimeConverter[Double]) {

  import tc._

  lazy val simpleCellsAge = 3.8 *: gy

  lazy val multiCellularLifeAge = 1 *: gy

  lazy val fungiAge = 560 *: my

  lazy val classMammalAge = 215 *: my

  lazy val primateAge = 60 *: my

  lazy val australopithecusAge = 4 *: my

  lazy val modernHumanAge = 200 *: ky

}