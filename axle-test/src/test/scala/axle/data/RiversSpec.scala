package axle.data

import org.scalatest._
import spire.implicits._
import axle.orderToOrdering
import axle.quanta.Distance
import axle.quanta.Flow
import axle.algebra.modules.doubleRationalModule
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung.directedGraphJung
import cats.implicits._

class RiversSpec extends FunSuite with Matchers {

  test("rivers data") {

    implicit val dcg = {
      import axle.spireToCatsEq
      Distance.converterGraphK2[Double, DirectedSparseGraph]
    }
    implicit val fcg = Flow.converterGraphK2[Double, DirectedSparseGraph]

    val rivers = Rivers()

    import rivers._

    val longest: River = List(nile, mississippi).maxBy(_.length)

    longest.name should be("Nile")
  }

}
