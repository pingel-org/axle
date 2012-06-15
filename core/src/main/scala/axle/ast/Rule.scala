
package axle.ast

case class Rule(name: String, statement: Statement) {

  var _precedenceLevel: Option[Int] = None
  def precedenceLevel = _precedenceLevel
  def precedenceLevel_=(p: Int) = {
    _precedenceLevel = Some(p)
  }

  private var _associativity: Option[String] = None
  def associativity = _associativity
  def associativity_=(a: String) = {
    _associativity = Some(a)
  }

}
