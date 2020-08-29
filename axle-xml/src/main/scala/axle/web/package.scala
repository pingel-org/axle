package axle

import edu.uci.ics.jung.graph.DirectedSparseGraph
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import edu.uci.ics.jung.algorithms.layout.FRLayout

import scala.xml.NodeSeq
import scala.xml.Node
import scala.xml.XML
import scala.xml.Text
import scala.collection.JavaConverters._

import cats.Show
import cats.implicits._

import spire.algebra._

import axle.math.arcTangent2
//import axle.web.SVG
import axle.web.SVG.rgb
import axle.visualize.angleDouble
import axle.visualize.Color.black
import axle.visualize.GraphVertexLayout
import axle.visualize.DirectedGraphVisualization
import axle.visualize.UndirectedGraphVisualization

package object web {

  def html[T: HtmlFrom](t: T): scala.xml.Node = HtmlFrom[T].toHtml(t)

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

  implicit val trigDouble: Trig[Double] = spire.implicits.DoubleAlgebra
  implicit val mmDouble: MultiplicativeMonoid[Double] = spire.implicits.DoubleAlgebra

  implicit def svgJungDirectedGraphVisualization[VP: Eq: HtmlFrom, EP: Show]: SVG[DirectedGraphVisualization[DirectedSparseGraph[VP, EP], VP, EP]] =
    new SVG[DirectedGraphVisualization[DirectedSparseGraph[VP, EP], VP, EP]] {

      def svg(vis: DirectedGraphVisualization[DirectedSparseGraph[VP, EP], VP, EP]): NodeSeq = {

        import vis._

        val layout: GraphVertexLayout[Double, VP] =
          vis.layoutOpt.getOrElse {
             val fr = new FRLayout(dg)
             fr.setSize(new java.awt.Dimension(width, height))
             new GraphVertexLayout[Double, VP] {
               def x(v: VP): Double = fr.getX(v)
               def y(v: VP): Double = fr.getY(v)
             }
          }

        val lines: List[Node] = dg.getEdges.asScala.map { edge =>
          <line x1={ s"${layout.x(dg.getSource(edge))}" } y1={ s"${layout.y(dg.getSource(edge))}" } x2={ s"${layout.x(dg.getDest(edge))}" } y2={ s"${layout.y(dg.getDest(edge))}" } stroke={ s"${rgb(black)}" } stroke-width="1"/>
        } toList

        val arrows: List[Node] = dg.getEdges.asScala.map { edge =>
          val height = layout.y(dg.getSource(edge)) - layout.y(dg.getDest(edge))
          val width = layout.x(dg.getDest(edge)) - layout.x(dg.getSource(edge))
          val svgRotationAngle = 180d - (arcTangent2(height, width) in angleDouble.degree).magnitude
          <polygon points={ s"${radius},0 ${radius + arrowLength},3 ${radius + arrowLength},-3" } fill="black" transform={ s"translate(${layout.x(dg.getDest(edge))},${layout.y(dg.getDest(edge))}) rotate($svgRotationAngle)" }/>
        } toList

        val circles: List[Node] = dg.getVertices.asScala.map { vertex =>
          <circle cx={ s"${layout.x(vertex)}" } cy={ s"${layout.y(vertex)}" } r={ s"${radius}" } fill={ s"${rgb(color)}" } stroke={ s"${rgb(borderColor)}" } stroke-width="1"/>
        } toList

        val labels: List[Node] = dg.getVertices.asScala.map { vertex =>
          val node = HtmlFrom[VP].toHtml(vertex)
          node match {
            case scala.xml.Text(text) =>
              <text text-anchor="middle" alignment-baseline="middle" x={ s"${layout.x(vertex)}" } y={ s"${layout.y(vertex)}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ text }</text>
            case _ =>
              <foreignObject x={ s"${layout.x(vertex)}" } y={ s"${layout.y(vertex)}" } width="100%" height="100%">
                <html xmlns="http://www.w3.org/1999/xhtml">
                  { node }
                </html>
              </foreignObject>
          }
        } toList

        val edgeLabels: List[Node] = dg.getEdges.asScala.map { edge =>
          val node = HtmlFrom[EP].toHtml(edge)
          val cx = (layout.x(dg.getDest(edge)) - layout.x(dg.getSource(edge))) * 0.6 + layout.x(dg.getSource(edge))
          val cy = (layout.y(dg.getDest(edge)) - layout.y(dg.getSource(edge))) * 0.6 + layout.y(dg.getSource(edge))
          node match {
            case Text(text) =>
              <text text-anchor="middle" alignment-baseline="middle" x={ s"${cx}" } y={ s"${cy}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ text }</text>
            case _ =>
              <foreignObject x={ s"${cx}" } y={ s"${cy}" } width="100%" height="100%">
                { node }
              </foreignObject>
          }
        } toList

        val nodes = lines ++ arrows ++ circles ++ labels ++ edgeLabels

        axle.web.svgFrame(nodes, width.toDouble, height.toDouble)
      }

    }

  import axle.syntax.undirectedgraph.undirectedGraphOps
  import axle.syntax.directedgraph.directedGraphOps
  import axle.jung._

  
  implicit def svgJungUndirectedGraphVisualization[V: Eq: HtmlFrom, E: Show]: SVG[UndirectedGraphVisualization[UndirectedSparseGraph[V, E], V, E]] =
    new SVG[UndirectedGraphVisualization[UndirectedSparseGraph[V, E], V, E]] {

    def svg(vis: UndirectedGraphVisualization[UndirectedSparseGraph[V, E], V, E]): NodeSeq = {

      import vis._

      val layout = new FRLayout(ug)
      layout.setSize(new java.awt.Dimension(width, height))

      val lines: List[Node] = ug.getEdges.asScala.map { edge =>
        val (v1, v2): (V, V) = ug.vertices(edge)
        <line x1={ s"${layout.getX(v1)}" } y1={ s"${layout.getY(v1)}" } x2={ s"${layout.getX(v2)}" } y2={ s"${layout.getY(v2)}" } stroke={ s"${rgb(black)}" } stroke-width="1"/>
      } toList

      val circles: List[Node] = ug.getVertices.asScala.map { vertex =>
        <circle cx={ s"${layout.getX(vertex)}" } cy={ s"${layout.getY(vertex)}" } r={ s"${radius}" } fill={ s"${rgb(color)}" } stroke={ s"${rgb(borderColor)}" } stroke-width="1"/>
      } toList

      val labels: List[Node] = ug.getVertices.asScala.map { vertex =>
        val node = HtmlFrom[V].toHtml(vertex)
        node match {
          case Text(t) =>
            <text text-anchor="middle" alignment-baseline="middle" x={ s"${layout.getX(vertex)}" } y={ s"${layout.getY(vertex)}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ axle.web.html(vertex) }</text>
          case _ =>
            <foreignObject x={ s"${layout.getX(vertex)}" } y={ s"${layout.getY(vertex)}" } width="100%" height="100%">
              { node }
            </foreignObject>
        }
      } toList

      val edgeLabels: List[Node] = ug.getEdges.asScala.map { edge =>
        val node = HtmlFrom[E].toHtml(edge)

        val (v1, v2): (V, V) = ug.vertices(edge)
        val cx = (layout.getX(v2) - layout.getX(v1)) * 0.5 + layout.getX(v1)
        val cy = (layout.getY(v2) - layout.getY(v1)) * 0.5 + layout.getY(v1)
        node match {
          case Text(text) =>
            <text text-anchor="middle" alignment-baseline="middle" x={ s"${cx}" } y={ s"${cy}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ text }</text>
          case _ =>
            <foreignObject x={ s"${cx}" } y={ s"${cy}" } width="100%" height="100%">
              { node }
            </foreignObject>
        }
      } toList

      val nodes = lines ++ circles ++ labels ++ edgeLabels

      axle.web.svgFrame(nodes, width.toDouble, height.toDouble)
    }

  }

  import axle.pgm.BayesianNetworkNode
  import cats.Show
  import scala.xml.Node
  implicit def bnnHtmlFrom[T: Show, N]: HtmlFrom[BayesianNetworkNode[T, N]] =
    new HtmlFrom[BayesianNetworkNode[T, N]] {
      def toHtml(bnn: BayesianNetworkNode[T, N]): Node =
        <div>
          <h2>{ bnn.variable.name }</h2>
          <table border={ "1" }>
            <tr>{ bnn.cpt.variables.map(variable => <td>{ variable.name }</td>): scala.xml.NodeSeq }<td>P</td></tr>
            {
              bnn.cpt.cases.map(kase =>
                <tr>
                  { kase.map(ci => <td>{ show"${ci}" }</td>) }
                  <td>{ bnn.cpt(kase) }</td>
                </tr>)
            }
          </table>
        </div>
    }

}
