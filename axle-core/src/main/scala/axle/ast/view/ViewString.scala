package axle.ast.view

import scala.math.max
import scala.math.min

import Emission.emit
import axle.ast.AstNode
import axle.ast.Document
import axle.ast.LLLanguage
import axle.ast.Language
import axle.string

object ViewString extends View[String] {

  def makeFormatter(language: Language, node: AstNode): AstNodeFormatterString =
    AstNodeFormatterString(
      FormatterConfig(language, true, Set(node)),
      FormatterState(0, 0, false, 0, Nil, Map()),
      Nil)

  override def AstNode(root: AstNode, language: Language): String =
    emit(language, root, makeFormatter(language, root)).result

  override def docNodeInContext(doc: Document, docNode: AstNode): String = {

    doc.ast.map(ast => {
      val fN = emit(doc.grammar, ast, makeFormatter(doc.grammar, docNode))
      val highlightedString = fN.result
      val highlightedLines = highlightedString.split("\n") // NOTE: python version used to cache highlightedLines
      // info("vs %s".format((fN.node2lineno map { case (_, v) => v }).mkString))

      // // TODO rjust(5) the second i+1

      val lineNos = max(0, docNode.lineNo - CONTEXT_PAD) to min(highlightedLines.length - 1, docNode.lineNo + CONTEXT_PAD)
      lineNos
        .map({ i => "<span class=lineno><a href='/view?filename=%s#%d'>%s</a></span> %s".format(doc.name, i + 1, i + 1, highlightedLines(i)) })
        .mkString("\n")

    }).getOrElse("no ast defined")
  }

  override def llRules(g: LLLanguage): String =
    "Rules:\n\n" +
      g._llRules.map(rule => (rule.id + ". " + string(rule))).mkString("\n")

  override def llParseTable(g: LLLanguage): String =
    "Parse Table:\n\n" +
      "   " + g.terminals.map(_.label).mkString("  ") + "\n" +
      g.nonTerminals.map(nt =>
        nt.label + "  " + g.terminals.map(t =>
          g.parseTable.get((nt, t)).map(_.id).getOrElse("-")).mkString("  ")).mkString("\n")

  override def llLanguage(g: LLLanguage): String =
    llRules(g) + "\n" + llParseTable(g)

}
