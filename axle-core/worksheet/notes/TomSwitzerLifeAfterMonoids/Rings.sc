object Rings {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  object lib {

    // A ring is an abelian group (+) (commutative addition)

    // with multiplication (*) that distributes:
    //   x * (y + z) == x * y + x * z

    trait Ring[A] {
      def zero: A
      def plus(x: A, y: A): A
      def negate(x: A): A
      def minus(x: A, y: A): A = plus(x, negate(y))
      def one: A
      def times(x: A, y: A): A
    }

    def turn[R: Ring](p: (R, R), q: (R, R), r: (R, R)): R = {
      val ring = implicitly[Ring[R]]
      import ring._
      minus(
        times((minus(q._1, p._1)), minus(r._2, p._2)),
        times((minus(r._1, p._1)), minus(q._2, p._2)))
    }

  }

}