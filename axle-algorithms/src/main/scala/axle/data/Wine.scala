package axle.data

import axle.quanta._
import spire.implicits._

/**
 * http://en.wikipedia.org/wiki/Wine_bottle
 *
 */

case class Wine()(implicit vc: VolumeConverter[Double]) {

  import vc._

  lazy val wineBottle = 750d *: milliliter

  lazy val magnum = 2d *: wineBottle

  lazy val jeroboam = 4d *: wineBottle

  lazy val rehoboam = 6d *: wineBottle

  lazy val methuselah = 8d *: wineBottle

  lazy val salmanazar = 12d *: wineBottle

  lazy val balthazar = 16d *: wineBottle

  lazy val nebuchadnezzar = 20d *: wineBottle

}