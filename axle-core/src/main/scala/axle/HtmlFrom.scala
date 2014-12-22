package axle

trait HtmlFrom[T] {
  def toHtml(a: T): xml.Node
}

object HtmlFrom {

  // default Html just embeds string (from Show) in Text node
  implicit def showToHtmlFrom[T: Show]: HtmlFrom[T] = new HtmlFrom[T] {
    def toHtml(a: T): xml.Node = xml.Text(string(a))
  }

}