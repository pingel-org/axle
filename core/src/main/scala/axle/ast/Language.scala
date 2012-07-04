
package axle.ast

import axle.Loggable
import collection._

case class Language(
  name: String,
  rules: List[Rule],
  precedence_groups: List[Tuple2[List[String], String]],
  parser: String => Option[MetaNode],
  trimmer: MetaNode => MetaNode)
  extends Loggable {

  // def name = _name
  // def uri = "/grammar/" + name

  val name2rule = rules.map(r => r.name -> r).toMap

  var level = 0
  for ((names, assoc) <- precedence_groups) {
    for (rule_name <- names) {
      info("rule %s level %s".format(rule_name, level))
      val rule = name2rule(rule_name)
      rule.precedenceLevel = level
      rule.associativity = assoc
    }
    level += 1
  }

  def trim(ast: MetaNode) = trimmer(ast)

  // info("cat " + filename + " | parser")
  def parseFile(filename: String) = parser(scala.io.Source.fromFile(filename).mkString)

  def parseString(code: String) = parser(code)

  def precedenceOf(rule_name: String): Option[Int] = name2rule.contains(rule_name) match {
    case true => name2rule(rule_name).precedenceLevel
    case false => None
  }

  def lowerThan(x: String, y: String): Boolean = {
    val lx = precedenceOf(x)
    val ly = precedenceOf(y)
    lx.isDefined && ly.isDefined && lx.get < ly.get
  }

}
