package axle.ast.view

import xml.{ NodeSeq, Text }
import collection._
import axle.ast._

class XhtmlLinesAstNodeAccumulatorState(xlf: XhtmlLinesAstNodeFormatter) {

  var currentLine: Option[mutable.ListBuffer[xml.Node]] = Some(new mutable.ListBuffer[xml.Node]())

  val _lines = mutable.Map[Int, NodeSeq]()

  def lines(): Map[Int, NodeSeq] = {
    if (currentLine.isDefined) {
      _lines += xlf.currentLineNo -> currentLine.get.toList
      currentLine = None // mark as "finished"
      // need better way of handling subsequent writes if they happen
    }
    _lines
  }

  def raw(s: String): Unit = currentLine.get.append(Text(s))

  def newline(): Unit = {
    _lines += xlf.currentLineNo -> currentLine.get.toList
    xlf.advanceLine()
    currentLine = Some(new mutable.ListBuffer[xml.Node]())
  }

  def space(): Unit = currentLine.get.append(Text(" "))

  def spaces(): Unit = currentLine.get.append(<span>&nbsp;&nbsp;&nbsp;</span>) // TODO

  // scala.xml.Utility.escape(word)
  def span(spanclass: String, s: String): Unit = currentLine.get += <span class={ spanclass }>{ s }</span>

  def absorb(label: String, absorbee: XhtmlLinesAstNodeAccumulatorState): Unit = {

    for ((lineno, line) <- absorbee.lines) {
      if (currentLine.get.size > 0) {
        val unfinishedLine: NodeSeq = currentLine.get.toList
        _lines += lineno -> <span>{ unfinishedLine }</span><span class={ label }>{ line }</span>;
        currentLine = Some(new mutable.ListBuffer[scala.xml.Node]())
      } else {
        _lines += lineno -> <span class={ label }>{ line }</span>
      }
    }
    if (absorbee.currentLine.isDefined) {
      currentLine = absorbee.currentLine
      // popped.lines += currentLineNo -> popped.currentLine.toList
      // popped.currentLine = None
    }
  }
}

class XhtmlLinesAstNodeFormatter(language: Language, highlight: Set[AstNode], conform: Boolean)
  extends AstNodeFormatter[Map[Int, NodeSeq], mutable.Stack[XhtmlLinesAstNodeAccumulatorState]](language, highlight, conform) {

  def result(): Map[Int, NodeSeq] = {
    if (tokens.size > 1) {
      throw new Exception(
        "called resultLines on XhtmlLinesFormatter with " +
          "an XhtmlLinesAccumulator that has a stack with more than one entry")
    }
    tokens.top.lines
  }

  var currentLineNo = 1
  def advanceLine(): Unit = currentLineNo += 1

  override val tokens = new mutable.Stack[XhtmlLinesAstNodeAccumulatorState]()

  tokens.push(new XhtmlLinesAstNodeAccumulatorState(this))

  def lines(): Map[Int, NodeSeq] = tokens.top.lines

  override def toString() = "XhtmlLinesAccumulator.toString not implemented"

  // override def append(t: String) { tokens += t }

  // delegate to the top of the stack for all of these

  override def conformTo(node: AstNode): Unit = {
    if (isConforming()) {
      while (node.lineNo > lineno) {
        // info("conforming.  formatter.lineno = " + formatter.lineno)
        newline(true, Some(node))
      }
    }
  }

  override def newline(hard: Boolean, nodeOpt: Option[AstNode], indent: Boolean = true): Unit =
    nodeOpt.map(node => {
      column = 0
      needsIndent = indent
      lineno += 1
      // result.appendAll(<br></br>)
      accNewline()
    })

  override def accRaw(s: String): Unit = tokens.top.raw(s)

  override def accNewline(): Unit = tokens.top.newline()

  override def accSpace(): Unit = tokens.top.space()

  override def accSpaces(): Unit = tokens.top.spaces()

  override def accSpan(spanclass: String, s: String) = tokens.top.span(spanclass, s)

  override def accPushStack(): Unit = tokens.push(new XhtmlLinesAstNodeAccumulatorState(this))

  // TODO: assert stack.size > 1
  override def accPopAndWrapStack(label: String) = tokens.top.absorb(label, tokens.pop)

}
