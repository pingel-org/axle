package axle.logic

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import cats.kernel.Eq

import FirstOrderPredicateLogic.enrichSymbol
import FirstOrderPredicateLogic.Statement
import FirstOrderPredicateLogic._

class FirstOrderPredicateLogicSpec extends AnyFunSuite with Matchers {

  import example.SamplePredicates._

  // This spec especially suffers from the conflicting specs "equalTo" and spire's ===
  // which results in this code being much less readable than it should be


  val i = Symbol("i")
  val x = Symbol("x")
  val y = Symbol("y")
  val z = Symbol("z")
  val sk0 = Symbol("sk0")
  val sk1 = Symbol("sk1")

  val stmtEq = Eq[Statement]

  test("eliminate equivalence") {
    assertResult(eliminateIff(∃(z ∈ Z, A(z) ⇔ B(z))))(∃(z ∈ Z, (A(z) ⊃ B(z)) ∧ (B(z) ⊃ A(z))))
    assertResult(eliminateIff(∃(z ∈ Z, (A(z) ∧ G(z)) ⇔ (B(z) ∨ H(z)))))(∃(z ∈ Z, ((A(z) ∧ G(z)) ⊃ (B(z) ∨ H(z))) ∧ ((B(z) ∨ H(z)) ⊃ (A(z) ∧ G(z)))))
  }

  test("eliminate implication") {
    assertResult(eliminateImplication(∀(z ∈ Z, M(z) ⊃ N(z))))(∀(z ∈ Z, ¬(M(z)) ∨ N(z)))
    assertResult(eliminateImplication(∃(x ∈ X, (P(x) ⊃ Q(x)) ⊃ R(x))))(∃(x ∈ X, ¬(¬(P(x)) ∨ Q(x)) ∨ R(x)))
  }

  test("moveNegation") {
    assertResult(moveNegation(∃(i ∈ I, ¬(¬(M(i)) ∧ N(i)))))(∃(i ∈ I, M(i) ∨ ¬(N(i))))
    assertResult(moveNegation(¬(∃(i ∈ I, (¬(M(i)) ∧ N(i))))))(∀(i ∈ I, M(i) ∨ ¬(N(i))))
    assertResult(moveNegation(∀(x ∈ X, ¬(¬(P(x))))))(∀(x ∈ X, P(x)))
  }

  test("skolemize") {
    {
      val actual = skolemize(∃(x ∈ X, ∀(y ∈ Y, P(x, y))))
      val expected = (P(sk0, y), Map(sk0 -> Set(y)))
      assertResult(stmtEq.eqv(actual._1, expected._1))(true)
      assertResult(actual._2)(expected._2)
    }
    {
      val actual = skolemize(∀(x ∈ X, ∃(y ∈ Y, Q(x, y))))
      val expected = (Q(x, sk0), Map(sk0 -> Set(x)))
      assertResult(stmtEq.eqv(actual._1, expected._1))(true)
      assertResult(actual._2)(expected._2)
    }
    {
      val actual = skolemize(∀(x ∈ X, ∃(y ∈ Y, ∃(z ∈ Z, R(x, y, z)))))
      val expected = (R(x, sk0, sk1), Map(sk0 -> Set(x), sk1 -> Set(x)))
      assertResult(stmtEq.eqv(actual._1, expected._1))(true)
      assertResult(actual._2)(expected._2)
    }
    {
      // TODO: all variable names should be uniqued prior to skolemization
      val actual = skolemize(∀(z ∈ Z, ∀(y ∈ Y, ∃(z ∈ Z, P(y, z)))))
      val expected = (P(y, sk0), Map(sk0 -> Set(z, y)))
      assertResult(stmtEq.eqv(actual._1, expected._1))(true)
      assertResult(actual._2)(expected._2)
    }
  }

  test("distribute") {
    assertResult(distribute(P(x) ∨ (Q(x) ∧ R(x))))((P(x) ∨ Q(x)) ∧ (P(x) ∨ R(x)))

    assertResult(distribute(E(x) ∨ (F(x) ∧ G(x)) ∨ H(x)))(
      ((E(x) ∨ F(x) ∨ H(x)) ∧ (E(x) ∨ G(x) ∨ H(x))))

    // TODO probably want to have the result parenthesized otherwise
    assertResult(distribute(A(x) ∨ ((C(x) ∧ G(x)) ∨ B(x))))(
      ((A(x) ∨ (C(x) ∨ B(x))) ∧ (A(x) ∨ (G(x) ∨ B(x)))))

    // TODO probably want to have the result parenthesized otherwise
    assertResult(distribute(A(x) ∧ (((E(x) ∨ F(x)) ∧ G(x)) ∨ B(x))))(
      (A(x) ∧ ((E(x) ∨ F(x) ∨ B(x)) ∧ (G(x) ∨ B(x)))))
  }

