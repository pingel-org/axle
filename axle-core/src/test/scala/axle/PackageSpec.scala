package axle

import org.scalatest._

class PackageSpec extends FunSuite with Matchers {

  test("find gaps in List[Int]") {
    import spire.implicits._
    gaps(List(1, 2, 6, 7, 8, 9, 10, 16)) should be(List((3, 5), (11, 15)))
  }

  test("find runs in List[Int]") {
    import spire.implicits._
    runs(List(1, 2, 6, 7, 8, 9, 10, 16)) should be(List((1, 2), (6, 10), (16, 16)))
  }

  test("intersperse") {
    assertResult(intersperse(7)((11 to 13).toList))(List(11, 7, 12, 7, 13))
  }

}
