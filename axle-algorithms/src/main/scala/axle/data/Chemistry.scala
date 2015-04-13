package axle.data

import axle.quanta._

case class Chemistry()(implicit tc: TemperatureConverter[Double]) {

  import tc._

  lazy val waterBoilingPoint = 100d *: celsius

  lazy val waterFreezingPoint = 0d *: celsius

  lazy val absoluteZero = 0d *: kelvin

}