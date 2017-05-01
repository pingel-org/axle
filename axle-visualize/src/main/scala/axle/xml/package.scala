package axle

package object xml {

  import scala.xml._

  def elem(label: String, attributes: List[(String, String)], children: Node*): Elem = {
    val baseElem = Elem(null, label, Null, TopScope, true, children: _*)
    elemWithAttributes(baseElem, attributes.map({ case (n, v) => attribute(n, v) }).reverse)
  }

  def attribute(name: String, value: String): Attribute =
    Attribute(None, name, Text(value), Null)

  def elemWithAttributes(e: Elem, attrs: List[Attribute]): Elem =
    attrs match {
      case Nil          => e
      case head :: tail => elemWithAttributes(e % head, tail)
    }

}