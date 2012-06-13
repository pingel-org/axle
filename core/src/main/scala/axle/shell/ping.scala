package axle.shell

import java.io._
import java.util.Scanner
import axle.ShellCommand

case class ping(hostname: String) extends ShellCommand {
  def exec() = {
    1
  }
}
