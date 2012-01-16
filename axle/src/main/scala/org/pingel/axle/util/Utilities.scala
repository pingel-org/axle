
package org.pingel.axle.util {

  import scala.collection._
  import java.lang.StringBuffer

  trait Printable {
    def print(s: String = ""): Unit
    def println(s: String = ""): Unit
    def indent(i: Int): Unit
  }

  class PrintableStringBuffer(sb: StringBuffer) extends Printable {
    def print(s: String = ""): Unit = {
      sb.append(s)
    }
    def println(s: String = ""): Unit = {
      sb.append(s + "\n")
    }
    def indent(i: Int): Unit = {
      (0 until i).map(x => sb.append("   "))
    }
  }
}
