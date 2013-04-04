
package axle

import spire.math._
import spire.algebra.{ Monoid, MetricSpace }
// http://en.wikipedia.org/wiki/Algebraic_structure

package object algebra {

  def ∅[T](implicit m: Monoid[T]): T = m.id

  implicit def toIdent[A](a: A): Identity[A] = new Identity[A] {
    lazy val value = a
  }

  implicit def toMA[M[_], A](ma: M[A]): MA[M, A] = new MA[M, A] {
    val value = ma
  }

  implicit def enrichMetricSpace[T: Manifest](space: MetricSpace[T, Double]) = new EnrichedMetricSpace(space)

}
