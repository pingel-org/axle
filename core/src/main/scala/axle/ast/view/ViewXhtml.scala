
package axle.ast.view

import axle.ast._
import collection._
import math.{ min, max }
import Emission._

/*
object ViewXhtml extends View[xml.NodeSeq] {
  // <html><head><link ref=... /></head><body>...</body><html>

  override def AstNode(root: AstNode, language: Language): xml.NodeSeq = {
    // <div class={"code"}></div>
    <link rel={ "stylesheet" } type={ "text/css" } href={ "/static/lodbms.css" }>
      { emit(language, root, new XhtmlAstNodeFormatter(language, Set.empty, true)).result }
    </link>
  }

  def nodeContext(language: Language, node: AstNode, uri: String): xml.NodeSeq = {

    val highlightedHtml = emit(language, node, new XhtmlLinesAstNodeFormatter(language, Set(node), true)).result // NOTE: python version cached this

    val lineNos = max(1, node.lineNo - CONTEXT_PAD) to min(highlightedHtml.size, node.lineNo + CONTEXT_PAD)

    <div>{
      for { lineno <- lineNos } yield {
        <span class={ "lineno" }>
          <a href={ uri + '#' + lineno }>{ "%5d".format(lineno) }</a>
        </span>
        <span>{ highlightedHtml(lineno) }</span><br/>
      }
    }</div>

  }

  // def contextHtmlLines(): Option[LinkedHashMap[Int, NodeSeq]] = contextHtml(doc, docNode) 
  override def docNodeInContext(doc: Document, docNode: AstNode): xml.NodeSeq =
    doc.ast().map(ast => nodeContext(doc.grammar(), docNode, "/document/" + doc.name))
      .getOrElse(<span>Oh no</span>)

  override def lllRules(lll: LLLanguage): xml.NodeSeq = {

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

  override def lllParseTable(lll: LLLanguage): xml.NodeSeq = {

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

*/