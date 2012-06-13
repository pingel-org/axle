package axle.shell

import axle.ShellCommand

case class echo(message: String) extends ShellCommand {
  def exec() = {
    stdout.println(message)
    0
  }
}