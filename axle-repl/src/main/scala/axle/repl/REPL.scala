
package axle.repl

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop

object AxleRepl extends App {

  // System.setProperty("file.encoding", "UTF-8")
  
  val settings = new Settings

  settings.usejavacp.value = true
  settings.deprecation.value = true

  new AxleILoop().process(settings)

}

class AxleILoop extends ILoop {

  override def prompt = "αχλε ↦ "

  addThunk {
    intp.beQuietDuring {
      intp.addImports("axle._")
    }
  }

  override def printWelcome() {
    echo("""
        
Welcome to αχλε

""")
  }
}
