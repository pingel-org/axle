package axle.ast.language

import axle.ast._
import scala.util.matching.Regex

object Scala {

  val precedence = List()

  def scalaCompilerParse(text: String): Option[MetaNode] = None // TODO !!! 

  def parse(text: String): MetaNode = MetaNodeValue(Some("TODO"), 1)

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

