object Rings {

  println("Welcome to the Scala worksheet")

  object lib {

    // A ring is an abelian group (group with commutative addition)

    // with multiplication that distributes over addition:
    //   x * (y + z) == x * y + x * z

    trait Ring[A] {
      def zero: A
      def plus(x: A, y: A): A
      def negate(x: A): A
      def minus(x: A, y: A): A = plus(x, negate(y))
      def one: A
      def times(x: A, y: A): A
    }

    object Ring {

      implicit object DoubleRing extends Ring[Double] {

        def zero = 0D

        def plus(x: Double, y: Double): Double = x + y

        def negate(x: Double): Double = -x

        def one: Double = 1D

        def times(x: Double, y: Double): Double = x * y
      }
    }

    def turn[R: Ring](p: (R, R), q: (R, R), r: (R, R)): R = {
      val ring = implicitly[Ring[R]]
      import ring._
      minus(
        times((minus(q._1, p._1)), minus(r._2, p._2)),
        times((minus(r._1, p._1)), minus(q._2, p._2)))
    }

  }

  import lib._

  object GrahamScan {

    def genPoints[R](n: Int)(gen: () => R): Seq[(R, R)] =
      (0 until n) map { i => (gen(), gen()) }

    // scan from left to right through 'sorted'
    // three at a time (p, q, r) throwing out any q if p->q->r forms a right turn
    def halfHull[R: Ring: Ordering](sorted: List[(R, R)]): Seq[(R, R)] = sorted match {
      case p :: q :: r :: rest => Nil
      case p :: q :: Nil => List(p, q)
      case p :: Nil => List(p)
      case Nil => Nil
    }

    def hull[R: Ring: Ordering](points: Seq[(R, R)]): Seq[(R, R)] = {
      val sorted = points.toList.sorted
      halfHull(sorted).drop(1) ++ halfHull(sorted.reverse).drop(1)
    }
  }

  import GrahamScan._

  hull(genPoints(1000)(util.Random.nextGaussian))

}