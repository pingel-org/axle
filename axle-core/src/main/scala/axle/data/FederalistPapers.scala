package axle.data

import axle.nlp._
import axle._

object FederalistPapers {

  val idPattern = """FEDERALIST.? No. (\d+)""".r

  val allCaps = """[A-Z ]+""".r

  case class FederalistPaper(id: Int, author: String, text: String, metadata: String)

  def parseArticles(filename: String) = {

    val lines = io.Source.fromFile(filename).getLines.toList

    val starts = lines.zipWithIndex.filter({ case (line, i) => line.startsWith("FEDERALIST") }).map(_._2)

    val last = lines.zipWithIndex.find(_._1.startsWith("End of the Project Gutenberg EBook")).get._2 - 1

    val ranges = starts.zip(starts.drop(1) ++ Vector(last))

    ranges map {
      case (first, last) =>
        val id = idPattern.findFirstMatchIn(lines(first)).map(_.group(1).toInt).getOrElse(0)
        val authorIndex = ((first + 1) to last).find(i => allCaps.unapplySeq(lines(i)).isDefined).getOrElse(0)
        val metadata = ((first + 1) to authorIndex - 1).map(lines(_)).mkString("\n")
        val text = ((authorIndex + 1) to last).map(lines(_)).mkString("\n")
        FederalistPaper(id, lines(authorIndex), text, metadata)
    }
  }
  
  implicit object articleAsDocument extends Document[FederalistPaper] {
    
    def tokens(paper: FederalistPaper) = language.English.tokenize(paper.text)

    def bigrams(paper: FederalistPaper) = tokens(paper).sliding(2).toVector

    def wordCounts(paper: FederalistPaper) = tokens(paper).countMap

    def bigramCounts(paper: FederalistPaper) = bigrams(paper).countMap

    def averageWordLength(paper: FederalistPaper) = {
      val ts = tokens(paper)
      ts.map(_.length).sum / ts.length.toDouble
    }

  }
  

}
