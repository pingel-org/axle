package axle.data

import axle.quanta.PowerConverter
import axle.quanta.SpeedConverter

case class Automotive()(implicit pc: PowerConverter[Double], sc: SpeedConverter[Double]) {

  import pc._
  import sc._

  /**
   * http://en.wikipedia.org/wiki/Ford_Mustang
   */
  lazy val engine2012MustangGT = 420d *: horsepower

  /**
   * Speed
   */

  lazy val speedLimit = 65d *: mph

}
