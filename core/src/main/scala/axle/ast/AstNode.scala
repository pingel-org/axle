
package axle.ast

import collection._

abstract class AstNode(_lineno: Int) {
  def lineNo: Int = _lineno
  def column: Int = -1
}

case class AstNodeValue(value: Option[String], _lineno: Int)
  extends AstNode(_lineno) {
}

case class AstNodeList(list: List[AstNode], _lineno: Int)
  extends AstNode(_lineno) {
  def children = list
}

case class AstNodeRule(val ruleName: String, mm: Map[String, AstNode], _lineno: Int)
  extends AstNode(_lineno) {
  def children = mm.values.toList
}
