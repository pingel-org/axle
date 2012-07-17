package axle.ast.view

import axle.Loggable
import axle.ast._
import collection._

class AstNodeFormatterString(language: Language, highlight: mutable.Set[AstNode], conform: Boolean)
  extends AstNodeFormatter[String, mutable.ListBuffer[String]](language, highlight, conform)
  with Loggable {

  override val tokens = new mutable.ListBuffer[String]()

  override def result() = tokens.mkString("")

  override def toString(): String = tokens.toList.mkString("")

  // override def append(t: String) { tokens += t }

  override def accRaw(s: String): Unit = tokens.append(s)

  override def accNewline(): Unit = {
    // println("info: AstNodeFormatterString accNewine")
    tokens.append("\n")
  }

  override def accSpace(): Unit = tokens.append(" ")

  override def accSpaces(): Unit = tokens.append("   ") // TODO

  override def accSpan(spanclass: String, s: String): Unit = tokens += s

}
