package axle.ast

import scala.util.matching.Regex

object Util {

  def matchAndTransform(
    string: String,
    regex: Regex,
    transform: (String, Regex.Match) => String): String = {

    val matches = regex.findAllIn(string).matchData.toList
  
    matches.scanLeft((0, ""))(
      (is: (Int, String), md: util.matching.Regex.Match) => {
        (md.end, string.substring(is._1, md.start) + transform(string, md))
      }).map(_._2).mkString("") + (if (matches.length > 0) { string.substring(matches.last.end, string.length) } else { "" })
  }

}
