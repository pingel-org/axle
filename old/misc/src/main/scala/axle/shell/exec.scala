package axle.shell

import axle.ShellCommand

case class exec(args: String*) extends ShellCommand {
  def exec() = {
    1
  }
}
