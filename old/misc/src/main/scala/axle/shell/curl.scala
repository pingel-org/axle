package axle.shell

import axle.ShellCommand

case class curl(url: String) extends ShellCommand {
  def exec() = {
    1
  }
}
