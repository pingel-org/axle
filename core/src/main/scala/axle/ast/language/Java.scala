package axle.ast.language

import axle.ast._
import util.matching.Regex

object Java {

  val precedence = Nil

  def java_compiler_parse(text: String): Option[AstNode] = None

  def parse(text: String): AstNode = AstNodeValue(Some("TODO"), 1)

  val lang = new Language(
    "java",
    List(new Rule("Stmt", Spread())),
    precedence,
    java_compiler_parse,
    ast => ast // trim
  )

  def language = lang

  def parse_tests = List[String]()

  def emit_tests = List[String]()
}

