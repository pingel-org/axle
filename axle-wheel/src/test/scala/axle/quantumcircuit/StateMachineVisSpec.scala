package axle.quantumcircuit

import edu.uci.ics.jung.graph.DirectedSparseGraph

import scala.xml._

import cats.effect._
import spire.math._

import axle._
import axle.jung._
import axle.algebra.DirectedGraph
import axle.visualize._
import axle.web._
import QBit._

import org.scalatest._

class StateMachineVisSpec extends FunSuite with Matchers {

  test("State Machine for X and H as DirectedGraph") {

    class Edge(val label: String)

    val fromToEdges =
      (commonQBits[Real] map { input => (input, H(input), new Edge("H"))}) ++
      (commonQBits[Real] map { input => (input, X(input), new Edge("X"))})

    implicit val htmlFromQbit: HtmlFrom[QBit[Real]] =
      new HtmlFrom[QBit[Real]] {
        def toHtml(a: QBit[Real]): Node = <span>{f"(${a.a.real.doubleValue}%1.4f, ${a.b.real.doubleValue}%1.4f)"}</span>
      }

    implicit val jdg = DirectedGraph.k2[DirectedSparseGraph, QBit[Real], Edge]

    val dg = jdg.make(commonQBits[Real], fromToEdges)

    import cats.Show
    implicit val showEdge: Show[Edge] = _.label

    val width = 800
    val height = 800
    val border = 100

    val centerX = (width - 2*border) / 2 + border
    val centerY = (height - 2*border) / 2 + border
    val radius = (min(width, height) - 2*border) / 2

    val layout = new GraphVertexLayout[Double, QBit[Real]] {
      def x(q: QBit[Real]): Double = centerX + radius*(q.a.real.toDouble)
      def y(q: QBit[Real]): Double = height - (centerY + radius*(q.b.real.toDouble))
    }

    val vis = DirectedGraphVisualization[DirectedSparseGraph[QBit[Real],Edge], QBit[Real]](
      dg, width, height, border, layoutOpt = Some(layout))

    val svgName = "qc_hx_state_machine.svg"
    // SVG[DirectedGraphVisualization[DirectedSparseGraph[QBit[Real],Edge], QBit[Real]] ]
    vis.svg[IO](svgName).unsafeRunSync()

    new java.io.File(svgName).exists should be(true)
  }

  // TODO
  //test("Bloch Sphere") {
  // ... to visualize qbits with non-zero imaginary component
  //}

}
