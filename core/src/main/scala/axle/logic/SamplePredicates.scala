package axle.logic

import FOPL._

object SamplePredicates {

  val I = Set(10)
  val X = Set(1, 2, 3)
  val Y = Set(4, 5, 6)
  val Z = Set(7, 8, 9)
  
  case class A(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "A"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class B(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "B"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class C(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "C"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class D(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "D"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class E(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "E"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class F(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "F"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class G(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "G"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class H(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "H"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class M(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "M"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class N(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "N"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class P(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "P"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class Q(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "Q"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class R(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "R"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class S(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "S"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class T(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "T"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class U(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "U"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class W(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "W"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }

}
