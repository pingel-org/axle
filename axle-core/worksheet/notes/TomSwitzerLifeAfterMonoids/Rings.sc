object Rings {

  println("Welcome to the Scala worksheet")

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

    def turn[R: Ring](p: (R, R), q: (R, R), r: (R, R)): R =
      (q._1 - p._1) * (r._2 - p._2) - (r._1 - p._1) * (q._2 - p._2)
  
  }

}