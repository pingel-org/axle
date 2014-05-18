
package axle.repl

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import scala.util.Properties.{ jdkHome, javaVersion, javaVmName }
import spire.implicits._

object AxleRepl extends App {

  val settings = new Settings

  settings.usejavacp.value = true
  settings.deprecation.value = true

//  val code = List(
//    "axle._",
//    "axle.algebra._",
//    "axle.stats._",
//    "axle.quanta._",
//    "axle.graph._",
//    "axle.matrix._",
//    "axle.ml._",
//    "axle.visualize._",
//    "axle.ast._",
//    "scala.collection._") map { imp =>
//      s"import $imp\n"
//    } mkString

  val axleILoop = new AxleILoop()
  axleILoop.process(settings)

}

class AxleILoop extends ILoop {

  override def prompt =
    System.getProperty("file.encoding").toLowerCase match {
    case "utf-8" | "utf8" => "αχλε ↦ "
    case _ => "axle > "
  }

  val versionString = "0.1"

  override def printWelcome(): Unit = {
    echo(s"""
      |Welcome to Axle $versionString ($javaVmName, Java $javaVersion).
      |axle-lang.org
      |Type in expressions to have them evaluated.
      |Type :help for more information.""".trim.stripMargin)
    echo("[info] started at " + new java.util.Date)
  }
}
