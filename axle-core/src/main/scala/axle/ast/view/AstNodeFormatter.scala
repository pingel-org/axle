
package axle.ast.view

import axle.ast._

case class FormatterConfig(
  language: Language,
  conform: Boolean,
  highlight: Set[AstNode])

case class FormatterState(
  indentationLevel: Int,
  column: Int,
  needsIndent: Boolean,
  lineno: Int,
  stack: List[Option[(Int, String)]],
  _node2lineno: Map[AstNode, Int])

abstract class AstNodeFormatter[R, S](
  _config: FormatterConfig,
  _state: FormatterState,
  _subState: S) {

  def apply(fs: FormatterState, ss: S): AstNodeFormatter[R, S]

  def config() = _config

  def state() = _state

  def subState() = _subState

  def accRaw(s: String, n: Int): AstNodeFormatter[R, S]

  def accNewline(): AstNodeFormatter[R, S]

  def accSpace(): AstNodeFormatter[R, S]

  def accSpaces(): AstNodeFormatter[R, S]

  def accSpan(spanclass: String, s: String, n: Int): AstNodeFormatter[R, S]

  def accPushStack(): AstNodeFormatter[R, S]

  def accPopAndWrapStack(label: String): AstNodeFormatter[R, S]

  def result(): R

  def isConforming() = config.conform

  def shouldHighlight(node: AstNode) = config.highlight.contains(node)

  def node2lineno() = state._node2lineno

  def markLine(node: AstNode, lineNo: Int): AstNodeFormatter[R, S] =
    this(FormatterState(
      state.indentationLevel,
      state.column,
      state.needsIndent,
      state.lineno,
      state.stack,
      state._node2lineno ++ List(node -> lineNo)),
      subState)

  def _indent(): AstNodeFormatter[R, S] = {
    if (state.column == 0 && state.needsIndent) {
      val newF = (1 to state.indentationLevel).foldLeft(this)({ case (f, x) => f.accSpaces })
      this(FormatterState(
        newF.state.indentationLevel,
        3 * newF.state.indentationLevel,
        false,
        newF.state.lineno,
        newF.state.stack,
        newF.state._node2lineno),
        newF.subState)
    } else {
      this(FormatterState(
        state.indentationLevel,
        state.column,
        false,
        state.lineno,
        state.stack,
        state._node2lineno),
        subState)
    }
  }

  def needsParens(attr_name: String, node: AstNode, subtree: AstNode, grammar: Language): Boolean =
    (node, subtree) match {
      case (AstNodeRule(nodeRuleName, nm, _), AstNodeRule(subtreeRuleName, sm, _)) => {
        val nodeRule = grammar.name2rule(nodeRuleName)
        val subtreeRule = grammar.name2rule(subtreeRuleName)
        grammar.lowerThan(subtreeRule, nodeRule) match {
          case Some(true) => true
          case Some(false) => attr_name match {
            // TODO assumptions about the names of "left" and "right" should be externalized
            case "left" => grammar.precedenceOf(subtreeRule) == grammar.precedenceOf(nodeRule) && grammar.associativityOf(nodeRule) == "right" // (2 ** 3) ** 4
            case "right" => grammar.precedenceOf(subtreeRule) == grammar.precedenceOf(nodeRule) && grammar.associativityOf(nodeRule) == "left"
            case _ => false
          }
          case None => false // is this right?
        }
      }
      case (_, _) => false
    }

  def raw(element: String): AstNodeFormatter[R, S] = {
    val f1 = this._indent()
    val lines = element.split("\n")
    val f2 = lines.size match {
      case 0 | 1 =>
        this(FormatterState(
          state.indentationLevel,
          state.column + element.length,
          state.needsIndent,
          state.lineno,
          state.stack,
          state._node2lineno),
          subState)
      case _ =>
        this(FormatterState(
          state.indentationLevel,
          lines.last.length,
          state.needsIndent,
          state.lineno + lines.size - 1,
          state.stack,
          state._node2lineno),
          subState)
    }
    f2.accRaw(element, element.length)
  }

  // TODO !!! toka.append("\\")
  def wrap(indent: Boolean = false): AstNodeFormatter[R, S] =
    newline(true, None, indent)

  def space(): AstNodeFormatter[R, S] = {
    val f1 = this._indent()
    val f2 = (if (state.column > 80) {
      f1.wrap()
    } else {
      f1(FormatterState(
        state.indentationLevel,
        state.column + 1,
        state.needsIndent,
        state.lineno,
        state.stack,
        state._node2lineno),
        subState)
    })
    f2.accSpace()
  }

  def indent(): AstNodeFormatter[R, S] =
    this(FormatterState(
      state.indentationLevel + 1,
      state.column,
      state.needsIndent,
      state.lineno,
      state.stack,
      state._node2lineno),
      subState)

  def dedent(): AstNodeFormatter[R, S] =
    this(FormatterState(
      state.indentationLevel - 1,
      state.column,
      state.needsIndent,
      state.lineno,
      state.stack,
      state._node2lineno),
      subState)

  def beginSpan(): AstNodeFormatter[R, S] = accPushStack

  def endSpan(spanType: String): AstNodeFormatter[R, S] = accPopAndWrapStack(spanType)

  def conformTo(node: AstNode): AstNodeFormatter[R, S] = this

  // println("AstNodeFormatter.newline(hard="+hard+", node="+node+", indent="+indent+")")
  // println("   column = " + column)
  // println("   conform = " + conform)

  def newline(hard: Boolean, nodeOpt: Option[AstNode], indent: Boolean = true): AstNodeFormatter[R, S] = {
    // && node.getLineNo.isDefined && ( node.getLineNo.get < lineno )
    if (nodeOpt.isDefined && config.conform) {
      if (state.column > 0) {
        this(FormatterState(
          state.indentationLevel,
          0,
          indent,
          state.lineno + 1,
          state.stack,
          state._node2lineno),
          subState).accNewline()
      } else {
        this
      }
    } else if (hard || state.column > 0) {
      this(FormatterState(
        state.indentationLevel,
        0,
        indent,
        state.lineno + 1,
        state.stack,
        state._node2lineno),
        subState).accNewline()
    } else {
      this
    }
  }

  def keyword(kw: String): AstNodeFormatter[R, S] =
    _indent().accSpan("keyword", kw, kw.length)

  def operator(op: String): AstNodeFormatter[R, S] =
    _indent().accSpan("operator", op, op.length)

  // NOTE: may not need to escape non-html
  def repr(r: String): AstNodeFormatter[R, S] =
    _indent().accSpan("repr", xml.Utility.escape(r), r.length)

  def name(n: String): AstNodeFormatter[R, S] = {
    _indent()
    val special = false // TODO
    special match {
      case true => {
        val (s, span) = (n, "special") // TODO
        accSpan(span, s, s.length)
      }
      case false => accRaw(n, n.length)

    }
  }

  def enterFor(): AstNodeFormatter[R, S] =
    this(FormatterState(
      state.indentationLevel,
      state.column,
      state.needsIndent,
      state.lineno,
      List(None) ++ state.stack,
      state._node2lineno),
      subState)

  def updateFor(varName: String): AstNodeFormatter[R, S] = {
    val frame = Some(state.stack.head
      .map(frame => (frame._1 + 1, varName))
      .getOrElse((0, varName))
    )
    this(FormatterState(
      state.indentationLevel,
      state.column,
      state.needsIndent,
      state.lineno,
      List(frame) ++ state.stack,
      state._node2lineno),
      subState)
  }

  def leaveFor(): AstNodeFormatter[R, S] =
    this(FormatterState(
      state.indentationLevel,
      state.column,
      state.needsIndent,
      state.lineno,
      state.stack.tail,
      state._node2lineno),
      subState)

  // def result(): String = toka.toString()

}
