package axle.data

import axle.quanta._

/**
 * http://en.wikipedia.org/wiki/Timeline_of_evolution
 *
 * Simple Cells -> Anatomically Modern Human
 */

case class Evolution()(implicit tc: TimeConverter[Double]) {

  import tc._

  def commonEra(t: Any): Unit = ???

  lazy val simpleCellsAge = commonEra(-3.8 *: gy)

  lazy val multiCellularLifeAge = commonEra(1d *: gy)

  lazy val fungiAge = commonEra(560d *: my)

  lazy val classMammalAge = commonEra(215d *: my)

  lazy val primateAge = commonEra(60d *: my)

  lazy val australopithecusAge = commonEra(4d *: my)

  lazy val modernHumanAge = commonEra(200d *: ky)

}