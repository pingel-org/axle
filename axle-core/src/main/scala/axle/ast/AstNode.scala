
package axle.ast

abstract class AstNode(val lineNo: Int) {
  def column: Int = -1
}

case class AstNodeValue(value: Option[String], _lineNo: Int)
  extends AstNode(_lineNo)

case class AstNodeList(list: List[AstNode], _lineNo: Int)
  extends AstNode(_lineNo) {
  def children: List[AstNode] = list
}

case class AstNodeRule(val ruleName: String, mm: Map[String, AstNode], _lineNo: Int)
  extends AstNode(_lineNo) {
  def children: List[AstNode] = mm.values.toList
}
