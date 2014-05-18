
package axle.repl

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import scala.tools.nsc.interpreter.JLineReader
import scala.tools.nsc.interpreter.JLineCompletion
import scala.tools.nsc.interpreter.InteractiveReader
import scala.tools.nsc.interpreter.NoCompletion
import scala.tools.nsc.interpreter.SimpleReader
import scala.util.Properties.{ jdkHome, javaVersion, javaVmName }
import scala.collection.JavaConverters._
import java.io.{ BufferedReader, StringReader, PrintWriter }
import jline.console.ConsoleReader

object AxleRepl extends App {

  val settings = new Settings

  settings.usejavacp.value = true
  settings.deprecation.value = true
  settings.Xnojline.value = false

  val axleILoop = new AxleILoop()
  axleILoop.process(settings)

}

class AxleILoop // (override protected val out: PrintWriter)
  extends ILoop { // (None, out) {

  override def prompt =
    System.getProperty("file.encoding").toLowerCase match {
      case "utf-8" | "utf8" => "αχλε ↦ "
      case _ => "axle > "
    }

  override def chooseReader(settings: Settings): InteractiveReader =
    try new JLineReader(
      if (settings.noCompletion)
        NoCompletion
      else
        new JLineCompletion(intp)) {
      override val consoleReader = new JLineConsoleReader() {
        override lazy val postInit: Unit = {
          //super.postInit
          println("in AxleILoop postInit !!!!!!!!!!!!!!!!!")
        }
      }
      var initialized = false
      override def readOneLine(prompt: String) =
        if (initialized) {
          consoleReader readLine prompt
        } else {
          initialized = true
          println("in AxleILoop readOneLine with initialized = false !!!!!!!!!!!!!!!!!")
          //          List(
          //            "axle._",
          //            "axle.algebra._",
          //            "axle.stats._",
          //            "axle.quanta._",
          //            "axle.graph._",
          //            "axle.matrix._",
          //            "axle.ml._",
          //            "axle.visualize._",
          //            "axle.ast._",
          //            "scala.collection._") foreach { pkgExpr =>
          //              intp.interpret(s"import pkgExpr")
          //            }
          intp.interpret("4 + 4")
          consoleReader readLine prompt
        }
    }
    catch {
      case ex @ (_: Exception | _: NoClassDefFoundError) =>
        echo("Failed to created JLineReader: " + ex + "\nFalling back to SimpleReader.")
        SimpleReader()
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