  test("flatten") {

    assertResult(flatten((P(x) ∧ Q(x) ∧ (R(x) ∨ S(x)))))(
      (P(x) ∧ (Q(x) ∧ (R(x) ∨ S(x)))))

    assertResult(flatten(A(x) ∨ B(x) ∨ C(x)))((A(x) ∨ (B(x) ∨ C(x))))

    assertResult(flatten((P(x) ∨ Q(x)) ∧ R(x) ∧ S(x)))((((P(x) ∨ Q(x)) ∧ (R(x) ∧ S(x)))))

    assertResult(flatten(((P(x) ∨ Q(x) ∨ M(x)) ∧ (R(x) ∨ W(x)) ∧ S(x))))(
      ((P(x) ∨ (Q(x) ∨ M(x))) ∧ ((R(x) ∨ W(x)) ∧ S(x))))
  }

  test("cnf") {
    {
      val actual = conjunctiveNormalForm(∀(x ∈ X, P(x)))
      val expected = (P(x), Map.empty[Symbol, Set[Symbol]])
      assertResult(stmtEq.eqv(actual._1, expected._1))(true)
      assertResult(actual._2)(expected._2)
    }
    {
      val actual = conjunctiveNormalForm(∀(x ∈ X, ¬((P(x) ∨ F(x)) ⊃ Q(x))))
      val expected = ((P(x) ∨ F(x)) ∧ ¬(Q(x)), Map.empty[Symbol, Set[Symbol]])
      assertResult(stmtEq.eqv(actual._1, expected._1))(true)
      assertResult(actual._2)(expected._2)
    }
    {
      val actual = conjunctiveNormalForm(∀(x ∈ X, F(x) ⇔ G(x)))
      val expected = ((¬(F(x)) ∨ G(x)) ∧ (¬(G(x)) ∨ F(x)), Map.empty[Symbol, Set[Symbol]])
      assertResult(stmtEq.eqv(actual._1, expected._1))(true)
      assertResult(actual._2)(expected._2)
    }
    {
      val sk2 = Symbol("sk2")
      val sk3 = Symbol("sk3")
      val sk4 = Symbol("sk4")
      // TODO: unique variable names
      val actual = conjunctiveNormalForm(¬(∀(x ∈ X, ∃(x ∈ X, P(x) ∧ Q(x)) ⊃ ∃(x ∈ X, D(x, x) ∨ F(x)))))
      val expected = (P(sk0) ∧ (Q(sk1) ∧ (¬(D(sk2, sk3)) ∧ ¬(F(sk4)))),
        Map(sk2 -> Set(x), sk3 -> Set(x), sk4 -> Set(x), sk1 -> Set.empty[Symbol], sk0 -> Set.empty[Symbol]))
      assertResult(stmtEq.eqv(actual._1, expected._1))(true)
      assertResult(actual._2)(expected._2)
    }
  }

  test("inf") {
    {
      val f = Symbol("f")
      val g = Symbol("g")
      val v = Symbol("v")
      val actual = implicativeNormalForm((P(y) ∨ Q(y)) ∧ ((¬(R(z)) ∨ ¬(S(v))) ∧ (T(f) ∨ ¬(U(g)))))
      val expected = List(true ⊃ (P(y) ∨ Q(y)), (R(z) ∧ S(v)) ⊃ false, U(g) ⊃ T(f))
      assertResult(actual.zip(expected).forall({ case (x, y) => stmtEq.eqv(x, y) }))(true)
    }
    {
      val actual = implicativeNormalForm((P(x) ∨ R(x) ∨ ¬(Q(x))) ∧ (R(x) ∧ ¬(M(x))))
      val expected = List(Q(x) ⊃ (P(x) ∨ R(x)), true ⊃ R(x), M(x) ⊃ false)
      assertResult(actual.zip(expected).forall({ case (x, y) => stmtEq.eqv(x, y) }))(true)
    }
    {
      val (cnf, skolems) = conjunctiveNormalForm(∀(x ∈ X, ¬((P(x) ∨ F(x)) ⊃ Q(x))))
      val actual = implicativeNormalForm(cnf)
      val expected = List(true ⊃ (P(x) ∨ F(x)), Q(x) ⊃ false)
      assertResult(actual.zip(expected).forall({ case (x, y) => stmtEq.eqv(x, y) }))(true)
    }
  }

}
