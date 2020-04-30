package axle.data

import java.net.URL

import scala.Vector

import cats.kernel.Eq
import cats.effect._
import cats.implicits._

import axle.IO.urlToCachedFileToLines

/**
 *
 * http://www.gutenberg.org/files/18/18.txt
 *
 */

object FederalistPapers {

  val idPattern = """FEDERALIST.? No. (\d+)""".r

  val allCaps = """[A-Z ]+""".r

  case class Article(id: Int, author: String, text: String, metadata: String)

  implicit val fpEq = Eq.fromUniversalEquals[Article]

  val source = new URL("http://www.gutenberg.org/files/18/18.txt")

  val filename = "gutenberg18.txt"

  def articles[F[_]: ContextShift: Sync](blocker: Blocker): F[List[Article]] = {

    urlToCachedFileToLines(source, Util.dataCacheDir, filename, blocker).map( lines => {

      val starts = lines.zipWithIndex.collect({ case (line, i) if line.startsWith("FEDERALIST") => i })

      val last = lines.zipWithIndex.find(_._1.startsWith("End of the Project Gutenberg EBook")).get._2 - 1

      val ranges = starts.zip(starts.drop(1) ++ Vector(last))

      ranges map {
        case (first, last) =>
          val id = idPattern.findFirstMatchIn(lines(first)).map(_.group(1).toInt).getOrElse(0)
          val authorIndex = ((first + 1) to last).find(i => allCaps.unapplySeq(lines(i)).isDefined).getOrElse(0)
          val metadata = ((first + 1) to authorIndex - 1).map(lines).mkString("\n")
          val text = ((authorIndex + 1) to last).map(lines).mkString("\n")
          Article(id, lines(authorIndex), text, metadata)
      }})
  }

}
