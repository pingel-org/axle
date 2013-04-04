package axle

import java.io._

case class and(left: ShellCommand, right: ShellCommand) extends ShellCommand {
  def exec() = {
    val leftExitStatus = left.exec()
    if (leftExitStatus == 0) {
      right.exec()
    } else {
      leftExitStatus
    }
  }
}

case class or(left: ShellCommand, right: ShellCommand) extends ShellCommand {
  def exec() = {
    if (left.exec() == 0) {
      0
    } else {
      right.exec()
    }
  }
}

case class pipe(from: ShellCommand, to: ShellCommand) extends ShellCommand {
  def exec() = {
    // TODO
    1
  }
}

case class redirect(cmd: ShellCommand, path: String) extends ShellCommand {
  def exec() = {
    val writer = new OutputStreamWriter(new FileOutputStream(path), "UTF-8")
    val exitStatus = cmd.exec() // TODO: supply writer as stdout
    try {
      writer.write("TODO")
    } finally {
      writer.close()
    }
    1 // TODO
  }
}

abstract class ShellCommand {

  // TODO create in/out/err per command
  lazy val stdin = System.in
  lazy val stdout = System.out
  lazy val stderr = System.err

  def !() = exec()

  def exec(): Int

  def &&(right: ShellCommand) = and(this, right)

  def ||(right: ShellCommand) = or(this, right)

  def |(to: ShellCommand) = pipe(this, to)

  def tee(path: String) = "TODO" // TODO

  // def 2>&1()

  def >(path: String) = redirect(this, path)

}
