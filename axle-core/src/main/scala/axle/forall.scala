package axle

import axle.algebra.Aggregatable
import axle.algebra.Functor
import axle.syntax.functor._
import axle.syntax.aggregatable._
import spire.algebra.BooleanAlgebra
import scala.reflect.ClassTag

object forall {

  def apply[A, B: ClassTag, F[_]: Functor: Aggregatable](as: F[A])(predicate: A => B)(implicit ev: BooleanAlgebra[B]): B =
    as.map(predicate).aggregate(ev.one)(ev.and, ev.and) // TODO short-circuit

}