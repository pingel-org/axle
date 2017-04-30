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

  val svgStyle = """
.caption{
  font-size: 14px;
  font-family: Georgia, serif;
}
"""

  val svgScript = """
  function init(evt) {
    if ( window.svgDocument == null ) {
      svgDocument = evt.target.ownerDocument;
    }
  }

  function ShowTooltip(evt, i) {
    tooltip = svgDocument.getElementById('tooltip' + i);
    //tooltip.setAttributeNS(null,"x",evt.clientX+10);
    //tooltip.setAttributeNS(null,"y",evt.clientY+30);
    tooltip.setAttributeNS(null,"visibility","visible");
  }

  function HideTooltip(evt, i) {
    tooltip = svgDocument.getElementById('tooltip' + i);
    tooltip.setAttributeNS(null,"visibility","hidden");
  }
"""

  // optional svg attribute: viewBox={ s"0 0 ${width} ${height}" }
  def svgFrame(inner: NodeSeq, width: Double, height: Double): Node =
    <svg version="1.1" xmlns="http://www.w3.org/2000/svg" width={ s"$width" } height={ s"$height" } onload="init(evt)">
      <style>{ svgStyle }</style>
      <script type="text/ecmascript">{ scala.xml.Unparsed("<![CDATA[%s]]>".format(svgScript)) }</script>
      { inner }
      Sorry, your browser does not support inline SVG.
    </svg>

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

  def elem(label: String, attributes: List[(String, String)], children: xml.Node*): xml.Elem = {
    val baseElem = xml.Elem(null, label, xml.Null, xml.TopScope, children: _*)
    elemWithAttributes(baseElem, attributes.map({ case (n, v) => attribute(n, v) }).reverse)
  }

  def attribute(name: String, value: String): xml.Attribute =
    xml.Attribute(None, name, xml.Text(value), xml.Null)

  def elemWithAttributes(e: xml.Elem, attrs: List[xml.Attribute]): xml.Elem =
    attrs match {
      case Nil          => e
      case head :: tail => elemWithAttributes(e % head, tail)
    }

}
