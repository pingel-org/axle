package axle

import axle.quanta._
import spire.implicits._

package object data {

  /**
   *  Mass
   */

  /**
   *  http://en.wikipedia.org/wiki/Body_weight
   */
  def averageBodyMassOfAdultMale(implicit mc: MassConverter[Double]) =
    86.6 *: mc.kilogram

  /**
   * Distance
   */

  /**
   * http://www.mapcrow.info/Distance_between_New_York_US_and_Los_Angeles_US.html
   */
  def newYorkToLosAngeles(implicit dc: DistanceConverter[Double]) =
    2443.79 *: dc.mile

  /**
   * Energy
   */

  /**
   * http://en.wikipedia.org/wiki/Castle_Bravo
   */
  def castleBravo(implicit ec: EnergyConverter[Double]) =
    15d *: ec.megaton

  /**
   * Power
   */

  def lightBulb(implicit pc: PowerConverter[Double]) =
    60d *: pc.watt

  /**
   * http://en.wikipedia.org/wiki/Hoover_Dam
   */
  def hooverDam(implicit pc: PowerConverter[Double]) =
    2080d *: pc.megawatt

  /**
   * http://en.wikipedia.org/wiki/Ford_Mustang
   */
  def engine2012MustangGT(implicit pc: PowerConverter[Double]) =
    420d *: pc.horsepower

  /**
   * Speed
   */

  def speedLimit(implicit sc: SpeedConverter[Double]) =
    65d *: sc.mph

  /**
   * Time
   */

  /**
   * http://en.wikipedia.org/wiki/Life_expectancy
   */
  def globalLifeExpectancy(implicit tc: TimeConverter[Double]) =
    67.2 *: tc.year

}