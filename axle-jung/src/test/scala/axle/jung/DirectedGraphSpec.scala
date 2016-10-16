
package axle.jung

import org.specs2.mutable.Specification

import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.algebra.DirectedGraph
import spire.math.Real
import spire.implicits._
import axle.syntax.finite._
import axle.syntax.directedgraph._
import axle.syntax.DirectedGraphOps

class DirectedGraphSpec extends Specification {

  "Directed Graph" should {
    "work" in {

      class Edge(val weight: Real)

      val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]

      val a = "a"
      val b = "b"
      val c = "c"
      val d = "d"

      val g = jdg.make(List(a, b, c, d),
        List(
          (a, b, new Edge(1.1)),
          (b, c, new Edge(1.1)),
          (c, d, new Edge(1.1)),
          (d, a, new Edge(1.1)),
          (a, c, new Edge(1.1)),
          (b, d, new Edge(1.1))))

      g.size must be equalTo 4
      g.neighbors(a).size must be equalTo 3
      // g.edges.size must be equalTo 6
      g.vertices.size must be equalTo 4
      g.findVertex(_ == "a").get must be equalTo "a"
      vertexFunctorDSG.map(g)(s => s + s).findVertex(_ == "aa").get must be equalTo "aa"
      edgeFunctorDSG.map(g)(r => new Edge(r.weight + 1.1)).findEdge("a","b").weight must be equalTo Real(2.2)
    }
  }

}
