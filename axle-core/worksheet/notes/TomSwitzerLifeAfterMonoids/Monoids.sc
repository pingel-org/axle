
object TomSwitzerLifeAfterMonoids {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // http://www.youtube.com/watch?v=xO9AoZNSOH4

  // Monoids

  trait MonoidZ[A] {
    def id: A
    def op(x: A, y: A): A
  }

  object MonoidZ {

    implicit object LongMonoidZ extends MonoidZ[Long] {
      def op(a: Long, b: Long): Long = a + b
      def id: Long = 0L
    }
  }

  // associative: (x |+| y) |+| z == x |+| (y |+| z)
  // identify: x |+| id == id |+| x == x

  // concatenation of lists, strings, etc
  // set union and sometimes intersection
  // "numeric" addition and multiplication
  // any semigroup lifted from Option
  // all sorts of fancy data structures

}