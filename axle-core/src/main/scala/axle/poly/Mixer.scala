package axle.poly

import shapeless._

import spire.random.Generator

object Mixer1 extends Poly1 {

  implicit def caseTuple[T] = at[(T, T)](t =>
    t._1
    // if (gen.nextBoolean) t._2 else t._1
  )

}

object Mixer2 extends Poly2 {

  implicit def caseTL[A, B <: HList] = at[(A, A), (Generator, B)] {
    case ((left, right), (gen, acc)) =>
      val choice: A = if (gen.nextBoolean()) left else right
      (gen, choice :: acc)
  }

}
/*
object Demo {

  val m = List(1, 8, 11)
  val f = List(2, 3, 12)
  val r = List(7, 7, 7)
  val gen: Generator = ???

  type GA[T] = (Generator, List[T])

  def mix[T](current: (T, T), genAcc: GA[T]): GA[T] = {
    val (gen, acc) = genAcc
    val choice = if (gen.nextBoolean()) current._1 else current._2
    (gen, choice :: acc)
  }

  def mutate[T](current: (T, T), genAcc: GA[T]): GA[T] = {
    val (gen, acc) = genAcc
    val choice = if (gen.nextDouble() < 0.03) current._1 else current._2
    (gen, choice :: acc)
  }

  // r.zip(m.zip(f)).foldRight((gen, List.empty[Int]))(mate)

  val mixed = m.zip(f).foldRight((gen, List.empty[Int]))(mix)._2
  val mated = r.zip(mixed).foldRight((gen, List.empty[Int]))(mutate)._2

}
*/
