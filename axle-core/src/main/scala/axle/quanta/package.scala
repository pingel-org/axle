
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

    import spire.algebra.{ Module, Field, Rng, Eq }
    import spire.implicits._
    import axle.graph.Vertex
    import axle.graph.DirectedGraph
    import axle.graph.JungDirectedGraph
    //import spire.syntax._
    import scala.language.reflectiveCalls

    sealed trait Quantum
    class Distance extends Quantum
    class Time extends Quantum

    trait QT[Q <: Quantum] { self =>

      implicit def modulizeQ[N: Field: Eq] = modulize[Q, N](self, implicitly[Field[N]], implicitly[Eq[N]])

      def vertex[N: Field: Eq](quantity: Quantity[Q, N]): Vertex[Quantity[Q, N]] =
        conversionGraph[N].findVertex(_.payload === quantity).get

      def canonicalBase[N: Field: Eq]: Quantity[Q, N]

      def conversionGraph[N: Field: Eq]: DirectedGraph[Quantity[Q, N], N => N]
    }

    object Quantity {

      implicit def eqqqn[Q <: Quantum, N: Field: Eq]: Eq[Quantity[Q, N]] = new Eq[Quantity[Q, N]] {
        // TODO: should a conversion be done when checking equality?
        def eqv(x: Quantity[Q, N], y: Quantity[Q, N]): Boolean = (x.magnitude === y.magnitude) && (x.unit === y.unit)
      }
    }

    case class Quantity[Q <: Quantum, N: Field: Eq](magnitude: N, unitOpt: Option[Quantity[Q, N]] = None)(implicit qt: QT[Q]) {

      implicit val module = modulize[Q, N]

      def unit: Quantity[Q, N] = unitOpt.getOrElse(this)

      // TODO: avoid having to re-define syntax for + and *
      def *(that: N): Quantity[Q, N] = module.timesr(this, that)

      def +(that: Quantity[Q, N]): Quantity[Q, N] = module.plus(this, that)

      def in(newUnit: Quantity[Q, N]): Quantity[Q, N] =
        qt.conversionGraph[N].shortestPath(qt.vertex(newUnit.unit), qt.vertex(unit))
          .map(
            _.map(_.payload).foldLeft(implicitly[Field[N]].one)((n, convert) => convert(n)))
          .map(n => new Quantity((magnitude * n) / newUnit.magnitude, Some(newUnit)))
          .getOrElse(throw new Exception("no conversion path from " + this + " to " + newUnit))

      def *:(n: N): Quantity[Q, N] =
        if (unitOpt.isDefined) {
          new Quantity(magnitude * n, unitOpt)
        } else {
          new Quantity(n, Some(this))
        }
    }

    implicit def modulize[Q <: Quantum, N](implicit qt: QT[Q], field: Field[N], eqn: Eq[N]) = new Module[Quantity[Q, N], N] {

      def negate(x: Quantity[Q, N]): Quantity[Q, N] = new Quantity(-x.magnitude, x.unitOpt) // AdditiveGroup

      def zero: Quantity[Q, N] = new Quantity(field.zero, Some(qt.canonicalBase)) // AdditiveMonoid

      def plus(x: Quantity[Q, N], y: Quantity[Q, N]): Quantity[Q, N] =
        new Quantity((x in y.unit).magnitude + y.magnitude, y.unitOpt) // AdditiveSemigroup

      implicit def scalar: Rng[N] = field // Module

      def timesl(r: N, v: Quantity[Q, N]): Quantity[Q, N] = new Quantity(v.magnitude * r, v.unitOpt)
    }

    object QT {

      implicit val distance = new QT[Distance] { qtd: QT[Distance] =>

        def meter[N: Field: Eq]: Quantity[Distance, N] = new Quantity[Distance, N](implicitly[Field[N]].one, None)(implicitly[Field[N]], implicitly[Eq[N]], qtd)

        def canonicalBase[N: Field: Eq]: Quantity[Distance, N] = meter[N]

        def conversionGraph[N: Field: Eq]: DirectedGraph[Quantity[Distance, N], N => N] = ???
      }

      implicit val time = new QT[Time] { qtt: QT[Time] =>

        def second[N: Field: Eq]: Quantity[Time, N] = new Quantity(implicitly[Field[N]].one, None)(implicitly[Field[N]], implicitly[Eq[N]], qtt)

        def minute[N: Field: Eq]: Quantity[Time, N] = 60 *: second[N]

        def canonicalBase[N: Field: Eq]: Quantity[Time, N] = second[N]

        def conversionGraph[N: Field: Eq]: DirectedGraph[Quantity[Time, N], N => N] = ???
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

    implicit val m = modulize[Time, Rational](QT.time, implicitly[Field[Rational]], implicitly[Eq[Rational]])
    m.timesr(t1, Rational(5, 3))

    val t4 = t1 * 60

  }

}
