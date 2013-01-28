package axle.algebra

trait Monoid[A] extends Semigroup[A] {

  def mzero(): A

}

object Monoid {

  //  def checkAxiom1[A](x: A)(implicit monoid: Monoid[A]): Boolean =
  //    (x |+| monoid.mzero) == x

  def checkLeftZero[A: Monoid](x: A): Boolean = {
    val monoid = implicitly[Monoid[A]]
    (monoid.mzero |+| x) === x
  }

  def checkRightZero[A: Monoid](x: A): Boolean = {
    val monoid = implicitly[Monoid[A]]
    (x |+| monoid.mzero) === x
  }

  // associativity

  def checkAssociativity[A: Monoid](x: A, y: A, z: A): Boolean =
    ((x |+| y) |+| z) == (x |+| (y |+| z))

  implicit val intMonoid = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero(): Int = 0
  }

  implicit val doubleMonoid = new Monoid[Double] {
    def mappend(a: Double, b: Double): Double = a + b
    def mzero(): Double = 0
  }

  val multMonoid = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a * b
    def mzero(): Int = 1
  }

  implicit val stringMonoid = new Monoid[String] {
    def mappend(a: String, b: String): String = a + b
    def mzero(): String = ""
  }

  implicit def listMonoid[A] = new Monoid[List[A]] {
    def mappend(a: List[A], b: List[A]): List[A] = a ++ b
    def mzero(): List[A] = List[A]()
  }

  implicit def tuple2Monoid[A: Monoid, B: Monoid]() = new Monoid[(A, B)] {
    def mappend(x: (A, B), y: (A, B)): (A, B) = (x._1 |+| y._1, x._2 |+| y._2)
    def mzero(): (A, B) = {
      val aMonoid = implicitly[Monoid[A]]
      val bMonoid = implicitly[Monoid[B]]
      (aMonoid.mzero(), bMonoid.mzero())
    }
  }

  implicit def tuple3Monoid[A: Monoid, B: Monoid, C: Monoid]() = new Monoid[(A, B, C)] {

    def mappend(x: (A, B, C), y: (A, B, C)): (A, B, C) =
      (x._1 |+| y._1, x._2 |+| y._2, x._3 |+| y._3)

    def mzero(): (A, B, C) = {
      val aMonoid = implicitly[Monoid[A]]
      val bMonoid = implicitly[Monoid[B]]
      val cMonoid = implicitly[Monoid[C]]
      (aMonoid.mzero(), bMonoid.mzero(), cMonoid.mzero())
    }
  }

  implicit def tuple4Monoid[A: Monoid, B: Monoid, C: Monoid, D: Monoid]() = new Monoid[(A, B, C, D)] {

    def mappend(x: (A, B, C, D), y: (A, B, C, D)): (A, B, C, D) =
      (x._1 |+| y._1, x._2 |+| y._2, x._3 |+| y._3, x._4 |+| y._4)

    def mzero(): (A, B, C, D) = {
      val aMonoid = implicitly[Monoid[A]]
      val bMonoid = implicitly[Monoid[B]]
      val cMonoid = implicitly[Monoid[C]]
      val dMonoid = implicitly[Monoid[D]]
      (aMonoid.mzero(), bMonoid.mzero(), cMonoid.mzero(), dMonoid.mzero())
    }
  }

  // hyper log log, etc

}
