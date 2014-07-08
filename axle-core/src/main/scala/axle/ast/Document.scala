
package axle.ast

class DocumentFromString(_grammar: Language, _label: String, content: String) extends Document {

  def grammar: Language = _grammar

  def ast: Option[AstNode] = _grammar.parseString(content)

  def name: String = _label
}

class DocumentFromFile(_grammar: Language, shortFilename: String, filename: String) extends Document {

  def grammar: Language = _grammar

  def ast: Option[AstNode] = _grammar.parseFile(filename)

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
