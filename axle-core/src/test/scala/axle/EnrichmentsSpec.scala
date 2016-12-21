package axle

import org.scalatest._
import spire.implicits._

class EnrichmentsSpec extends FunSuite with Matchers {

  test("x in EnrichedGenTraversable") {

    val cp = List(1, 2, 3) ⨯ List(4, 5, 6)
    val cpl = cp.toList

    assertResult(cpl.length)(9)
    assertResult(cpl(0))((1, 4))
    assertResult(cpl(8))((3, 6))
  }

  test("EnrichedIndexdSeq apply(Range) returns sub-sequence") {
    val xs = (1 to 10).toVector
    assertResult(xs(3 to 7).size)(5)
  }

  test("EnrichedIndexdSeq apply(empty range) returns empty IndexedSequence") {
    val xs = (1 to 10).toVector
    assertResult(xs(3 until 3).size)(0)
  }

  test("EnrichedIndexdSeq swap(i, j) swaps values") {
    val xs = (1 to 3).toVector
    assertResult(xs.swap(0, 1))(Vector(2, 1, 3))
  }

  test("EnrichedIndexdSeq random selects random element") {
    val xs = (1 to 10).toVector
    xs.random should be >= 4
  }

  test("EnrichedIndexdSeq powerset creates powerset") {
    val xs = (1 to 3).toVector
    assertResult(xs.powerset.size)(8)
  }

  test("EnrichedIndexdSeq ℘ also creates powerset") {
    val xs = (1 to 3).toVector
    assertResult(xs.℘)(xs.powerset)
  }

  test("EnrichedIndexdSeq permutations(n) creates permutations") {
    val xs = (1 to 5).toVector
    assertResult(xs.permutations(2).size)(20)
  }

  test("EnrichedIndexdSeq combinations(n) creates combinations") {
    val xs = (1 to 5).toVector
    assertResult(xs.combinations(2).size)(10)
  }

  test("forall in axle._") {
    val p: Integer => Boolean = (x: Integer) => x % 2 == 0
    val ints = List(2, 4, 6)
    val r: Boolean = false // TODO axle.forall(ints)(p)
    assertResult(r)(true)
  }

  test("enriched iterator supports lastOption") {
    assertResult((1 to 10).toIterator.lastOption)(Some(10))
  }

  test("enriched iterator supports terminatesWithin") {
    assertResult((1 to 10).toIterator.terminatesWithin(20))(true)
    assertResult((1 to 10).toIterator.terminatesWithin(5))(false)
  }
}
