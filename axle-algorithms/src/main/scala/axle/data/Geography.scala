package axle.data

import axle.quanta.DistanceConverter

case class Geography()(implicit dc: DistanceConverter[Double]) {

  /**
   * http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html
   */
  lazy val newYorkToLosAngeles = 2443.79 *: dc.mile

}