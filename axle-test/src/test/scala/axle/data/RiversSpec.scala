package axle.data

import org.scalatest._
import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.implicits._
import cats.Order.catsKernelOrderingForOrder
import spire.implicits._
import axle.quanta.Distance
import axle.quanta.Flow
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung

class RiversSpec extends FunSuite with Matchers {

  test("rivers data") {

    implicit val dcg = {
      Distance.converterGraphK2[Double, DirectedSparseGraph]
    }
    implicit val fcg = Flow.converterGraphK2[Double, DirectedSparseGraph]

    val rivers = Rivers()

    import rivers._

    val longest: River = List(nile, mississippi).maxBy(_.length)

    longest.name should be("Nile")
  }

}
