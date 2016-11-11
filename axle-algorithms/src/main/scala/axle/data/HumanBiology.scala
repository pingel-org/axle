package axle.data

import axle.quanta.MassConverter
import axle.quanta.TimeConverter

case class HumanBiology()(implicit mc: MassConverter[Double], tc: TimeConverter[Double]) {

  import mc._
  import tc._

  /**
   *  http://en.wikipedia.org/wiki/Body_weight
   */
  def averageBodyMassOfAdultMale = 86.6 *: kilogram

  /**
   * http://en.wikipedia.org/wiki/Life_expectancy
   */
  def globalLifeExpectancy = 67.2 *: year

}
