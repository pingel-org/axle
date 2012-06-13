package axle.shell

import java.io._
import axle.ShellCommand

case class pwd() extends ShellCommand {
  def exec() = {
    val cwd = new File(".")
    stdout.println(cwd.getCanonicalPath)
    0
  }
}
