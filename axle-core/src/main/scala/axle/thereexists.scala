package axle

import spire.algebra.Bool
import axle.algebra.Functor
import axle.algebra.Aggregatable
import axle.syntax.functor._
import axle.syntax.aggregatable._

object thereexists {

  def apply[A, B, F, G](
    as: F)(
      predicate: A => B)(
        implicit bool: Bool[B],
        functor: Functor[F, A, B, G],
        agg: Aggregatable[G, B, B]): B =
    as.map(predicate).aggregate(bool.zero)(bool.or, bool.or) //TODO short-circuit

}
