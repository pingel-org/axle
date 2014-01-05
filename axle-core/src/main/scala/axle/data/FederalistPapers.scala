package axle.data

import axle.nlp._
import axle._
import spire.math._

object FederalistPapers {

  val idPattern = """FEDERALIST.? No. (\d+)""".r

  val allCaps = """[A-Z ]+""".r

  case class FederalistPaper(id: Int, author: String, text: String, metadata: String)

  def parseArticles(filename: String): List[FederalistPaper] = {

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

    def tokens(paper: FederalistPaper): IndexedSeq[String] = language.English.tokenize(paper.text)

    def bigrams(paper: FederalistPaper): Vector[IndexedSeq[String]] = tokens(paper).sliding(2).toVector

    def wordCounts(paper: FederalistPaper): Map[String, Long] = tokens(paper) tally

    def bigramCounts(paper: FederalistPaper): Map[IndexedSeq[String], Long] = bigrams(paper) tally

    def averageWordLength(paper: FederalistPaper): Number = {
      val ts = tokens(paper)
      Rational(ts.map(_.length).sum, ts.length)
    }

  }


}
