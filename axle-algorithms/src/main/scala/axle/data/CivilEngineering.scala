package axle.data

import axle.quanta.PowerConverter

case class CivilEngineering()(implicit pc: PowerConverter[Double]) {

  import pc._

  /**
   * http://en.wikipedia.org/wiki/Hoover_Dam
   */

  lazy val hooverDam = 2080d *: megawatt

  /**
   * Power
   */

  def lightBulb(implicit pc: PowerConverter[Double]) =
    60d *: pc.watt

}