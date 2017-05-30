package axle

import scala.annotation.implicitNotFound
import scala.xml.Node
import scala.xml.Text
import cats.Show

@implicitNotFound("Witness not found for HtmlFrom[${T}]")
trait HtmlFrom[T] {

  def toHtml(a: T): Node
}

object HtmlFrom {

  final def apply[T: HtmlFrom]: HtmlFrom[T] = implicitly[HtmlFrom[T]]

  // default Html just embeds string (from Show) in Text node
  implicit def showToHtmlFrom[T: Show]: HtmlFrom[T] = new HtmlFrom[T] {
    def toHtml(a: T): Node = Text(string(a))
  }

}
