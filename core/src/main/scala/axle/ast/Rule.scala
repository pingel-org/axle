
package axle.ast

case class Rule(name: String, _statement: Statement) {

  def statement = _statement

  var _precedence_level: Option[Int] = None
  def precedence_level = _precedence_level
  def precedence_level_=(p: Int) = {
    _precedence_level = Some(p)
  }

  private var _associativity: Option[String] = None
  def associativity = _associativity
  def associativity_=(a: String) = {
    _associativity = Some(a)
  }

}
