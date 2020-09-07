package axle.logic.example

import FirstOrderPredicateLogic.Predicate

object SamplePredicates {

  val I: Set[Any] = Set(10)
  val X: Set[Any] = Set(1, 2, 3)
  val Y: Set[Any] = Set(4, 5, 6)
  val Z: Set[Any] = Set(7, 8, 9)

  case class A(symbols: List[Symbol]) extends Predicate {
    def name: String = "A"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object A {
    def apply(symbols: Symbol*): A = A(symbols.toList)
  }
  case class B(symbols: List[Symbol]) extends Predicate {
    def name: String = "B"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object B {
    def apply(symbols: Symbol*): B = B(symbols.toList)
  }
  case class C(symbols: List[Symbol]) extends Predicate {
    def name: String = "C"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object C {
    def apply(symbols: Symbol*): C = C(symbols.toList)
  }
  case class D(symbols: List[Symbol]) extends Predicate {
    def name: String = "D"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object D {
    def apply(symbols: Symbol*): D = D(symbols.toList)
  }
  case class E(symbols: List[Symbol]) extends Predicate {
    def name: String = "E"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object E {
    def apply(symbols: Symbol*): E = E(symbols.toList)
  }
  case class F(symbols: List[Symbol]) extends Predicate {
    def name: String = "F"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object F {
    def apply(symbols: Symbol*): F = F(symbols.toList)
  }
  case class G(symbols: List[Symbol]) extends Predicate {
    def name: String = "G"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object G {
    def apply(symbols: Symbol*): G = G(symbols.toList)
  }
  case class H(symbols: List[Symbol]) extends Predicate {
    def name: String = "H"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object H {
    def apply(symbols: Symbol*): H = H(symbols.toList)
  }
  case class M(symbols: List[Symbol]) extends Predicate {
    def name: String = "M"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object M {
    def apply(symbols: Symbol*): M = M(symbols.toList)
  }
  case class N(symbols: List[Symbol]) extends Predicate {
    def name: String = "N"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object N {
    def apply(symbols: Symbol*): N = N(symbols.toList)
  }
  case class P(symbols: List[Symbol]) extends Predicate {
    def name: String = "P"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object P {
    def apply(symbols: Symbol*): P = P(symbols.toList)
  }
  case class Q(symbols: List[Symbol]) extends Predicate {
    def name: String = "Q"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object Q {
    def apply(symbols: Symbol*): Q = Q(symbols.toList)
  }
  case class R(symbols: List[Symbol]) extends Predicate {
    def name: String = "R"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object R {
    def apply(symbols: Symbol*): R = R(symbols.toList)
  }
  case class S(symbols: List[Symbol]) extends Predicate {
    def name: String = "S"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object S {
    def apply(symbols: Symbol*): S = S(symbols.toList)
  }
  case class T(symbols: List[Symbol]) extends Predicate {
    def name: String = "T"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object T {
    def apply(symbols: Symbol*): T = T(symbols.toList)
  }
  case class U(symbols: List[Symbol]) extends Predicate {
    def name: String = "U"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object U {
    def apply(symbols: Symbol*): U = U(symbols.toList)
  }
  case class W(symbols: List[Symbol]) extends Predicate {
    def name: String = "W"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  object W {
    def apply(symbols: Symbol*): W = W(symbols.toList)
  }

}
