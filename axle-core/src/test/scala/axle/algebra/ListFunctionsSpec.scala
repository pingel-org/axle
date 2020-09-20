package axle.algebra

import cats.implicits._
import spire.algebra._
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

class ListFunctionsSpec extends AnyFunSuite with Matchers {

  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra

  test("find gaps in List[Int]") {
    gaps(List(1, 2, 6, 7, 8, 9, 10, 16)) should be(List((3, 5), (11, 15)))
  }

  test("find runs in List[Int]") {
    runs(List(1, 2, 6, 7, 8, 9, 10, 16)) should be(List((1, 2), (6, 10), (16, 16)))
  }

  test("intersperse") {
    assertResult(intersperse(7)((11 to 13).toList))(List(11, 7, 12, 7, 13))
  }

}
