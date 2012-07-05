
package axle.util {

  import collection._
  import java.lang.StringBuffer

  trait Printable {
    def print(s: String = ""): Unit
    def println(s: String = ""): Unit
    def indent(i: Int): Unit
  }

  class PrintableStringBuffer(sb: StringBuffer) extends Printable
  {
    
    def print(string: String) = sb.append(string)
    
    def println() = sb.append("\n")
    
    def println(string: String) = {
      sb.append(string)
      sb.append("\n")
    }
    
    override def toString() = sb.toString()
    
    def indent(c: Int) = for( i <- 0 until c ) {
      sb.append("   ")
    }
  }

  class PrintablePrintStream(ps: java.io.PrintStream) extends Printable
  {
    
    def print(string: String) = ps.print(string)
    
    def println() = ps.println()
    
    def println(string: String) = ps.println(string)
    
    def indent(c: Int) = for( i <- 0 to c-1 ) {
      ps.print("   ")
    }
    
  }
  
  object Stringer {

    def render(iterable: Iterable[_ <: Object], sep: String) = {
      var result = ""
      val it = iterable.iterator
      while (it.hasNext) {
        val o = it.next()
        result += o.toString()
        if (it.hasNext) {
          result += sep
        }
      }
      result
    }

  }

  /**
   * org.pinge.util.Lister is a trivial class that was helpful
   * when this library was implemented in Java.  It is deprecated,
   * and will be deleted as soon as the referring code is updated.
   */

  trait Lister[In, Out] {

    def function(in: In): Out

    def execute(input: Iterable[In]) = input.map({ x => function(x) }).toList

  }

  trait Collector[In, Out] {

    def function(in: In): Out

    def execute(input: Iterable[In]): Set[Out] = input.map({ x => function(x) }).toSet
  }

}
