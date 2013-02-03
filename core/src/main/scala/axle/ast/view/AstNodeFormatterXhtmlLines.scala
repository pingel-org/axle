package axle.ast.view

import collection._
import axle.ast._

/*
case class XhtmlLinesAstNodeFormatterState(
  tokens: List[xml.Node],
  currentLine: Option[List[xml.Node]], // = Some(List[xml.Node]())
  _lines: Map[Int, xml.NodeSeq])

class XhtmlLinesAstNodeFormatter(
  config: FormatterConfig,
  state: FormatterState,
  subState: XhtmlLinesAstNodeFormatterState)
  extends AstNodeFormatter[Map[Int, xml.NodeSeq], XhtmlLinesAstNodeFormatterState](config, state, subState) {

  type A = XhtmlAstNodeFormatter

  def lines(): Map[Int, xml.NodeSeq] = {
    if (currentLine.isDefined) {
      _lines += currentLineNo -> currentLine.get.toList
      currentLine = None // mark as "finished"
      // need better way of handling subsequent writes if they happen
    }
    _lines
  }

  def raw(s: String): XhtmlAstNodeFormatter = currentLine.get.append(xml.Text(s))

  def newline(): XhtmlAstNodeFormatter = {
    _lines += currentLineNo -> currentLine.get.toList
    advanceLine()
    currentLine = Some(List[xml.Node]())
  }

  def space(): XhtmlAstNodeFormatter = currentLine.get.append(xml.Text(" "))

  def spaces(): XhtmlAstNodeFormatter = currentLine.get.append(<span>&nbsp;&nbsp;&nbsp;</span>) // TODO

  // xml.Utility.escape(word)
  def span(spanclass: String, s: String): XhtmlAstNodeFormatter = currentLine.get += <span class={ spanclass }>{ s }</span>

  def absorb(label: String): XhtmlAstNodeFormatter = {

    for ((lineno, line) <- lines) {
      if (currentLine.get.size > 0) {
        val unfinishedLine: xml.NodeSeq = currentLine.get.toList
        _lines += lineno -> <span>{ unfinishedLine }</span><span class={ label }>{ line }</span>;
        currentLine = Some(List[scala.xml.Node]())
      } else {
        _lines += lineno -> <span class={ label }>{ line }</span>
      }
    }
    if (currentLine.isDefined) {
      // currentLine = absorbee.currentLine
      //// popped.lines += currentLineNo -> popped.currentLine.toList
      //// popped.currentLine = None
    }
  }

  def result(): Map[Int, xml.NodeSeq] = {
    if (tokens.size > 1) {
      throw new Exception(
        "called resultLines on XhtmlLinesFormatter with " +
          "an XhtmlLinesAccumulator that has a stack with more than one entry")
    }
    tokens.head.lines
  }

  val currentLineNo = 1
  def advanceLine(): XhtmlLinesAstNodeFormatter = currentLineNo += 1

  override val tokens = List[XhtmlLinesAstNodeAccumulator]()

  tokens.push(new XhtmlLinesAstNodeAccumulator(this))

  def lines(): Map[Int, xml.NodeSeq] = tokens.head.lines

  override def toString() = "XhtmlLinesAccumulator.toString not implemented"

  // override def append(t: String) { tokens += t }

  // delegate to the top of the stack for all of these

  override def conformTo(node: AstNode): XhtmlLinesAstNodeFormatter = {
    if (isConforming()) {
      while (node.lineNo > lineno) {
        // info("conforming.  formatter.lineno = " + formatter.lineno)
        newline(true, Some(node))
      }
    } else {
      this
    }
  }

  override def newline(hard: Boolean, nodeOpt: Option[AstNode], indent: Boolean = true): XhtmlLinesAstNodeFormatter =
    nodeOpt.map(node => {
      column = 0
      needsIndent = indent
      lineno += 1
      // result.appendAll(<br></br>)
      accNewline()
    })

  override def accRaw(s: String): XhtmlLinesAstNodeFormatter = tokens.top.raw(s)

  override def accNewline(): XhtmlLinesAstNodeFormatter = tokens.top.newline()

  override def accSpace(): XhtmlLinesAstNodeFormatter = tokens.top.space()

  override def accSpaces(): XhtmlLinesAstNodeFormatter = tokens.top.spaces()

  override def accSpan(spanclass: String, s: String): XhtmlLinesAstNodeFormatter = tokens.top.span(spanclass, s)

  override def accPushStack(): XhtmlLinesAstNodeFormatter = tokens.push(new XhtmlLinesAstNodeAccumulatorState(this))

  // TODO: assert stack.size > 1
  override def accPopAndWrapStack(label: String): XhtmlLinesAstNodeFormatter = tokens.top.absorb(label, tokens.pop)

}
*/
