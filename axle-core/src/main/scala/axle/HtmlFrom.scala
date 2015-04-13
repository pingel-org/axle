package axle

import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for HtmlFrom[${T}]")
trait HtmlFrom[T] {

  def toHtml(a: T): xml.Node
}

object HtmlFrom {

  def apply[T: HtmlFrom]: HtmlFrom[T] = implicitly[HtmlFrom[T]]

  // default Html just embeds string (from Show) in Text node
  implicit def showToHtmlFrom[T: Show]: HtmlFrom[T] = new HtmlFrom[T] {
    def toHtml(a: T): xml.Node = xml.Text(string(a))
  }

}