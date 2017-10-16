package axle.poly

import shapeless._

import spire.random.Generator

object Mutator1 extends Poly1 {

  implicit def caseTuple[T] = at[(T, T)](t =>
    if (0.1 < 0.03) t._2 else t._1
  )

}

object Mutator2 extends Poly2 {

  implicit def caseTL[A, B <: HList] = at[(A, A), (Generator, B)] {
    case ((random, g), (gen, acc)) =>
      val choice: A = if (gen.nextDouble() < 0.03) random else g
      (gen, choice :: acc)
  }

}