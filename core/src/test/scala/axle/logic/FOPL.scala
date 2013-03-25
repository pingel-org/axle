
package axle.logic

import org.specs2.mutable._

class StatementSpecification extends Specification {

  import FOPL._
  import SamplePredicates._

  "eliminate equivalence" should {
    "work 1" in {
      eliminateIff(∃('z, A('z) ⇔ B('z))) should be equalTo
        ∃('z, (A('z) ⊃ B('z)) ∧ (B('z) ⊃ A('z)))
    }
    "work 2" in {
      eliminateIff(∃('z, (A('z) ∧ G('z)) ⇔ (B('z) ∨ H('z)))) should be equalTo
        ∃('z, ((A('z) ∧ G('z)) ⊃ (B('z) ∨ H('z))) ∧ ((B('z) ∨ H('z)) ⊃ (A('z) ∧ G('z))))
    }
  }

  "eliminate implication" should {
    "work 1" in {
      eliminateImplication(∀('r, M('r) ⊃ N('r))) should be equalTo
        ∀('r, ¬(M('r)) ∨ N('r))
    }
    "work 2" in {
      eliminateImplication(∃('x, (P('x) ⊃ Q('x)) ⊃ R('x))) should be equalTo
        ∃('x, ¬(¬(P('x)) ∨ Q('x)) ∨ R('x))
    }
  }

  "moveNegation" should {
    "work 1" in {
      moveNegation(∃('i, ¬(¬(M('i)) ∧ N('i)))) should be equalTo ∃('i, M('i) ∨ ¬(N('i)))
    }
    "work 2" in {
      moveNegation(¬(∃('i, (¬(M('i)) ∧ N('i))))) should be equalTo ∀('i, M('i) ∨ ¬(N('i)))
    }
    "work 3" in {
      moveNegation(∀('x, ¬(¬(P('x))))) should be equalTo ∀('x, P('x))
    }
  }

  "skolemize" should {
    "work 1" in {
      skolemize(∃('d, ∀('e, Y('d, 'e)))) should be equalTo Y(skolemFor(1, 'd), 'e)
    }
    "work 2" in {
      skolemize(∀('d, ∃('e, Y('d, 'e)))) should be equalTo Y('d, skolemFor(1, 'e))
    }
    "work 3" in {
      skolemize(∀('d, ∃('e, ∃('f, Z('d, 'e, 'f))))) should be equalTo Z('d, skolemFor(1, 'e), skolemFor(1, 'f))
    }
    "work 4" in {
      // TODO: skolem fn should be sfE('c, 'd)
      skolemize(∀('c, ∀('d, ∃('e, Y('d, 'e))))) should be equalTo Y('d, skolemFor(1, 'e))
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
      conjunctiveNormalForm(∀('x, P('x))) must be equalTo P('x)
    }
    "work 2" in {
      conjunctiveNormalForm(∀('x, ¬((P('x) ∨ F('x)) ⊃ X('x)))) must be equalTo
        (P('x) ∨ F('x)) ∧ ¬(X('x))
    }
    "work 3" in {
      conjunctiveNormalForm(∀('m, F('m) ⇔ G('m))) must be equalTo
        (¬(F('m)) ∨ G('m)) ∧ (¬(G('m)) ∨ F('m))
    }
    "work 4" in {
      conjunctiveNormalForm(¬(∀('x, ∃('x, P('x) ∧ Q('x)) ⊃ ∃('x, D('x, 'x) ∨ F('x))))) must be equalTo
        P(skolemFor(1, 'x)) ∧ (Q(skolemFor(1, 'x)) ∧ (¬(D('x, 'x)) ∧ ¬(F('x))))
    }
  }

  "inf" should {
    "work 1" in {
      implicativeNormalForm( (P('y) ∨ Q('y)) ∧ ((¬(R('z)) ∨ ¬(S('v))) ∧ (T('f) ∨ ¬(U('g))))) must be equalTo
        List(true ⊃ (P('y) ∨ Q('y)), (R('z) ∧ S('v)) ⊃ false, U('g) ⊃ T('f))
    }
    "work 2" in {
      implicativeNormalForm((P('x) ∨ R('x) ∨ ¬(Q('x))) ∧ (R('x) ∧ ¬(M('x)))) must be equalTo
        List(Q('x) ⊃ (P('x) ∨ R('x)), true ⊃ R('x), M('x) ⊃ false)
    }
    "work 3" in {
      implicativeNormalForm(conjunctiveNormalForm(∀('x, ¬((P('x) ∨ F('x)) ⊃ X('x))))) must be equalTo
        List(true ⊃ (P('x) ∨ F('x)), X('x) ⊃ false)
    }
  }

}