package axle

import spire.algebra.MetricSpace
import spire.algebra.Module
import spire.algebra.Rng
import spire.implicits.multiplicativeSemigroupOps
import spire.math.Rational
import spire.math.Rational.apply
import spire.math.Real
import spire.math.Real.apply

package object algebra {

  type ZipperK1[M[_], A, B] = Zipper[M[A], A, M[B], B, M[(A, B)]]

  type IndexedK1[M[_], I, N] = Indexed[M[N], I, N]

  type FiniteK1[M[_], N, I] = Finite[M[N], I]

  type FunctorK1[M[_], A, B] = Functor[M[A], A, B, M[B]]

  type AggregatableK1[M[_], A, B] = Aggregatable[M[A], A, B]

  implicit def wrappedStringSpace[N](
    implicit iscSpace: MetricSpace[IndexedSeq[Char], N]) =
    new MetricSpace[String, N] {
      def distance(s1: String, s2: String): N = iscSpace.distance(s1, s2)
    }

  implicit val rationalDoubleMetricSpace: MetricSpace[Rational, Double] =
    new MetricSpace[Rational, Double] {

      def distance(v: Rational, w: Rational): Double = (v.toDouble - w.toDouble).abs
    }

  implicit val realDoubleMetricSpace: MetricSpace[Real, Double] =
    new MetricSpace[Real, Double] {

      def distance(v: Real, w: Real): Double = (v.toDouble - w.toDouble).abs
    }

  implicit val doubleDoubleMetricSpace: MetricSpace[Double, Double] =
    new MetricSpace[Double, Double] {

      def distance(v: Double, w: Double): Double = (v - w).abs
    }

  //import spire.implicits._
  //import spire.math._

  //  implicit val rationalRng: Rng[Rational] = new Rng[Rational] {
  //
  //    val rat = new spire.math.RationalAlgebra()
  //
  //    def negate(x: Rational): Rational = rat.negate(x)
  //
  //    def zero: Rational = rat.zero
  //
  //    def plus(x: Rational, y: Rational): Rational = rat.plus(x, y)
  //
  //    def times(x: Rational, y: Rational): Rational = rat.times(x, y)
  //  }

  object modules {

    val rat = new spire.math.RationalAlgebra()

    import spire.implicits.DoubleAlgebra
    import spire.implicits.IntAlgebra

    implicit val doubleIntModule: Module[Double, Int] =
      new Module[Double, Int] {

        def negate(x: Double): Double = DoubleAlgebra.negate(x)

        def zero: Double = DoubleAlgebra.zero

        def plus(x: Double, y: Double): Double = DoubleAlgebra.plus(x, y)

        implicit def scalar: Rng[Int] = IntAlgebra

        def timesl(r: Int, v: Double): Double = r * v

      }

    implicit val doubleDoubleModule: Module[Double, Double] =
      new Module[Double, Double] {

        def negate(x: Double): Double = DoubleAlgebra.negate(x)

        def zero: Double = DoubleAlgebra.zero

        def plus(x: Double, y: Double): Double = DoubleAlgebra.plus(x, y)

        implicit def scalar: Rng[Double] = DoubleAlgebra

        def timesl(r: Double, v: Double): Double = r * v

      }

    implicit val realDoubleModule: Module[Real, Double] =
      new Module[Real, Double] {

        def negate(x: Real): Real = -x

        def zero: Real = Real(0)

        def plus(x: Real, y: Real): Real = x + y

        implicit def scalar: Rng[Double] = DoubleAlgebra

        def timesl(r: Double, v: Real): Real = r * v
      }

    implicit val realRationalModule: Module[Real, Rational] =
      new Module[Real, Rational] {

        def negate(x: Real): Real = -x

        def zero: Real = Real(0)

        def plus(x: Real, y: Real): Real = x + y

        implicit def scalar: Rng[Rational] = Rng[Rational]

        def timesl(r: Rational, v: Real): Real = r * v
      }

    implicit val doubleRationalModule: Module[Double, Rational] = new Module[Double, Rational] {

      def negate(x: Double): Double = DoubleAlgebra.negate(x)

      def zero: Double = DoubleAlgebra.zero

      def plus(x: Double, y: Double): Double = DoubleAlgebra.plus(x, y)

      implicit def scalar: Rng[Rational] = rat

      def timesl(r: Rational, v: Double): Double = r.toDouble * v

    }

    implicit val floatRationalModule: Module[Float, Rational] = new Module[Float, Rational] {

      import spire.implicits.FloatAlgebra

      def negate(x: Float): Float = FloatAlgebra.negate(x)

      def zero: Float = FloatAlgebra.zero

      def plus(x: Float, y: Float): Float = FloatAlgebra.plus(x, y)

      implicit def scalar: Rng[Rational] = rat

      def timesl(r: Rational, v: Float): Float = r.toDouble.toFloat * v

    }

    implicit val floatDoubleModule: Module[Float, Double] =
      new Module[Float, Double] {

        import spire.implicits.FloatAlgebra

        def negate(x: Float): Float = FloatAlgebra.negate(x)

        def zero: Float = FloatAlgebra.zero

        def plus(x: Float, y: Float): Float = FloatAlgebra.plus(x, y)

        implicit def scalar: Rng[Double] = DoubleAlgebra

        def timesl(r: Double, v: Float): Float = (r * v).toFloat

      }

    implicit val rationalDoubleModule: Module[Rational, Double] = new Module[Rational, Double] {

      def negate(x: Rational): Rational = rat.negate(x)

      def zero: Rational = rat.zero

      def plus(x: Rational, y: Rational): Rational = rat.plus(x, y)

      implicit def scalar: Rng[Double] = DoubleAlgebra

      def timesl(r: Double, v: Rational): Rational = r * v

    }

    implicit val rationalRationalModule: Module[Rational, Rational] = new Module[Rational, Rational] {

      def negate(x: Rational): Rational = rat.negate(x)

      def zero: Rational = rat.zero

      def plus(x: Rational, y: Rational): Rational = rat.plus(x, y)

      implicit def scalar: Rng[Rational] = rat

      def timesl(r: Rational, v: Rational): Rational = r * v

    }

  }
}
