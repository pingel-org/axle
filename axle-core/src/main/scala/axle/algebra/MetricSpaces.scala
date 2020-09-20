package axle.algebra

import spire.algebra._
import spire.math._

trait ScalarDoubleSpace extends MetricSpace[Double, Double] {

  def distance(v: Double, w: Double): Double = spire.math.abs(v - w)
}

trait ScalarRealSpace extends MetricSpace[Real, Real] {

  def distance(v: Real, w: Real): Real = (v - w).abs()
}

trait RealTuple2Space extends MetricSpace[(Real, Real), Real] {

  def distance(v: (Real, Real), w: (Real, Real)): Real =
    ((v._1 - w._1) ** 2 + (v._2 - w._2) ** 2).sqrt()
}

trait IterableRealSpace extends MetricSpace[Iterable[Real], Real] {

  def distance(v: Iterable[Real], w: Iterable[Real]): Real = {
    assert(v.size == w.size)
    val parts = v.zip(w).map({ case (x, y) => (x - y) * (x - y) })
    val s = parts.reduce(implicitly[AdditiveMonoid[Real]].plus _)
    s.sqrt()
  }
}
