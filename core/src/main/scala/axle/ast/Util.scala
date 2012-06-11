package axle.ast

import scala.util.matching.Regex

import java.io.InputStream
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.File
import scala.collection.mutable.ListBuffer

object Util {

  def findLeaves(dirname: String, suffix: String): List[String] = {
    val dir = new File(dirname)
    var result = new ListBuffer[String]()
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

  def convertStreamToString(is: InputStream): String = is match {
    case null => ""
    case _ => {
      val sb = new StringBuilder()
      var line = ""
      try {
        val reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))
        var keepGoing = true
        while (keepGoing) {
          line = reader.readLine()
          if (line != null) {
            sb.append(line).append("\n")
          } else {
            keepGoing = false
          }
        }
      } finally {
        is.close()
      }
      sb.toString()
    }
  }

  def matchAndTransform(string: String, regex: Regex, transform: (String, Regex.Match) => String): String = {

    var last_end = -1;
    var result = ""

    for (md <- regex.findAllIn(string).matchData) {
      if (last_end == -1) {
        result += string.substring(0, md.start)
      } else {
        result += string.substring(last_end, md.start)
      }
      result += transform(string, md)
      last_end = md.end
    }

    last_end match {
      case -1 => string
      case _ => {
        if (last_end < string.length) {
          result += string.substring(last_end, string.length)
        }
        result
      }
    }

  }

}
