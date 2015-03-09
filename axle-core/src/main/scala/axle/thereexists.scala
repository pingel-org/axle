package axle

import spire.algebra.Bool
import scala.reflect.ClassTag
import axle.algebra.Functor
import axle.algebra.Aggregatable
import axle.syntax.functor._
import axle.syntax.aggregatable._

object thereexists {

  def apply[A, B: ClassTag, F[_]: Functor: Aggregatable](as: F[A])(predicate: A => B)(implicit ev: Bool[B]): B =
    as.map(predicate).aggregate(ev.zero)(ev.or, ev.or) //TODO short-circuit

}
