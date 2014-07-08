
package axle.ast.view

import scala.math.max
import scala.math.min
import scala.xml.NodeSeq.seqToNodeSeq

import Emission.emit
import axle.ast.AstNode
import axle.ast.Document
import axle.ast.LLLanguage
import axle.ast.Language

object ViewXhtml extends View[xml.NodeSeq] {
  // <html><head><link ref=... /></head><body>...</body><html>

  def makeFormatter(language: Language, node: AstNode): XhtmlAstNodeFormatter =
    new XhtmlAstNodeFormatter(
      FormatterConfig(language, true, Set(node)),
      FormatterState(0, 0, false, 0, Nil, Map()),
      Nil)

  // <div class={"code"}></div>
  override def AstNode(root: AstNode, language: Language): xml.NodeSeq =
    <link rel={ "stylesheet" } type={ "text/css" } href={ "/static/lodbms.css" }>
      { emit(language, root, makeFormatter(language, root)).result }
    </link>

  def nodeContext(language: Language, node: AstNode, uri: String): xml.NodeSeq = {

    val highlightedHtml = emit(language, node, makeFormatter(language, node)).result // NOTE: python version cached this

    val lineNos = max(1, node.lineNo - CONTEXT_PAD) to min(highlightedHtml.size, node.lineNo + CONTEXT_PAD)

    <div>{
      lineNos map { lineno =>
        <span class={ "lineno" }>
          <a href={ uri + '#' + lineno }>{ "%5d".format(lineno) }</a>
        </span>
        <span>{ highlightedHtml(lineno) }</span><br/>
      }
    }</div>

  }

  // def contextHtmlLines(): Option[LinkedHashMap[Int, NodeSeq]] = contextHtml(doc, docNode)
  override def docNodeInContext(doc: Document, docNode: AstNode): xml.NodeSeq =
    doc.ast.map(ast => nodeContext(doc.grammar, docNode, "/document/" + doc.name))
      .getOrElse(<span>Oh no</span>)

  def llRules(g: LLLanguage): xml.NodeSeq =
    <div>
      <span>Rules:</span>
      <ul>
        {
          g.llRules.zipWithIndex.map({
            case (rule, id) => {
              <li>{ id }:{ rule.from }->{ rule.rhs.mkString("", " ", "") }</li>
            }
          })
        }
      </ul>
    </div>

  def llParseTable(g: LLLanguage): xml.NodeSeq =
    <div>
      <span>Parse Table:</span>
      <table>
        <tr>
          <td></td>
          {
            g.terminals map { term =>
              <td>{ term.label }</td>
            }
          }
        </tr>
        {
          g.nonTerminals map { nterm =>
            <tr>
              <td>{ nterm }:</td>
              {
                g.terminals map { term =>
                  <td>
                    {
                      if (g.parseTable.contains((nterm, term))) {
                        g.parseTable((nterm, term)).id
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

  def llLanguage(g: LLLanguage): xml.NodeSeq =
    <h2>{ g.name }</h2>
    <div>
      { llRules(g) }
      { llParseTable(g) }
    </div>

}
