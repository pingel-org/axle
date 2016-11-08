
package axle.ast

trait AstNode {
  def column: Int = -1
  def lineNo: Int
}

case class AstNodeValue(value: Option[String], lineNo: Int)
  extends AstNode

case class AstNodeList(list: List[AstNode], lineNo: Int)
  extends AstNode {
  def children: List[AstNode] = list
}

case class AstNodeRule(val ruleName: String, mm: Map[String, AstNode], lineNo: Int)
  extends AstNode {
  def children: List[AstNode] = mm.values.toList
}
