
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

    // implicit def modulizeQ = modulize[Q, N](self, implicitly[Field[N]], implicitly[Eq[N]])

    trait QT[Q <: Quantum, N] { self =>

      implicit def fieldN: Field[N]

      implicit def eqN: Eq[N]

      //      def vertex(quantity: Quantity[Q, N]): Vertex[Quantity[Q, N]] =
      //        conversionGraph.findVertex(_.payload === quantity).get
      //
      //      def canonicalBase: Quantity[Q, N]
      //
      //      def conversionGraph: DirectedGraph[Quantity[Q, N], N => N]
    }

    object Quantity {

      implicit def eqqqn[Q <: Quantum, N: Field: Eq]: Eq[Quantity[Q, N]] = new Eq[Quantity[Q, N]] {
        // TODO: perform conversion when checking equality
        def eqv(x: Quantity[Q, N], y: Quantity[Q, N]): Boolean = (x.magnitude === y.magnitude) && (x.unit === y.unit)
      }
    }

    case class Quantity[Q <: Quantum, N](magnitude: N, unitOpt: Option[Quantity[Q, N]] = None)(implicit qt: QT[Q, N], fieldN: Field[N], eqN: Eq[N]) {

      def unit: Quantity[Q, N] = unitOpt.getOrElse(this)

      private[this] def vertex(cg: DirectedGraph[Quantity[Q, N], N => N], quantity: Quantity[Q, N]): Vertex[Quantity[Q, N]] =
        cg.findVertex(_.payload === quantity).get

      def in(newUnit: Quantity[Q, N])(implicit cg: DirectedGraph[Quantity[Q, N], N => N]): Quantity[Q, N] =
        cg.shortestPath(vertex(cg, newUnit.unit), vertex(cg, unit))
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

    implicit def modulize[Q <: Quantum, N](implicit qt: QT[Q, N], fieldn: Field[N], eqn: Eq[N], cg: DirectedGraph[Quantity[Q, N], N => N]) = new Module[Quantity[Q, N], N] {

      def negate(x: Quantity[Q, N]): Quantity[Q, N] = new Quantity(-x.magnitude, x.unitOpt) // AdditiveGroup

      def zero: Quantity[Q, N] = new Quantity(fieldn.zero, None) // AdditiveMonoid

      def plus(x: Quantity[Q, N], y: Quantity[Q, N]): Quantity[Q, N] =
        new Quantity((x in y.unit).magnitude + y.magnitude, y.unitOpt) // AdditiveSemigroup

      implicit def scalar: Rng[N] = fieldn // Module

      def timesl(r: N, v: Quantity[Q, N]): Quantity[Q, N] = new Quantity(v.magnitude * r, v.unitOpt)
    }

    implicit def qt[Q <: Quantum, N](implicit _fieldN: Field[N], _eqN: Eq[N]): QT[Q, N] = new QT[Q, N] {

      implicit def eqN: Eq[N] = _eqN

      implicit def fieldN: Field[N] = _fieldN
    }

    val meter = Quantity[Distance, Rational](1)
    val second = Quantity[Time, Rational](1)
    val minute = 60 *: second

    implicit val cgDR: DirectedGraph[Quantity[Distance, Rational], Rational => Rational] = ???
    implicit val cgTR: DirectedGraph[Quantity[Time, Rational], Rational => Rational] = ???

    // TODO: meter, second, etc should be importable

    val d1 = Rational(3, 4) *: meter
    val d2 = Rational(7, 2) *: meter
    val t1 = Rational(4) *: second
    val t2 = Rational(9, 88) *: second
    val t3 = 5d *: second

    implicit val mdr = modulize[Distance, Rational]
    implicit val mtr = modulize[Time, Rational]

    val d3 = d1 + d2
    val d4 = d2 - d2
    //val d5 = d2 + t2 // shouldn't compile
    val t4 = t2 in minute
    val t5 = mtr.timesr(t1, Rational(5, 3))
    // TODO val t6 = t1 * Rational(5, 3)
    val t7 = mtr.timesl(Rational(5, 3), t1)
    // TODO val t8 = Rational(5, 3) * t1
    // TODO val t9 = t1 * 60
    // TODO val t5 = t1 / 2

  }

}
