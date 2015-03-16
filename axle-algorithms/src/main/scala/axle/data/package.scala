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

  // Distant Past:
  /**
   * http://en.wikipedia.org/wiki/Age_of_the_Universe
   */
  def universeAge(implicit tc: TimeConverter[Double]) =
    13.7 *: tc.gy

  def earthAge(implicit tc: TimeConverter[Double]) =
    4.54 *: tc.gy // Some("earth age"), None, Some("http://en.wikipedia.org/wiki/Age_of_the_Earth"))

  /**
   * http://en.wikipedia.org/wiki/Timeline_of_evolution
   *
   * Simple Cells -> Anatomically Modern Human
   */
  def simpleCellsAge(implicit tc: TimeConverter[Double]) =
    3.8 *: tc.gy

  def multiCellularLifeAge(implicit tc: TimeConverter[Double]) =
    1 *: tc.gy

  def fungiAge(implicit tc: TimeConverter[Double]) =
    560 *: tc.my

  def classMammalAge(implicit tc: TimeConverter[Double]) =
    215 *: tc.my

  def primateAge(implicit tc: TimeConverter[Double]) =
    60 *: tc.my

  def australopithecusAge(implicit tc: TimeConverter[Double]) =
    4 *: tc.my

  def modernHumanAge(implicit tc: TimeConverter[Double]) =
    200 *: tc.ky

  /**
   * Volume
   */

  /**
   * http://en.wikipedia.org/wiki/Great_Lakes
   */
  def greatLakesVolume(implicit vc: VolumeConverter[Double]) =
    22671d *: vc.km3

  /**
   * http://en.wikipedia.org/wiki/Wine_bottle
   *
   * The wine bottle quantities are arguably unit of measurement themselves.
   */
  def wineBottle(implicit vc: VolumeConverter[Double]) =
    750d *: vc.milliliter

  def magnum(implicit vc: VolumeConverter[Double]) =
    2d *: wineBottle

  def jeroboam(implicit vc: VolumeConverter[Double]) =
    4d *: wineBottle

  def rehoboam(implicit vc: VolumeConverter[Double]) =
    6d *: wineBottle

  def methuselah(implicit vc: VolumeConverter[Double]) =
    8d *: wineBottle

  def salmanazar(implicit vc: VolumeConverter[Double]) =
    12d *: wineBottle

  def balthazar(implicit vc: VolumeConverter[Double]) =
    16d *: wineBottle

  def nebuchadnezzar(implicit vc: VolumeConverter[Double]) =
    20d *: wineBottle

  /**
   * Flow
   */

  /**
   * http://en.wikipedia.org/wiki/Niagara_Falls
   */
  def niagaraFalls(implicit fc: FlowConverter[Double]) =
    1834d *: fc.m3s

}