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
    val f = (x: Int) => Option(if( x < 3 ) Option(x + 1) else None)
    val g = (x: Int) => x
    val empty = List.empty[Int]
    val combine = (head: Int) => (tail: List[Int]) => head :: tail

    val result = chain[Int, Int, Option, List](a, f, g, empty, combine)

    val expected = Option(List(1, 2, 3))

    result should be(expected)
  }

  test("foled") {

    val a = 0
    val f = (x: Int) => Option( if( x < 3 ) Option(x + 1) else None )
    val g = (x: Int) => x

    val result = foled[Int, Int, Option](a, f, g)

    val expected = Option(Option(3))

    result should be(expected)
  }

}