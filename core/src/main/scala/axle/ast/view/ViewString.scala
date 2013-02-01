package axle.ast.view

import axle.ast._
import axle.Loggable
import collection._

object ViewString extends View[String] with Loggable {

  override def AstNode(root: AstNode, language: Language): String = {
    val formatter = new AstNodeFormatterString(language, mutable.Set.empty, true)
    Emission.emit(language, root, formatter)
    formatter.result
  }

  override def docNodeInContext(doc: Document, docNode: AstNode): String = {

    val contextFormatter = new AstNodeFormatterString(doc.grammar, mutable.Set(docNode), true)
    doc.ast().map(ast => {
      Emission.emit(doc.grammar, ast, contextFormatter)
      val highlighted_string = contextFormatter.result
      val highlighted_lines = highlighted_string.split("\n") // NOTE: python version used to cache highlighted_lines
      info("vs %s".format((for ((_, v) <- contextFormatter.node2lineno) yield v).mkString))

      // // TODO rjust(5) the second i+1

      (math.max(0, docNode.lineNo - CONTEXT_PAD) to math.min(highlighted_lines.length - 1, docNode.lineNo + CONTEXT_PAD))
        .map({ i => "<span class=lineno><a href='/view?filename=%s#%d'>%s</a></span> %s".format(doc.name, i + 1, i + 1, highlighted_lines(i)) })
        .mkString("\n")

    }
    ).getOrElse("no ast defined")
  }

  override def lllRules(lll: LLLanguage): String = "TODO"

  override def lllParseTable(lll: LLLanguage): String = "TODO"

  override def llLanguage(lll: LLLanguage): String = {

    val result = new StringBuffer
    result.append("Rules:\n")
    for (rule <- lll.llRules) {
      result.append(rule + "\n")
    }
    result.append("\n")
    result.append("Parse Table:\n")
    result.append("   ")
    for (term <- lll.terminals) {
      result.append(term + " ")
    }
    result.append("\n")
    for (nterm <- lll.nonTerminals) {
      result.append(nterm + ": ")
      for (term <- lll.terminals) {
        if (lll.parseTable.contains((nterm, term))) {
          result.append(lll.parseTable((nterm, term)).id + " ")
        } else {
          result.append("- ")
        }
      }
      result.append("\n")
    }
    result.append("\n")
    result.toString
  }

}
