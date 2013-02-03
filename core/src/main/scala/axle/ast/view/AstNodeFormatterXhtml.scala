package axle.ast.view

import axle.ast._
import collection._

/*
class XhtmlAstNodeFormatter(
  config: FormatterConfig,
  state: FormatterState,
  subState: List[xml.Node])
  extends AstNodeFormatter[List[xml.Node], List[xml.Node]](config, state, subState) {

  type A = XhtmlAstNodeFormatter

  override def result() = subState.toList

  override def toString(): String = subState.toList.mkString("")

  override def accRaw(s: String): XhtmlAstNodeFormatter = subState.append(Text(s))

  override def accNewline(): XhtmlAstNodeFormatter = subState.appendAll(<br/>)

  override def accSpace(): XhtmlAstNodeFormatter = subState.append(Text(" "))

  override def accSpaces(): XhtmlAstNodeFormatter = subState.append(<span>&nbsp;&nbsp;&nbsp;</span>) // TODO

  // xml.Utility.escape(word)
  override def accSpan(spanclass: String, s: String): XhtmlAstNodeFormatter = subState += <span class={ spanclass }>{ s }</span>

}

*/
