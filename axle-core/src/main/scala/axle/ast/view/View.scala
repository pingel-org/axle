package axle.ast.view

import axle.ast._

trait View[T] {

  val CONTEXT_PAD: Int = 5

  def AstNode(root: AstNode, language: Language): T

  def docNodeInContext(doc: Document, docNode: AstNode): T

  def llRules(g: LLLanguage): T

  def llParseTable(g: LLLanguage): T

  def llLanguage(g: LLLanguage): T

}
