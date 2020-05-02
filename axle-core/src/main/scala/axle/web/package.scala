package axle

import scala.xml.NodeSeq
import scala.xml.Node
import scala.xml.XML


package object web {

  def bodify(inner: NodeSeq): Node =
    <html>
      <body>{ inner }</body>
    </html>

    // optional svg attribute: viewBox={ s"0 0 ${width} ${height}" }
  def svgFrame(inner: NodeSeq, width: Double, height: Double): Node = {

    val svgStyle = axle.IO.classpathResourceAsString("/svgstyle.css")
    val svgScript = axle.IO.classpathResourceAsString("/svgfunctions.js")

    <svg version="1.1" xmlns="http://www.w3.org/2000/svg" width={ s"$width" } height={ s"$height" } onload="init(evt)">
      <style>{ svgStyle }</style>
      <script type="text/ecmascript">{ scala.xml.Unparsed("<![CDATA[%s]]>".format(svgScript)) }</script>
      { inner }
      Sorry, your browser does not support inline SVG.
    </svg>
  }

  implicit class SVGSyntax[S: SVG](s: S) {

    final val encoding = "UTF-8"

    import cats.effect._
    def svg[F[_]: Sync](filename: String): F[Unit] = {
      Sync[F].delay {
        val nodes = SVG[S].svg(s)
        if (nodes.length == 1 && nodes.head.label == "svg") {
          XML.save(filename, nodes.head, encoding, true)
        } else {
          XML.save(filename, svgFrame(nodes, 500, 500), encoding, true)
        }
      }
    }

    def svg[F[_]: Sync](filename: String, width: Int, height: Int): F[Unit] =
      Sync[F].delay {
        XML.save(filename, svgFrame(SVG[S].svg(s), width.toDouble, height.toDouble), encoding, true)
      }

    def html[F[_]: Sync](filename: String): F[Unit] =
      Sync[F].delay {
        XML.save(filename, bodify(SVG[S].svg(s)), encoding, true)
      }

  }

}
