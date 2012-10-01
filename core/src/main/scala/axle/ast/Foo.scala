package axle.ast

import util.matching.Regex
import axle.Loggable

class Bar {}

object Foo extends Loggable {

  def doit(): Unit = {
    
    val s = "bc fidaa abmn fidabb nuaaabi fidjab bkjkj"

    def transform(s: String, m: Regex.Match): String = "$" + s.substring(m.start(1), m.end(1))

    val x = Util.matchAndTransform(s, """fid(\w+)""".r, transform)
    info(s)
    info(x)

    /*
     val t = List[String]("a", "b", "c")
     val u = t ::: List[String]("d", "e")
     info("u = " + u)
     
     val z = (x: Int) => x + 1
     
     //val abc = (x: List[String]): Int => { x.length }
     
     val score = new Function1[List[String], Int] {
     def apply(ss: List[String]): Int = ss.length
     }
     */

    //(ss: List[String]): Int => ss.length
    // (for (s <- ss) yield s.length()).foldLeft(0)(_+_)

    /*
     val element: String = "abc"
     val lines = element.split("\n")
     val x = lines.last
     */

    /*
     val y: String = "abc"
     val a: Int = y.indexOf("bc")
     info("a = " + a)
     */

    /*
     val list = List("A", "B")

     info("list(1) = " + list(1))
     
     val m = Map("X" -> 5, "Y" -> 8)
     
     info("m = " + m)
     
     for ( i <- m ) {
       info("k = " + i._1 + " m[k] = " + i._2)
     }
     */

    // val indentation_level: Int = 5
    // val x: String = ( List.range(0, indentation_level) map (i => "| -") ).mkString("")
    // val y: String = ( for ( x <- List.range(0, indentation_level)) yield "   " ).mkString("")
    // val tokens = new mutable.ListBuffer[String]()
    // tokens += "efoo"
    // tokens += "ebar"

    // val node2lineno = new mutable.Map[String, Int]()
    // node2lineno.update("x", 5)

    // val s : String = String.format("foo %s", "abc")
    // info("s = " + s)

  }

}
