package axle.jung

import axle.graph.Vertex
import axle.graph.Edge
import org.specs2.mutable._
import spire.math._

class UndirectedGraphSpec extends Specification {

  "Undirected Graph" should {
    "work" in {

      val g = JungUndirectedGraph(List("a", "b", "c", "d"),
        (vs: Seq[Vertex[String]]) => vs match {
          case a :: b :: c :: d :: Nil => List((a, b, ""), (b, c, ""), (c, d, ""), (d, a, ""), (a, c, ""), (b, d, ""))
          case _ => Nil
        })

      g.size must be equalTo (4)
    }
  }

  "REPL Demo" should {
    "work" in {

      val g = JungUndirectedGraph[String, Real](
        List("a"),
        (vs: Seq[Vertex[String]]) => Nil)

      1 must be equalTo (1)
    }
  }

}
