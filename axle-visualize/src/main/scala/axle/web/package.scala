package axle

import scala.xml.Node
import scala.xml.NodeSeq
import scala.xml.XML

import axle.web.SVG

package object web {

  final val encoding = "UTF-8"

  def bodify(inner: NodeSeq): Node =
    <html>
      <body>{ inner }</body>
    </html>

  def svgFrame(inner: NodeSeq, width: Int, height: Int): Node =
    <svg width={ s"$width" } height={ s"$height" }>{ inner }Sorry, your browser does not support inline SVG.</svg>

  def svg[T: SVG](t: T, filename: String, width: Int, height: Int): Unit =
    XML.save(filename, svgFrame(SVG[T].svg(t), width, height), encoding, true, null)

  def svg[T: SVG](t: T, filename: String): Unit =
    SVG[T].svg(t) match {
      case svg: Node   => XML.save(filename, svg, encoding, true, null)
      case nodes: Node => XML.save(filename, svgFrame(nodes, 500, 500), encoding, true, null)
    }

  def html[T: SVG](t: T, filename: String): Unit =
    XML.save(filename, bodify(SVG[T].svg(t)), encoding, true, null)

}