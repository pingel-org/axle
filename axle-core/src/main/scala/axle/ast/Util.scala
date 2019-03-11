package axle.ast

import java.io.File
import java.io.InputStream

import scala.util.matching.Regex

object Util {

  def findLeaves(dirname: String, suffix: String): List[String] =
    _findLeaves(new File(dirname), suffix)

  def _findLeaves(dir: File, suffix: String): List[String] =
    dir.listFiles.toList
      .flatMap({ file =>
        if (file.isDirectory) {
          _findLeaves(file, suffix).map(h => file.getName + File.separator + h)
        } else {
          if (file.getName.endsWith(suffix)) {
            List(file.getName)
          } else {
            Nil
          }
        }
      })

  def convertStreamToString(is: InputStream): String = {
    val result = scala.io.Source.fromInputStream(is, "UTF-8").getLines().mkString("\n")
    is.close
    result
  }

  def matchAndTransform(string: String, regex: Regex, transform: (String, Regex.Match) => String): String = {
    val matches = regex.findAllIn(string).matchData.toList
    matches.scanLeft((0, ""))(
      (is: (Int, String), md: util.matching.Regex.Match) => {
        (md.end, string.substring(is._1, md.start) + transform(string, md))
      }).map(_._2).mkString("") + (if (matches.length > 0) { string.substring(matches.last.end, string.length) } else { "" })
  }

}
