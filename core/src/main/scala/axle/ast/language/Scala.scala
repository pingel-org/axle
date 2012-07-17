package axle.ast.language

import axle.ast._
import util.matching.Regex

object Scala {

  val precedence = List()

  def scalaCompilerParse(text: String): Option[AstNode] = None // TODO !!! 

  def parse(text: String): AstNode = AstNodeValue(Some("TODO"), 1)

  val lang = new Language(
    "scala",
    List(new Rule("Stmt", Spread())),
    precedence,
    scalaCompilerParse,
    ast => ast // trim
  )

  def language = lang

  def parse_tests = List[String]()

  def emit_tests = List[String]()
}

