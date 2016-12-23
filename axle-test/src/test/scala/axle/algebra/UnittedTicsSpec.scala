package axle.algebra

import scala.Vector

import org.scalatest._

import axle.quanta.Information
import axle.quanta.UnittedQuantity
import axle.jung.directedGraphJung
import cats.kernel.Eq
import spire.implicits._
import cats.implicits._
import edu.uci.ics.jung.graph.DirectedSparseGraph

class UnittedTicsSpec extends FunSuite with Matchers {

  test("Tics for UnittedQuantity") {

    implicit val id = Information.converterGraphK2[Double, DirectedSparseGraph]
    import id.bit
    implicit val base = bit

    val ticker = axle.quanta.unittedTicsGraphK2[Information, Double, DirectedSparseGraph]

    val tics = ticker.tics(0d *: bit, 1d *: bit).toVector

    // TODO: configurable precision
    val expected = Vector(
      (0.0 *: bit, "0.000000"),
      (0.1 *: bit, "0.100000"),
      (0.2 *: bit, "0.200000"),
      (0.3 *: bit, "0.300000"),
      (0.4 *: bit, "0.400000"),
      (0.5 *: bit, "0.500000"),
      (0.6 *: bit, "0.600000"),
      (0.7 *: bit, "0.700000"),
      (0.8 *: bit, "0.800000"),
      (0.9 *: bit, "0.900000"),
      (1.0 *: bit, "1.000000"))

    val vieq = Eq[Vector[(UnittedQuantity[Information, Double], String)]]

    // tics must be equalTo expected
    vieq.eqv(tics, expected) should be(true)
  }

}
