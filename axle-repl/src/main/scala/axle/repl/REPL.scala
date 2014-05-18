
package axle.repl

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import scala.util.Properties.{ jdkHome, javaVersion, javaVmName }
import scala.collection.JavaConverters._
import java.io.{ BufferedReader, StringReader, PrintWriter }
import jline.console.ConsoleReader

object AxleRepl extends App {

  val settings = new Settings

  settings.usejavacp.value = true
  settings.deprecation.value = true
  settings.Xnojline.value = false

  val code = List(
    "axle._",
    "axle.algebra._",
    "axle.stats._",
    "axle.quanta._",
    "axle.graph._",
    "axle.matrix._",
    "axle.ml._",
    "axle.visualize._",
    "axle.ast._",
    "scala.collection._") map { imp => s"import $imp; " } mkString

  val output = new PrintWriter(Console.out, true)
    
  val initialInput = new BufferedReader(new StringReader(code.trim + "\n"))
    
  val input = new BufferedReader(Console.in) {
    var initialized = false
    
    override def readLine() = {
      if(initialized) {
        super.readLine()
      } else {
        initialized = true
        initialInput.readLine()
      }
    }
  }

  val axleILoop = new AxleILoop(Some(input), output)
  axleILoop.process(settings)

}

class AxleILoop(in0: Option[BufferedReader], override protected val out: PrintWriter)
  extends ILoop(in0, out) {

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
      |Type :help for more information, or :quit to quit.""".trim.stripMargin)
    echo("[info] started at " + new java.util.Date)
  }
}
