package axle.quanta

import cats.kernel.Eq
import spire.algebra.Field
import spire.algebra.Module
import spire.math.ConvertableTo
import axle.algebra.Bijection
import axle.algebra.DirectedGraph
import axle.algebra.Scale
import axle.algebra.Scale10s

case class Force() extends Quantum {

  def wikipediaUrl: String = "http://en.wikipedia.org/wiki/Force"

}

trait ForceUnits extends QuantumUnits[Force] {

  lazy val pound = unit("pound", "lb", Some("http://en.wikipedia.org/wiki/Pound-force"))
  lazy val newton = unit("newton", "N", Some("http://en.wikipedia.org/wiki/Newton_(unit)"))
  lazy val dyne = unit("dyne", "dyn", Some("http://en.wikipedia.org/wiki/Dyne"))

  def units: List[UnitOfMeasurement[Force]] =
    List(pound, newton, dyne)

}

trait ForceConverter[N] extends UnitConverter[Force, N] with ForceUnits {

  def defaultUnit = newton

}

object Force {

  import spire.math._
  import spire.implicits._

  def converterGraphK2[N: Field: Eq: ConvertableTo, DG[_, _]](
    implicit
    module: Module[N, Rational],
    evDG:   DirectedGraph[DG[UnitOfMeasurement[Force], N => N], UnitOfMeasurement[Force], N => N]) =
    converterGraph[N, DG[UnitOfMeasurement[Force], N => N]]

  def converterGraph[N: Field: Eq: ConvertableTo, DG](
    implicit
    module: Module[N, Rational], evDG: DirectedGraph[DG, UnitOfMeasurement[Force], N => N]) =
    new UnitConverterGraph[Force, N, DG] with ForceConverter[N] {

      def links: Seq[(UnitOfMeasurement[Force], UnitOfMeasurement[Force], Bijection[N, N])] =
        List[(UnitOfMeasurement[Force], UnitOfMeasurement[Force], Bijection[N, N])](
          (newton, pound, Scale(4.4482216)),
          (dyne, newton, Scale10s(5)))

    }

}
