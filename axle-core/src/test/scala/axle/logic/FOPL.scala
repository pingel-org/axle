
package axle.logic

import org.specs2.mutable._
import spire.algebra.Eq
//import spire.implicits._
import spire.std.option._

/**
 *
 *
 */

class StatementSpecification extends Specification {

  import axle.logic.FOPL._
  import SamplePredicates._

  // This spec especially suffers from the conflicting specs "equalTo" and spire's ===
  // which results in this code being much less readable than it should be

  val stmtEq = Eq[Statement]

  "eliminate equivalence" should {
    "work 1" in {
      eliminateIff(∃('z ∈ Z, A('z) ⇔ B('z))) should be equalTo
        ∃('z ∈ Z, (A('z) ⊃ B('z)) ∧ (B('z) ⊃ A('z)))
    }
    "work 2" in {
      eliminateIff(∃('z ∈ Z, (A('z) ∧ G('z)) ⇔ (B('z) ∨ H('z)))) should be equalTo
        ∃('z ∈ Z, ((A('z) ∧ G('z)) ⊃ (B('z) ∨ H('z))) ∧ ((B('z) ∨ H('z)) ⊃ (A('z) ∧ G('z))))
    }
  }

  "eliminate implication" should {
    "work 1" in {
      eliminateImplication(∀('z ∈ Z, M('z) ⊃ N('z))) should be equalTo
        ∀('z ∈ Z, ¬(M('z)) ∨ N('z))
    }
    "work 2" in {
      eliminateImplication(∃('x ∈ X, (P('x) ⊃ Q('x)) ⊃ R('x))) should be equalTo
        ∃('x ∈ X, ¬(¬(P('x)) ∨ Q('x)) ∨ R('x))
    }
  }

  "moveNegation" should {
    "work 1" in {
      moveNegation(∃('i ∈ I, ¬(¬(M('i)) ∧ N('i)))) should be equalTo ∃('i ∈ I, M('i) ∨ ¬(N('i)))
    }
    "work 2" in {
      moveNegation(¬(∃('i ∈ I, (¬(M('i)) ∧ N('i))))) should be equalTo ∀('i ∈ I, M('i) ∨ ¬(N('i)))
    }
    "work 3" in {
      moveNegation(∀('x ∈ X, ¬(¬(P('x))))) should be equalTo ∀('x ∈ X, P('x))
    }
  }

