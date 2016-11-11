package axle.data

import axle.quanta.VolumeConverter

case class Lakes()(implicit vc: VolumeConverter[Double]) {

  /**
   * http://en.wikipedia.org/wiki/Great_Lakes
   */
  lazy val greatLakesVolume = 22671d *: vc.km3

}
