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

    val contextFormatter = new AstNodeFormatterString(doc.getGrammar, mutable.Set(docNode), true)
    doc.getAst().map(ast => {
      Emission.emit(doc.getGrammar, ast, contextFormatter)
      val highlighted_string = contextFormatter.result
      val highlighted_lines = highlighted_string.split("\n") // NOTE: python version used to cache highlighted_lines
      info("vs %s".format((for ((_, v) <- contextFormatter.node2lineno) yield v).mkString))

      // // TODO rjust(5) the second i+1

      (math.max(0, docNode.getLineNo - CONTEXT_PAD) to math.min(highlighted_lines.length - 1, docNode.getLineNo + CONTEXT_PAD))
        .map({ i => "<span class=lineno><a href='/view?filename=%s#%d'>%s</a></span> %s".format(doc.getName, i + 1, i + 1, highlighted_lines(i)) })
        .mkString("\n")

    }
    ).getOrElse("no ast defined")
  }

  override def lllRules(lll: LLLanguage): String = "TODO"

  override def lllParseTable(lll: LLLanguage): String = "TODO"

  override def llLanguage(lll: LLLanguage): String = {

    val result = new StringBuffer
    result.append("Rules:\n")
    for ((id, rule) <- lll.llRules) {
      result.append(rule + "\n")
    }
    result.append("\n")
    result.append("Parse Table:\n")
    result.append("   ")
    for ((_, term) <- lll.terminals) {
      result.append(term + " ")
    }
    result.append("\n")
    for ((_, nterm) <- lll.nonTerminals) {
      result.append(nterm + ": ")
      for ((_, term) <- lll.terminals) {
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
