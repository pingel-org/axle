
package axle.ast.view

import axle.ast._
import axle.Loggable
import collection._
import xml.{ NodeSeq, Text }

object ViewXhtml extends View[xml.NodeSeq] with Loggable {
  // <html><head><link ref=... /></head><body>...</body><html>

  override def AstNode(root: AstNode, language: Language): xml.NodeSeq = {
    val result = new mutable.ArrayBuffer[xml.Node]()
    // <div class={"code"}></div>
    result.append(<link rel={ "stylesheet" } type={ "text/css" } href={ "/static/lodbms.css" }/>)
    val formatter = new XhtmlAstNodeFormatter(language, mutable.Set.empty, true)
    Emission.emit(language, root, formatter)
    result.appendAll(formatter.result)
    result.toList
  }

  def nodeContext(language: Language, node: AstNode, uri: String): xml.NodeSeq = {
    val contextFormatter = new XhtmlLinesAstNodeFormatter(language, mutable.Set(node), true)
    Emission.emit(language, node, contextFormatter)
    val highlightedHtml = contextFormatter.result // NOTE: python version cached this

    val lines = new mutable.LinkedHashMap[Int, NodeSeq]()

    (math.max(1, node.getLineNo - CONTEXT_PAD) to math.min(highlightedHtml.size, node.getLineNo + CONTEXT_PAD))
      .map(i => { lines += i -> highlightedHtml(i) })

    info("contextHtml's result = " + lines)

    (for ((lineno, line) <- lines) yield {
      <span class={ "lineno" }><a href={ uri + '#' + lineno }>{ "%5d".format(lineno) }</a></span><span>{ line }</span><br/>
    })
      .flatMap(identity)
      .toSeq

  }

  // Option[mutable.LinkedHashMap[Int, NodeSeq]]
  // def contextHtmlLines(): Option[LinkedHashMap[Int, NodeSeq]] = contextHtml(doc, docNode) 
  override def docNodeInContext(doc: Document, docNode: AstNode): xml.NodeSeq =
    doc.getAst().map(ast => nodeContext(doc.getGrammar(), docNode, "/document/" + doc.getName))
      .getOrElse(<span>Oh no</span>)

  override def lllRules(lll: LLLanguage): NodeSeq = {

    val rs = for ((_, rule) <- lll.llRules) yield {
      <li>{ rule.id }:{ rule.from }->{ rule.rhs.mkString("", " ", "") }</li>
    }

    <div>
      <span>Rules:</span>
      <ul>
        { rs }
      </ul>
    </div>
  }

  override def lllParseTable(lll: LLLanguage): NodeSeq = {

    <div>
      <span>Parse Table:</span>
      <table>
        <tr>
          <td></td>
          {
            for ((_, term) <- lll.terminals) yield {
              <td>{ term.label }</td>
            }
          }
        </tr>
        {
          for ((_, nterm) <- lll.nonTerminals) yield {
            <tr>
              <td>{ nterm }:</td>
              {
                for ((_, term) <- lll.terminals) yield {
                  <td>
                    {
                      if (lll.parseTable.contains((nterm, term))) {
                        lll.parseTable((nterm, term)).id
                      } else {
                        "-"
                      }
                    }
                  </td>
                }
              }
            </tr>
          }
        }
      </table>
    </div>
  }

  override def llLanguage(lll: LLLanguage) = {
    <h2>{ lll.name }</h2>
    <div>
      { lllRules(lll) }
      { lllParseTable(lll) }
    </div>
  }

}
