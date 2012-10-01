package axle.ast

import util.matching.Regex

import java.io.InputStream
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.File
import collection._

object Util {

  def findLeaves(dirname: String, suffix: String): List[String] = {
    val dir = new File(dirname)
    val result = new mutable.ListBuffer[String]()
    for (f <- dir.listFiles) {
      if (f.isDirectory) {
        result.appendAll(findLeaves(f.getName(), suffix).map(h => f.getName + File.separator + h))
      } else {
        if (f.getName.endsWith(suffix)) {
          result.append(f.getName)
        }
      }
    }
    result.toList
  }

  def convertStreamToString(is: InputStream): String = {
    val sb = new StringBuilder()
    try {
      val reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))
      var keepGoing = true
      while (keepGoing) {
        val line = reader.readLine()
        if (line != null) {
          sb.append(line + "\n")
        } else {
          keepGoing = false
        }
      }
    } finally {
      is.close()
    }
    sb.toString()
  }

  def matchAndTransform(string: String, regex: Regex, transform: (String, Regex.Match) => String): String = {
    val matches = regex.findAllIn(string).matchData.toList
    matches.scanLeft((0, ""))(
      (is: (Int, String), md: util.matching.Regex.Match) => {
        (md.end, string.substring(is._1, md.start) + transform(string, md))
      }).map(_._2).mkString("") + (if (matches.length > 0) { string.substring(matches.last.end, string.length) } else { "" })
  }

}
