
package axle.ast

case class DocumentFromString(val grammar: Language, val name: String, content: String) extends Document {

  def ast: Option[AstNode] = grammar.parseString(content)
}

case class DocumentFromFile(val grammar: Language, shortFilename: String, filename: String) extends Document {

  def ast: Option[AstNode] = grammar.parseFile(filename)

  def name: String = shortFilename
}

trait Document {

  def ast: Option[AstNode]

  def grammar: Language

  def name: String

  //  def makeHtml = {
  //    val htmlFilename = Config.htmlDirectory + File.separator + shortFilename + ".html"
  //    new File(new File(htmlFilename).getParent).mkdirs
  //    val html = getGrammar.ast2html(getAst)
  //    val outFile = new File(htmlFilename)
  //    val out = new FileWriter(outFile)
  //    out.write(html.toString)
  //    out.close()
  //  }

}
