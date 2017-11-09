package axle.poly

import shapeless._

import spire.random.Generator

object combineR extends Poly2 {

  implicit def ci = at[Int, Int]((i, acc) => acc+i)

  implicit def cs = at[String, Int]((s, acc) => acc+s.length)

  implicit def cb = at[Boolean, Int]((b, acc) => acc+(if(b) 1 else 0))
}

object Mutator2 extends Poly2 {

  implicit def caseTL[A, B <: HList] = at[(A, A), (Generator, B)] {
    case ((random, g), (gen, acc)) =>
      val choice: A = if (gen.nextDouble() < 0.03) random else g
      (gen, choice :: acc)
  }

  implicit def caseX[A, B <: HList] = at[A :: A :: HNil, (Generator, B)] {
    case (randomG, (gen, acc)) =>
      val random = randomG.head
      val g = randomG.tail.head
      val choice: A = if (gen.nextDouble() < 0.03) random else g
      (gen, choice :: acc)
  }

}