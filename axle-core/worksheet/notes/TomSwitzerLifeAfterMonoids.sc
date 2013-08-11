
object TomSwitzerLifeAfterMonoids {

  println("Welcome to the Scala worksheet")

  // http://www.youtube.com/watch?v=xO9AoZNSOH4

  // Monoids

  trait MonoidX[A] {
    def id: A
    def op(x: A, y: A): A
  }

  // associative: (x |+| y) |+| z == x |+| (y |+| z)
  // identify: x |+| id == id |+| x == x

  // concatenation of lists, strings, etc
  // set union and sometimes intersection
  // "numeric" addition and multiplication
  // any semigroup lifted from Option
  // all sorts of fancy data structures

  // Groups

  // A monoid with symmetry!

  trait Group[A] extends MonoidX[A] {
    def inverse(a: A): A
  }

  // a |+| a.inverse = a.inverse |+| a == 0

  // Examples:
  // addition with Int, Double, BigInt, etc
  // Multiplication with Double, BigDecimal, etc
  // Set of permutations
  // Rubik's cube

  // log example
  // * We want a "catalogue" (add and remove items; stored in append-only db)
  // * The goal: summarize the items in the catalogue
  // * Can groups help?

  //trait Catalogue[A] {
  //  def reduce0[B: MonoidX](f: Event[A] => B): B
  //}

  sealed trait Event[A]
  case class Add[A](item: A) extends Event[A]
  case class Remove[A](item: A) extends Event[A]

  //def count[A](cat: Catalogue[A]): Long =
  //  cat.reduce0[Long]({
  //    case Add(_) => 1L
  //    case Remove(_) => -1L
  //  })

  // abstract over the symmetry

  trait Catalogue[A] {

    def reduce0[B: MonoidX](f: Event[A] => B): B

    def reduce[A, B: Group](f: A => B): B = reduce0({
      case Add(a) => f(a)
      case Remove(a) => f(a).inverse
    })(Group[B])

  }


  // Rings

  // Fields

}