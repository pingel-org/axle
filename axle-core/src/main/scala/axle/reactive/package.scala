package axle

import concurrent.duration._

import monix.reactive._

import spire.algebra._

import axle.quanta.UnittedQuantity
import axle.quanta.Time
import axle.quanta.TimeConverter

package object reactive {

  implicit val mmDouble: MultiplicativeMonoid[Double] = spire.implicits.DoubleAlgebra

  def intervalScan[D](
    initialValue: D,
    f:            D => D,
    interval:     UnittedQuantity[Time, Double])(
    implicit
    tc: TimeConverter[Double]): Observable[D] = {

    Observable
      .interval(((interval in tc.millisecond).magnitude).millis)
      .scan(initialValue)({ case (oldD, _) => f(oldD) })
  }

}