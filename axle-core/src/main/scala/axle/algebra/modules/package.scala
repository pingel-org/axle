package axle.algebra

import spire.algebra._
import spire.math._

package object modules {

  val rat = new spire.math.RationalAlgebra()
  val realAlgebra = new spire.math.RealAlgebra

  implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
  implicit val ringInt: CRing[Int] = spire.implicits.IntAlgebra
  implicit val fieldFloat: Field[Float] = spire.implicits.FloatAlgebra

  implicit val doubleIntModule: CModule[Double, Int] =
    new CModule[Double, Int] {

      def negate(x: Double): Double = fieldDouble.negate(x)

      def zero: Double = fieldDouble.zero

      def plus(x: Double, y: Double): Double = fieldDouble.plus(x, y)

      implicit def scalar: CRing[Int] = ringInt

      def timesl(s: Int, v: Double): Double = s * v
    }

  implicit val doubleDoubleModule: CModule[Double, Double] =
    new CModule[Double, Double] {

      def negate(x: Double): Double = fieldDouble.negate(x)

      def zero: Double = fieldDouble.zero

      def plus(x: Double, y: Double): Double = fieldDouble.plus(x, y)

      implicit def scalar: CRing[Double] = fieldDouble

      def timesl(s: Double, v: Double): Double = s * v

    }

  implicit val realDoubleModule: CModule[Real, Double] =
    new CModule[Real, Double] {

      def negate(x: Real): Real = realAlgebra.negate(x)

      def zero: Real = Real(0)

      def plus(x: Real, y: Real): Real = x + y

      implicit def scalar: CRing[Double] = fieldDouble

      def timesl(s: Double, v: Real): Real = s * v
    }

  implicit val realRationalModule: CModule[Real, Rational] =
    new CModule[Real, Rational] {

      def negate(x: Real): Real = realAlgebra.negate(x)

      def zero: Real = Real(0)

      def plus(x: Real, y: Real): Real = x + y

      implicit def scalar: CRing[Rational] = rat

      def timesl(s: Rational, v: Real): Real = s * v
    }

  implicit val doubleRationalModule: CModule[Double, Rational] =
    new CModule[Double, Rational] {

      def negate(x: Double): Double = fieldDouble.negate(x)

      def zero: Double = fieldDouble.zero

      def plus(x: Double, y: Double): Double = fieldDouble.plus(x, y)

      implicit def scalar: CRing[Rational] = rat

      def timesl(s: Rational, v: Double): Double = s.toDouble * v

    }

  implicit val floatRationalModule: CModule[Float, Rational] =
    new CModule[Float, Rational] {

      def negate(x: Float): Float = fieldFloat.negate(x)

      def zero: Float = fieldFloat.zero

      def plus(x: Float, y: Float): Float = fieldFloat.plus(x, y)

      implicit def scalar: CRing[Rational] = rat

      def timesl(s: Rational, v: Float): Float = s.toDouble.toFloat * v

    }

  implicit val floatDoubleModule: CModule[Float, Double] =
    new CModule[Float, Double] {

      def negate(x: Float): Float = fieldFloat.negate(x)

      def zero: Float = fieldFloat.zero

      def plus(x: Float, y: Float): Float = fieldFloat.plus(x, y)

      implicit def scalar: CRing[Double] = fieldDouble

      def timesl(s: Double, v: Float): Float = (s * v).toFloat

    }

  implicit val rationalDoubleModule: CModule[Rational, Double] =
    new CModule[Rational, Double] {

      def negate(x: Rational): Rational = rat.negate(x)

      def zero: Rational = rat.zero

      def plus(x: Rational, y: Rational): Rational = rat.plus(x, y)

      implicit def scalar: CRing[Double] = fieldDouble

      def timesl(s: Double, v: Rational): Rational = s * v

  }

  implicit val rationalRationalModule: CModule[Rational, Rational] =
    new CModule[Rational, Rational] {

      def negate(x: Rational): Rational = rat.negate(x)

      def zero: Rational = rat.zero

      def plus(x: Rational, y: Rational): Rational = rat.plus(x, y)

      implicit def scalar: CRing[Rational] = rat

      def timesl(s: Rational, v: Rational): Rational = s * v

  }

}
