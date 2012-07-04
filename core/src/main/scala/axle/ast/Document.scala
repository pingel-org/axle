
package axle.ast

import axle.Loggable
import scala.collection.mutable.Set
import scala.collection.mutable.LinkedHashMap
import java.io.File
import java.io.FileWriter

class DocumentFromString(grammar: Language, label: String, content: String) extends Document {

  def getGrammar = grammar

  def getAst() = grammar.parseString(content)

  def getName() = label
}

class DocumentFromFile(grammar: Language, shortFilename: String, filename: String) extends Document {

  def getGrammar = grammar

  def getAst() = grammar.parseFile(filename)

  def getName() = shortFilename
}

trait Document extends Loggable {

  def getAst(): Option[MetaNode]

  def getGrammar(): Language

  def getName(): String

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
