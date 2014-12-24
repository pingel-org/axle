package axle.ast.view

import axle.Show

case class XhtmlAstNodeFormatter(
  config: FormatterConfig,
  state: FormatterState,
  subState: List[xml.Node])
  extends AstNodeFormatter[List[xml.Node], List[xml.Node]] {

  def apply(s: FormatterState, ss: List[xml.Node]): XhtmlAstNodeFormatter =
    XhtmlAstNodeFormatter(config, s, ss)

  def result: List[xml.Node] = subState.toList

  def accRaw(s: String, n: Int): XhtmlAstNodeFormatter =
    XhtmlAstNodeFormatter(config, state, subState ++ List(xml.Text(s)))

  def accNewline: XhtmlAstNodeFormatter =
    XhtmlAstNodeFormatter(config, state, subState ++ List(<br/>))

  def accSpace: XhtmlAstNodeFormatter =
    XhtmlAstNodeFormatter(config, state, subState ++ List(xml.Text(" ")))

  def accSpaces: XhtmlAstNodeFormatter =
    XhtmlAstNodeFormatter(config, state, subState ++ List(<span>&nbsp;&nbsp;&nbsp;</span>))

  // xml.Utility.escape(word)
  def accSpan(spanclass: String, s: String, n: Int): XhtmlAstNodeFormatter =
    XhtmlAstNodeFormatter(config, state, subState ++ List(<span class={ spanclass }>{ s }</span>))

  def accPushStack: AstNodeFormatter[List[xml.Node], List[xml.Node]] = this

  def accPopAndWrapStack(label: String): AstNodeFormatter[List[xml.Node], List[xml.Node]] = this

}

object XhtmlAstNodeFormatter {

  implicit val showXhtmlAstNodeFormatter: Show[XhtmlAstNodeFormatter] =
    new Show[XhtmlAstNodeFormatter] {

      def text(anfx: XhtmlAstNodeFormatter): String = anfx.subState.toList.mkString("")
    }

}