  "skolemize" should {
    "work 1" in {
      val actual = skolemize(∃('x ∈ X, ∀('y ∈ Y, P('x, 'y))))
      val expected = (P('sk0, 'y), Map('sk0 -> Set('y)))
      stmtEq.eqv(actual._1, expected._1) must be equalTo true
      actual._2 must be equalTo expected._2
    }
    "work 2" in {
      val actual = skolemize(∀('x ∈ X, ∃('y ∈ Y, Q('x, 'y))))
      val expected = (Q('x, 'sk0), Map('sk0 -> Set('x)))
      stmtEq.eqv(actual._1, expected._1) must be equalTo true
      actual._2 must be equalTo expected._2
    }
    "work 3" in {
      val actual = skolemize(∀('x ∈ X, ∃('y ∈ Y, ∃('z ∈ Z, R('x, 'y, 'z)))))
      val expected = (R('x, 'sk0, 'sk1), Map('sk0 -> Set('x), 'sk1 -> Set('x)))
      stmtEq.eqv(actual._1, expected._1) must be equalTo true
      actual._2 must be equalTo expected._2
    }
    "work 4" in {
      // TODO: all variable names should be uniqued prior to skolemization
      val actual = skolemize(∀('z ∈ Z, ∀('y ∈ Y, ∃('z ∈ Z, P('y, 'z)))))
      val expected = (P('y, 'sk0), Map('sk0 -> Set('z, 'y)))
      stmtEq.eqv(actual._1, expected._1) must be equalTo true
      actual._2 must be equalTo expected._2
    }
  }

  "distribute" should {
    "work 1" in {
      distribute(P('x) ∨ (Q('x) ∧ R('x))) should be equalTo (P('x) ∨ Q('x)) ∧ (P('x) ∨ R('x))
    }
    "work 2" in {
      distribute(E('x) ∨ (F('x) ∧ G('x)) ∨ H('x)) should be equalTo
        ((E('x) ∨ F('x) ∨ H('x)) ∧ (E('x) ∨ G('x) ∨ H('x)))
    }
    "work 3" in {
      // TODO probably want to have the result parenthesized otherwise
      distribute(A('x) ∨ ((C('x) ∧ G('x)) ∨ B('x))) should be equalTo
        ((A('x) ∨ (C('x) ∨ B('x))) ∧ (A('x) ∨ (G('x) ∨ B('x))))
    }
    "work 4" in {
      // TODO probably want to have the result parenthesized otherwise
      distribute(A('x) ∧ (((E('x) ∨ F('x)) ∧ G('x)) ∨ B('x))) should be equalTo
        (A('x) ∧ ((E('x) ∨ F('x) ∨ B('x)) ∧ (G('x) ∨ B('x))))
    }
  }

  "flatten" should {
    "work 1" in {
      flatten((P('x) ∧ Q('x) ∧ (R('x) ∨ S('x)))) must be equalTo
        (P('x) ∧ (Q('x) ∧ (R('x) ∨ S('x))))
    }
    "work 2" in {
      flatten(A('x) ∨ B('x) ∨ C('x)) must be equalTo (A('x) ∨ (B('x) ∨ C('x)))
    }
    "work 3" in {
      flatten((P('x) ∨ Q('x)) ∧ R('x) ∧ S('x)) must be equalTo (((P('x) ∨ Q('x)) ∧ (R('x) ∧ S('x))))
    }
    "work 4" in {
      flatten(((P('x) ∨ Q('x) ∨ M('x)) ∧ (R('x) ∨ W('x)) ∧ S('x))) must be equalTo
        ((P('x) ∨ (Q('x) ∨ M('x))) ∧ ((R('x) ∨ W('x)) ∧ S('x)))
    }
  }

  "cnf" should {
    "work 1" in {
      val actual = conjunctiveNormalForm(∀('x ∈ X, P('x)))
      val expected = (P('x), Map.empty[Symbol, Set[Symbol]])
      stmtEq.eqv(actual._1, expected._1) must be equalTo true
      actual._2 must be equalTo expected._2
    }
    "work 2" in {
      val actual = conjunctiveNormalForm(∀('x ∈ X, ¬((P('x) ∨ F('x)) ⊃ Q('x))))
      val expected = ((P('x) ∨ F('x)) ∧ ¬(Q('x)), Map.empty[Symbol, Set[Symbol]])
      stmtEq.eqv(actual._1, expected._1) must be equalTo true
      actual._2 must be equalTo expected._2
    }
    "work 3" in {
      val actual = conjunctiveNormalForm(∀('x ∈ X, F('x) ⇔ G('x)))
      val expected = ((¬(F('x)) ∨ G('x)) ∧ (¬(G('x)) ∨ F('x)), Map.empty[Symbol, Set[Symbol]])
      stmtEq.eqv(actual._1, expected._1) must be equalTo true
      actual._2 must be equalTo expected._2
    }
    "work 4" in {
      // TODO: unique variable names
      val actual = conjunctiveNormalForm(¬(∀('x ∈ X, ∃('x ∈ X, P('x) ∧ Q('x)) ⊃ ∃('x ∈ X, D('x, 'x) ∨ F('x)))))
      val expected = (P('sk0) ∧ (Q('sk1) ∧ (¬(D('sk2, 'sk3)) ∧ ¬(F('sk4)))),
        Map('sk2 -> Set('x), 'sk3 -> Set('x), 'sk4 -> Set('x), 'sk1 -> Set.empty[Symbol], 'sk0 -> Set.empty[Symbol]))
      stmtEq.eqv(actual._1, expected._1) must be equalTo true
      actual._2 must be equalTo expected._2
    }
  }

  "inf" should {
    "work 1" in {
      val actual = implicativeNormalForm((P('y) ∨ Q('y)) ∧ ((¬(R('z)) ∨ ¬(S('v))) ∧ (T('f) ∨ ¬(U('g)))))
      val expected = List(true ⊃ (P('y) ∨ Q('y)), (R('z) ∧ S('v)) ⊃ false, U('g) ⊃ T('f))
      actual.zip(expected).forall({ case (x, y) => stmtEq.eqv(x, y) }) must be equalTo true
    }
    "work 2" in {
      val actual = implicativeNormalForm((P('x) ∨ R('x) ∨ ¬(Q('x))) ∧ (R('x) ∧ ¬(M('x))))
      val expected = List(Q('x) ⊃ (P('x) ∨ R('x)), true ⊃ R('x), M('x) ⊃ false)
      actual.zip(expected).forall({ case (x, y) => stmtEq.eqv(x, y) }) must be equalTo true
    }
    "work 3" in {
      val (cnf, skolems) = conjunctiveNormalForm(∀('x ∈ X, ¬((P('x) ∨ F('x)) ⊃ Q('x))))
      val actual = implicativeNormalForm(cnf)
      val expected = List(true ⊃ (P('x) ∨ F('x)), Q('x) ⊃ false)
      actual.zip(expected).forall({ case (x, y) => stmtEq.eqv(x, y) }) must be equalTo true
    }
  }

}
