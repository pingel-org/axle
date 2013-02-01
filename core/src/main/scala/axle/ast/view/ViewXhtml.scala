
package axle.ast.view

import axle.ast._
import axle.Loggable
import collection._
import xml.{ NodeSeq, Text }

object ViewXhtml extends View[xml.NodeSeq] with Loggable {
  // <html><head><link ref=... /></head><body>...</body><html>

  override def AstNode(root: AstNode, language: Language): xml.NodeSeq = {
    // <div class={"code"}></div>
    val formatter = new XhtmlAstNodeFormatter(language, mutable.Set.empty, true)
    Emission.emit(language, root, formatter)
    <link rel={ "stylesheet" } type={ "text/css" } href={ "/static/lodbms.css" }>
      { formatter.result }
    </link>
  }

  def nodeContext(language: Language, node: AstNode, uri: String): xml.NodeSeq = {
    val contextFormatter = new XhtmlLinesAstNodeFormatter(language, mutable.Set(node), true)
    Emission.emit(language, node, contextFormatter)
    val highlightedHtml = contextFormatter.result // NOTE: python version cached this

    // Note: this was a LinkedHashMap:
    val lines = Map[Int, NodeSeq]() ++
      (math.max(1, node.lineNo - CONTEXT_PAD) to math.min(highlightedHtml.size, node.lineNo + CONTEXT_PAD))
      .map(i => i -> highlightedHtml(i))

    (for { (lineno, line) <- lines } yield {
      <span class={ "lineno" }><a href={ uri + '#' + lineno }>{ "%5d".format(lineno) }</a></span><span>{ line }</span><br/>
    })
      .flatMap(identity)
      .toSeq

  }

  // def contextHtmlLines(): Option[LinkedHashMap[Int, NodeSeq]] = contextHtml(doc, docNode) 
  override def docNodeInContext(doc: Document, docNode: AstNode): xml.NodeSeq =
    doc.ast().map(ast => nodeContext(doc.grammar(), docNode, "/document/" + doc.name))
      .getOrElse(<span>Oh no</span>)

  override def lllRules(lll: LLLanguage): NodeSeq = {

    <div>
      <span>Rules:</span>
      <ul>
        {
          lll.llRules.zipWithIndex.map({
            case (rule, id) => {
              <li>{ id }:{ rule.from }->{ rule.rhs.mkString("", " ", "") }</li>
            }
          })
        }
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
            for (term <- lll.terminals) yield {
              <td>{ term.label }</td>
            }
          }
        </tr>
        {
          for (nterm <- lll.nonTerminals) yield {
            <tr>
              <td>{ nterm }:</td>
              {
                for (term <- lll.terminals) yield {
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
