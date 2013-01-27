package axle.algebra

trait Monoid[A] extends Semigroup[A] {

  def mzero(): A

}

object Monoid {

  //  def checkAxiom1[A](x: A)(implicit monoid: Monoid[A]): Boolean =
  //    (x |+| monoid.mzero) == x

  def checkAxiom1[A: Monoid](x: A): Boolean = {
    val monoid = implicitly[Monoid[A]]
    (x |+| monoid.mzero) === x
  }

  def checkAxiom2[A: Monoid](x: A): Boolean = {
    val monoid = implicitly[Monoid[A]]
    x === (x |+| monoid.mzero)
  }

  // associativity
  
  def checkAxiom3[A: Monoid](x: A, y: A, z: A): Boolean =
    ((x |+| y) |+| z) == (x |+| (y |+| z))

  implicit val intMonoid = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero(): Int = 0
  }

  val multMonoid = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a * b
    def mzero(): Int = 1
  }

  implicit val stringMonoid = new Monoid[String] {
    def mappend(a: String, b: String): String = a + b
    def mzero(): String = ""
  }

  // hyper log log, etc

}
