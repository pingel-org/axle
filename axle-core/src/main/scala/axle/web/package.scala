package axle

import scala.xml.Node
import scala.xml.NodeSeq
import scala.xml.XML

package object web {

  final val encoding = "UTF-8"

  def bodify(inner: NodeSeq): Node =
    <html>
      <body>{ inner }</body>
    </html>

  val svgStyleStream = this.getClass.getResourceAsStream("/svgstyle.css")
  val svgStyle = io.Source.fromInputStream(svgStyleStream).mkString
  svgStyleStream.close()

  val svgScriptStream = this.getClass.getResourceAsStream("/svgfunctions.js")
  val svgScript = io.Source.fromInputStream(svgScriptStream).mkString
  svgScriptStream.close()

  // optional svg attribute: viewBox={ s"0 0 ${width} ${height}" }
  def svgFrame(inner: NodeSeq, width: Double, height: Double): Node =
    <svg version="1.1" xmlns="http://www.w3.org/2000/svg" width={ s"$width" } height={ s"$height" } onload="init(evt)">
      <style>{ svgStyle }</style>
      <script type="text/ecmascript">{ scala.xml.Unparsed("<![CDATA[%s]]>".format(svgScript)) }</script>
      { inner }
      Sorry, your browser does not support inline SVG.
    </svg>

  def svg[T: SVG](t: T, filename: String, width: Int, height: Int): Unit =
    XML.save(filename, svgFrame(SVG[T].svg(t), width.toDouble, height.toDouble), encoding, true, null)

  def svg[T: SVG](t: T, filename: String): Unit = {
    val nodes = SVG[T].svg(t)
    if (nodes.length == 1 && nodes.head.label == "svg") {
      XML.save(filename, nodes.head, encoding, true, null)
    } else {
      XML.save(filename, svgFrame(nodes, 500, 500), encoding, true, null)
    }
  }

  def html[T: SVG](t: T, filename: String): Unit =
    XML.save(filename, bodify(SVG[T].svg(t)), encoding, true, null)

}
