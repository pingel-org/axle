
package axle.repl

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop
import spire.implicits._

object AxleRepl extends App {

  val settings = new Settings

  settings.usejavacp.value = true
  settings.deprecation.value = true

  val axleILoop = new AxleILoop().process(settings)

  List(
    "axle._",
    "axle.algebra._",
    "axle.stats._",
    "axle.quanta._",
    "axle.graph._",
    "axle.matrix._",
    "axle.ml._",
    "axle.visualize._",
    "axle.ast._",
    "scala.collection._") foreach { imp =>
      //axleILoop.interpret(s"import $imp")
  }
  
}

class AxleILoop extends ILoop {

  override def prompt =
    if (System.getProperty("file.encoding").toLowerCase === "utf-8")
      "αχλε ↦ "
    else
      "axle > "
  
  override def printWelcome(): Unit = {
    echo("""

Welcome to axle

  axle-lang.org

""")
  }
}
