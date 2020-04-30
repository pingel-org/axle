
package axle.ast

case class Document(
  ast: Option[AstNode],
  grammar: Language,
  name: String)

  //  def makeHtml = {
  //    val htmlFilename = Config.htmlDirectory + File.separator + shortFilename + ".html"
  //    new File(new File(htmlFilename).getParent).mkdirs
  //    val html = getGrammar.ast2html(getAst)
  //    val outFile = new File(htmlFilename)
  //    val out = new FileWriter(outFile)
  //    out.write(html.show)
  //    out.close()
  //  }

