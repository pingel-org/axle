package axle

import concurrent.duration._
import spire.implicits._
import monix.reactive._
import monix.execution.Scheduler
import axle.quanta.UnittedQuantity
import axle.quanta.Time
import axle.quanta.TimeConverter

package object reactive {

  def intervalScan[D](
    initialValue: D,
    f: D => D,
    interval: UnittedQuantity[Time, Double])(
      implicit scheduler: Scheduler,
      tc: TimeConverter[Double]): Observable[D] = {

    Observable
      .interval(((interval in tc.millisecond).magnitude).millis)
      .scan(initialValue)({ case (oldD, _) => f(oldD) })
  }

}