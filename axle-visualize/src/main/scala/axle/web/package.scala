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
    <svg viewBox={ s"0 0 ${width} ${height}" } version="1.1" xmlns="http://www.w3.org/2000/svg" width={ s"$width" } height={ s"$height" }>{ inner }Sorry, your browser does not support inline SVG.</svg>

  def svg[T: SVG](t: T, filename: String, width: Int, height: Int): Unit =
    XML.save(filename, svgFrame(SVG[T].svg(t), width, height), encoding, true, null)

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