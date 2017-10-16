package axle.poly

import shapeless._

import spire.random.Generator

object Mixer2 extends Poly2 {

  implicit def caseTL[A, B <: HList] = at[(A, A), (Generator, B)] {
    case ((left, right), (gen, acc)) =>
      val choice: A = if (gen.nextBoolean()) left else right
      (gen, choice :: acc)
  }

}
