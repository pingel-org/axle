package axle.logic

import FOPL._

object SamplePredicates {

  val I: Set[Any] = Set(10)
  val X: Set[Any] = Set(1, 2, 3)
  val Y: Set[Any] = Set(4, 5, 6)
  val Z: Set[Any] = Set(7, 8, 9)

  case class A(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "A"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class B(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "B"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class C(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "C"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class D(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "D"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class E(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "E"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class F(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "F"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class G(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "G"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class H(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "H"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class M(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "M"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class N(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "N"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class P(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "P"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class Q(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "Q"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class R(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "R"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class S(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "S"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class T(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "T"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class U(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "U"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }
  case class W(_symbols: Symbol*) extends Predicate(_symbols: _*) {
    def name: String = "W"
    def apply(symbolTable: Map[Symbol, Any]): Boolean = true
  }

}
