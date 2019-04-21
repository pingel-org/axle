package axle

import spire.algebra._

package object quantumcircuit {
  implicit class EnrichedVector[T](vector: Vector[T]) {

    def ⊗(other: Vector[T])(implicit multT: MultiplicativeSemigroup[T]): Vector[T] = 
      for {
        x <- vector
        y <- other
      } yield multT.times(x, y)

  }

}