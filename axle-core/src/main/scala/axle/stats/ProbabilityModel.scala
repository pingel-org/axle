package axle.stats

/**
 * ProbabilityModel
 *
 * See http://www.stat.yale.edu/Courses/1997-98/101/probint.htm
 */

import cats.kernel.Order

import spire.random.Generator
import spire.random.Dist
import spire.algebra.Ring
import spire.algebra.Field

trait ProbabilityModel[M[_, _]] {

  def construct[A, V](variable: Variable[A], as: Iterable[A], f: A => V)(implicit ring: Ring[V]): M[A, V]

  def values[A](model: M[A, _]): IndexedSeq[A]

  def combine[A, V](modelsToProbabilities: Map[M[A, V], V])(implicit fieldV: Field[V]): M[A, V]

  def empty[A, V](variable: Variable[A])(implicit ringV: Ring[V]): M[A, V]

  def orientation[A, V](model: M[A, V]): Variable[A]

  def orient[A, B, V](model: M[A, V], newVariable: Variable[B])(implicit ringV: Ring[V]): M[B, V]

  def probabilityOf[A, V](model: M[A, V], a: A)(implicit fieldV: Field[V]): V

  def condition[A, V, G](model: M[A, V], given: CaseIs[G]): M[A, V]

  def observe[A, V](model: M[A, V], gen: Generator)(implicit spireDist: Dist[V], ringV: Ring[V], orderV: Order[V]): A

}
