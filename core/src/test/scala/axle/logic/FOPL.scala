
import org.specs2.mutable._

class StatementSPecification extends Specification {

  import FOPL._

  case class A(s: Symbol) extends Statement
  case class B(s: Symbol) extends Statement
  case class C(s: Symbol) extends Statement
  case class D(s: Symbol, t: Symbol) extends Statement
  case class E(s: Symbol) extends Statement
  case class F(s: Symbol) extends Statement
  case class G(s: Symbol) extends Statement
  case class H(s: Symbol) extends Statement
  case class M(s: Symbol) extends Statement
  case class N(s: Symbol) extends Statement
  case class P(s: Symbol) extends Statement
  case class Q(s: Symbol) extends Statement
  case class R(s: Symbol) extends Statement
  case class S(s: Symbol) extends Statement
  case class T(s: Symbol) extends Statement
  case class U(s: Symbol) extends Statement
  case class W(s: Symbol) extends Statement
  case class X(s: Symbol) extends Statement
  case class Y(s: Symbol, t: Symbol) extends Statement
  case class Z(s: Symbol, t: Symbol, u: Symbol) extends Statement

  "eliminate equivalence" should {
    "work" in {
      eliminateIff(∃('z, A('z) ⇔ B('z))) should be equalTo
        ∃('z, (A('z) ⊃ B('z)) ∧ (B('z) ⊃ A('z)))
    }
  }

  "eliminate equivalence 2" should {
    "work" in {
      eliminateIff(∃('z, (A('z) ∧ G('z)) ⇔ (B('z) ∨ H('z)))) should be equalTo
        ∃('z, ((A('z) ∧ G('z)) ⊃ (B('z) ∨ H('z))) ∧ ((B('z) ∨ H('z)) ⊃ (A('z) ∧ G('z))))
    }
  }

  "eliminate implication" should {
    "work" in {
      eliminateImplication(∀('r, M('r) ⊃ N('r))) should be equalTo
        ∀('r, ¬(M('r)) ∨ N('r))
    }
  }

  "eliminate implication 2" should {
    "work" in {
      eliminateImplication(∃('x, (P('x) ⊃ Q('x)) ⊃ R('x))) should be equalTo
        ∃('x, ¬(¬(P('x)) ∨ Q('x)) ∨ R('x))
    }
  }

  "moveNegation" should {
    "work" in {
      moveNegation(∃('i, ¬(¬(M('i)) ∧ N('i)))) should be equalTo ∃('i, M('i) ∨ ¬(N('i)))
    }
  }

  "moveNegation 2" should {
    "work" in {
      moveNegation(¬(∃('i, (¬(M('i)) ∧ N('i))))) should be equalTo ∀('i, M('i) ∨ ¬(N('i)))
    }
  }

  "moveNegation 3" should {
    "work" in {
      moveNegation(∀('x, ¬(¬(P('x))))) should be equalTo ∀('x, P('x))
    }
  }

