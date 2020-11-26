package axle.algebra

import spire.algebra._
import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

class PackageSpec extends AnyFunSuite with Matchers {

  test("etc") {
    implicit val intRing: Ring[Int] = spire.implicits.IntAlgebra
    etc(1).take(5).toList should be(List(1, 2, 3, 4, 5))
  }

  test("chain") {

    val a = 0
    val f = (x: Int) => if( x < 3 ) Option(Option(x + 1)) else None
    val g = (x: Int) => x
    val empty = LazyList.empty[Option[Int]]
    val combine = (mb: Option[Int]) => (ll: LazyList[Option[Int]]) => ll.prepended(mb)

    val result = chain[Int, Int, Option, LazyList](a, f, g, empty, combine)

    val expected = Option(LazyList(Option(1), Option(2), Option(3)))

    result should be(expected)
  }

  test("chain infinite") {

    val a = 0
    val f = (x: Int) => Option(Option(x + 1))
    val g = (x: Int) => x
    val empty = LazyList.empty[Option[Int]]
    val combine = (mb: Option[Int]) => (ll: LazyList[Option[Int]]) => ll.prepended(mb)

    val result = chain[Int, Int, Option, LazyList](a, f, g, empty, combine).map(_.take(3))

    val expected = Option(LazyList(Option(1), Option(2), Option(3)))

    result should be(expected)
  }


}