package axle.ast.view

import axle.ast._
import collection._

class XhtmlAstNodeFormatter(
  config: FormatterConfig,
  state: FormatterState,
  subState: List[xml.Node])
  extends AstNodeFormatter[List[xml.Node], List[xml.Node]](config, state, subState) {

  def apply(s: FormatterState, ss: List[xml.Node]) = new XhtmlAstNodeFormatter(config, s, ss)

  def result() = subState.toList

  override def toString(): String = subState.toList.mkString("")

  def accRaw(s: String, n: Int): XhtmlAstNodeFormatter =
    new XhtmlAstNodeFormatter(config, state, subState ++ List(xml.Text(s)))

  def accNewline(): XhtmlAstNodeFormatter =
    new XhtmlAstNodeFormatter(config, state, subState ++ List(<br/>))

  def accSpace(): XhtmlAstNodeFormatter =
    new XhtmlAstNodeFormatter(config, state, subState ++ List(xml.Text(" ")))

  def accSpaces(): XhtmlAstNodeFormatter =
    new XhtmlAstNodeFormatter(config, state, subState ++ List(<span>&nbsp;&nbsp;&nbsp;</span>))

  // xml.Utility.escape(word)
  def accSpan(spanclass: String, s: String, n: Int): XhtmlAstNodeFormatter =
    new XhtmlAstNodeFormatter(config, state, subState ++ List(<span class={ spanclass }>{ s }</span>))

  def accPushStack() = this

  def accPopAndWrapStack(label: String) = this

}
