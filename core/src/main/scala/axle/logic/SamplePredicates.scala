package axle.logic

import FOPL._

object SamplePredicates {

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
  case class X(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "X"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class Y(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "Y"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class Z(symbols: Symbol*) extends Predicate(symbols: _*) {
    def name() = "Z"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }

}
