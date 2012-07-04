package axle.ast.view

import axle.ast._
import scala.collection._
import scala.xml._

class XhtmlMetaNodeFormatter(language: Language, highlight: Set[MetaNode], conform: Boolean)
  extends MetaNodeFormatter[List[scala.xml.Node], mutable.ListBuffer[scala.xml.Node]](language, highlight, conform) {

  override def result() = tokens.toList

  override val tokens = new mutable.ListBuffer[scala.xml.Node]()

  override def toString(): String = tokens.toList.mkString("")

  // override def append(t: String) { tokens += t }

  override def accRaw(s: String): Unit = tokens.append(Text(s))

  override def accNewline(): Unit = tokens.appendAll(<br/>)

  override def accSpace(): Unit = tokens.append(Text(" "))

  override def accSpaces(): Unit = tokens.append(<span>&nbsp;&nbsp;&nbsp;</span>) // TODO

  // scala.xml.Utility.escape(word)
  override def accSpan(spanclass: String, s: String): Unit = tokens += <span class={ spanclass }>{ s }</span>

}