  "skolemize" should {
    "work" in {
      skolemize(∃('d, ∀('e, Y('d, 'e)))) should be equalTo Y('NEWFCN1, 'E)
    }
  }

  "skolemize 2" should {
    "work" in {
      skolemize(∀('d, ∃('e, Y('d, 'e)))) should be equalTo Y('d, skolemFor(1, 'd))
    }
  }

  "skolemize 3" should {
    "work" in {
      skolemize(∀('d, ∃('e, ∃('f, Z('d, 'e, 'f))))) should be equalTo Z('d, skolemFor(1, 'd), skolemFor(2, 'd))
    }
  }

  "skolemize 4" should {
    "work" in {
      skolemize(P('x)) should be equalTo ∀('c, ∀('d, ∃('e, Y('d, 'e))))
    }
  }

  "distribute" should {
    "work" in {
      // Note: other orders are valid
      distribute(P('x) ∨ (Q('x) ∧ R('x))) should be equalTo (P('x) ∨ Q('x)) ∧ (P('x) ∨ R('x))
    }
  }
  "distribute 2" should {
    "work" in {
      distribute(E('x) ∨ (F('x) ∧ G('x)) ∨ H('x)) should be equalTo
        ((F('x) ∨ E('x) ∨ H('x)) ∧ (G('x) ∨ E('x) ∨ H('x)))
    }
  }

  "distribute 3" should {
    "work" in {
      distribute(A('x) ∨ ((C('x) ∧ G('x)) ∨ B('x))) should be equalTo
        ((C('x) ∨ A('x) ∨ B('x)) ∧ (G('x) ∨ A('x) ∨ B('x)))
    }
  }

  "distribute 4" should {
    "work" in {
      distribute(A('x) ∧ (((E('x) ∨ F('x)) ∧ G('x)) ∨ B('x))) should be equalTo
        (A('x) ∧ (E('x) ∨ F('x) ∨ B('x)) ∧ (G('x) ∨ B('x)))
    }
  }

  "flatten" should {
    "work" in {
      flatten((P('x) ∧ (Q('x) ∧ (R('x) ∨ S('x))))) must be equalTo
        ((P('x) ∧ Q('x)) ∧ (R('x) ∨ S('x)))
    }
  }

  "flatten 2" should {
    "work" in {
      flatten(A('x) ∨ (B('x) ∨ C('x))) must be equalTo (A('x) ∨ B('x) ∨ C('x))
    }
  }

  "flatten 3" should {
    "work" in {
      flatten(((P('x) ∨ Q('x)) ∧ (R('x) ∧ S('x)))) must be equalTo ((P('X) ∨ Q('X)) ∧ R('X) ∧ S('X))
    }
  }

  "flatten 4" should {
    "work" in {
      flatten(((P('x) ∨ (Q('x) ∨ M('x))) ∧ ((R('x) ∨ W('x)) ∧ S('x)))) must be equalTo
        ((P('x) ∨ Q('X) ∨ M('X)) ∧ (R('X) ∨ W('X)) ∧ S('X))
    }
  }

  "cnf" should {
    "work" in {
      conjunctiveNormalForm(∀('x, P('x))) must be equalTo P('x)
    }
  }

  "cnf 2" should {
    "work" in {
      conjunctiveNormalForm(∀('x, ¬((P('x) ∨ F('x)) ⊃ X('x)))) must be equalTo
        (P('x) ∨ F('x)) ∧ ¬(X('x))
    }
  }

  "cnf 3" should {
    "work" in {
      conjunctiveNormalForm(∀('m, F('m) ⇔ G('m))) must be equalTo
        (¬(F('m)) ∨ G('m)) ∧ (¬(G('m)) ∧ F('m))
    }
  }

  "cnf 4" should {
    "work" in {
      conjunctiveNormalForm(¬(∀('x, ∃('x, P('x) ∧ Q('x)) ⊃ ∃('x, D('x, 'x) ∨ F('x))))) must be equalTo
        P('NEWFCN1) ∧ Q('NEWFCN1) ∧ ¬(D('x, 'x)) ∧ ¬(F('x))
    }
  }

  "inf" should {
    "work" in {
      implicativeNormalForm((P('y) ∨ Q('y)) ∧ (¬(R('z))) ∨ (¬(S('v))) ∧ (T('f)) ∨ (¬(U('g)))) must be equalTo
        List(true ⊃ (P('y) ∨ Q('y)), (R('z) ∧ S('v)) ⊃ false, U('g) ⊃ T('f))
    }
  }

  "inf 2" should {
    "work" in {
      implicativeNormalForm((P('x) ∨ R('x) ∨ (¬(Q('x)))) ∧ R('x) ∧ (¬(M('x)))) must be equalTo
        List(Q('x) ⊃ (P('x) ∨ R('x)), true ⊃ R('x), M('x) ⊃ false)
    }
  }

  "inf 3" should {
    "work" in {
      implicativeNormalForm(conjunctiveNormalForm(∀('x, ¬((P('x) ∨ F('x)) ⊃ X('x))))) must be equalTo
        List(true ⊃ (P('x) ∨ F('x)), X('x) ⊃ false)
    }
  }

}