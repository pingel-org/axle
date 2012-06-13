package axle.shell

import java.io._
import java.util.Scanner
import axle.ShellCommand

case class kill(pid: String) extends ShellCommand {
  def exec() = {
    1
  }
}
