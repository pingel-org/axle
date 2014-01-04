package axle.logic

import FOPL._

object SamplePredicates {

  val I = Set(10)
  val X = Set(1, 2, 3)
  val Y = Set(4, 5, 6)
  val Z = Set(7, 8, 9)

  case class A(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "A"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class B(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "B"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class C(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "C"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class D(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "D"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class E(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "E"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class F(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "F"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class G(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "G"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class H(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "H"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class M(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "M"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class N(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "N"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class P(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "P"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class Q(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "Q"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class R(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "R"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class S(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "S"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class T(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "T"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class U(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "U"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class W(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name: String = "W"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }

}
