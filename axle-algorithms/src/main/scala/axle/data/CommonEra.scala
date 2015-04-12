package axle.data

import axle.quanta.Time
import axle.quanta.TimeConverter
import axle.quanta.UnittedQuantity
import spire.algebra.AdditiveMonoid
import spire.algebra.Module

/**
 * TODO the CommonEra itself defines a Module
 */

case class CommonEra[N: AdditiveMonoid]()(implicit tc: TimeConverter[N]) {

  val am = implicitly[AdditiveMonoid[N]]

  import tc._

  lazy val _zero: UnittedQuantity[Time, N] = am.zero *: year

  //  def instant(t: UnittedQuantity[Time, N]): Vector1D[Time, N] =
  //    Vector1D[Time, N](zero, t)
}

