package axle.data

import axle.quanta.EnergyConverter

case class Military()(implicit ec: EnergyConverter[Double]) {

  import ec._

  /**
   * http://en.wikipedia.org/wiki/Castle_Bravo
   */
  lazy val castleBravo = 15d *: megaton

}