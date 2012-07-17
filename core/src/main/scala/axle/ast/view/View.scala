package axle.ast.view

import axle.ast._

trait View[T] {

  val CONTEXT_PAD: Int = 5

  def AstNode(root: AstNode, language: Language): T

  def docNodeInContext(doc: Document, docNode: AstNode): T

  def lllRules(lll: LLLanguage): T

  def lllParseTable(lll: LLLanguage): T

  def llLanguage(lll: LLLanguage): T

}
