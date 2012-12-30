package axle.shell

import java.io._
import java.util.Scanner
import axle.ShellCommand

case class cd(path: String) extends ShellCommand {
  def exec() = {
    1
  }
}
