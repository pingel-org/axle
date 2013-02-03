
package axle.ast

import collection._

class Language(
  name: String,
  rules: List[Rule],
  precedenceGroups: List[(List[String], String)],
  parser: String => Option[AstNode],
  trimmer: AstNode => AstNode) {

  val name2rule = rules.map(r => r.name -> r).toMap

  val rulename2precedence = precedenceGroups.zipWithIndex.flatMap({
    case ((names, assoc), i) => names.map((_, i))
  }).toMap

  val rulename2associativity = precedenceGroups.flatMap({
    case (names, assoc) => names.map((_, assoc))
  }).toMap

  def trim(ast: AstNode) = trimmer(ast)

  def parseFile(filename: String) = parser(io.Source.fromFile(filename).mkString)

  def parseString(code: String) = parser(code)

  def precedenceOf(rule: Rule): Option[Int] = rulename2precedence.get(rule.name)

  def associativityOf(rule: Rule): String = rulename2associativity.get(rule.name).getOrElse("left")

  def lowerThan(x: Rule, y: Rule): Option[Boolean] =
    precedenceOf(x).flatMap(px => precedenceOf(y).map(py => px < py))

}
