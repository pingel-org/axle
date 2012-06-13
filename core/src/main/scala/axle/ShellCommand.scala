package axle

import java.io._

abstract class ShellCommand {

  // TODO create in/out/err per command
  lazy val stdin = System.in
  lazy val stdout = System.out
  lazy val stderr = System.err

  def !() = exec()

  def exec(): Int

  // TODO &&
  
  // TODO ||
  
//  def >(path: String) = { // TODO
//    val out = new OutputStreamWriter(new FileOutputStream(path), "UTF-8")
//    try {
//      out.write("TODO")
//    } finally {
//      out.close()
//    }
//    this
//  }

  // def |(next: ShellCommand) = new cat("TODO").exec()

}
