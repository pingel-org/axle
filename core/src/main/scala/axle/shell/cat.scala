package axle.shell

import java.io._
import java.util.Scanner
import axle.ShellCommand

case class cat(path: String) extends ShellCommand {
  def exec() = {
    val scanner = new Scanner(new FileInputStream(path), "UTF-8")
    try {
      while (scanner.hasNextLine()) {
        stdout.println(scanner.nextLine())
      }
      // TODO catch error and set return value to 1
    } finally {
      scanner.close()
    }
    0
  }
}
