package axle.ast.view

import axle.ast._

trait View[T] {

  val CONTEXT_PAD: Int = 5

  def metaNode(root: MetaNode, language: Language): T

  def docNodeInContext(doc: Document, docNode: MetaNode): T

  def lllRules(lll: LLLanguage): T

  def lllParseTable(lll: LLLanguage): T

  def llLanguage(lll: LLLanguage): T

}
