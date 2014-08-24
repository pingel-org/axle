
package axle

import java.math.BigDecimal
import spire.math.Rational
import spire.algebra.MetricSpace
import spire.algebra.Field

package object quanta {

  implicit val rationalDoubleMetricSpace: MetricSpace[Rational, Double] = new MetricSpace[Rational, Double] {

    def distance(v: Rational, w: Rational): Double = (v.toDouble - w.toDouble).abs

  }

  object Experiment {

    import spire.math._
    import spire.algebra._
    import spire.implicits._
    // import spire.syntax._
    import scala.language.reflectiveCalls

    sealed trait Quantum
    class Distance extends Quantum
    class Time extends Quantum

    trait QT[Q <: Quantum] { self =>

      implicit def modulizeThisQT[N: Field] = modulize[Q, N](self, implicitly[Field[N]])
      
      def canonicalBase[N: Field]: Quantity[Q, N]
    }

    case class Quantity[Q <: Quantum, N: Field](magnitude: N, unitOpt: Option[Quantity[Q, N]] = None)(implicit qt: QT[Q]) {

      implicit val module = modulize[Q, N]

      // TODO: avoid having to re-define this syntax
      def *(that: N): Quantity[Q, N] = module.timesr(this, that)

      def in(newUnit: Quantity[Q, N]): Quantity[Q, N] = ???

      def +(that: Quantity[Q, N]): Quantity[Q, N] = module.plus(this, that)

      def *:(n: N): Quantity[Q, N] =
        if (unitOpt.isDefined) {
          new Quantity(magnitude * n, unitOpt)
        } else {
          new Quantity(n, Some(this))
        }

    }

    implicit def modulize[Q <: Quantum, N](implicit qt: QT[Q], field: Field[N]) = new Module[Quantity[Q, N], N] {

      // from AdditiveGroup
      def negate(x: Quantity[Q, N]): Quantity[Q, N] = new Quantity(-x.magnitude, x.unitOpt)

      // from AdditiveMonoid
      def zero: Quantity[Q, N] = new Quantity(field.zero, Some(qt.canonicalBase))

      // from AdditiveSemigroup
      def plus(x: Quantity[Q, N], y: Quantity[Q, N]): Quantity[Q, N] = ???

      // from Module
      implicit def scalar: Rng[N] = ???

      def timesl(r: N, v: Quantity[Q, N]): Quantity[Q, N] = new Quantity(v.magnitude * r, v.unitOpt)

    }

    object QT {

      implicit val distance = new QT[Distance] { qtd: QT[Distance] =>

        def meter[N: Field]: Quantity[Distance, N] = new Quantity[Distance, N](implicitly[Field[N]].one, None)(implicitly[Field[N]], qtd)

        def canonicalBase[N: Field]: Quantity[Distance, N] = meter[N]
      }

      implicit val time = new QT[Time] { qtt: QT[Time] =>

        def second[N: Field]: Quantity[Time, N] = new Quantity(implicitly[Field[N]].one, None)(implicitly[Field[N]], qtt)

        // TODO get rid of this (needed for 'minute' below)
        implicit def modulizeTime[N: Field] = modulize[Time, N](qtt, implicitly[Field[N]])
        
        def minute[N: Field]: Quantity[Time, N] = 60 *: second[N]

        def canonicalBase[N: Field]: Quantity[Time, N] = second[N]

      }
    }
    
    import QT._

    // TODO: meter, second, etc should be importable
    
    val d1: Quantity[Distance, Rational] = Rational(3, 4) *: distance.meter[Rational]
    val d2: Quantity[Distance, Rational] = Rational(7, 2) *: distance.meter[Rational]
    val t1: Quantity[Time, Rational] = Rational(4) *: time.second[Rational]
    val t2: Quantity[Time, Rational] = Rational(9, 88) *: time.second[Rational]

    val d3 = d2 + d2
    val t3 = t2 in time.minute[Rational]

    implicit val m = modulize[Time, Rational](QT.time, implicitly[Field[Rational]])
    m.timesr(t1, Rational(5, 3))

    val t4 = t1 * 60

  }

}
