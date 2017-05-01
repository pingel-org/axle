package axle.web

import java.awt.Dimension

import scala.collection.JavaConverters.collectionAsScalaIterableConverter
import scala.xml.NodeSeq
import scala.xml.NodeSeq.seqToNodeSeq
import scala.annotation.implicitNotFound

import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.visualization.DefaultVisualizationModel
import edu.uci.ics.jung.graph.DirectedSparseGraph
import edu.uci.ics.jung.graph.UndirectedSparseGraph

import cats.Show
import cats.kernel.Eq

import spire.implicits.DoubleAlgebra
import spire.algebra.Field

import axle.HtmlFrom
import axle.string
import axle.visualize.BarChart
import axle.visualize.BarChartGrouped
import axle.visualize.BarChartGroupedView
import axle.visualize.BarChartView
import axle.visualize.Color
import axle.visualize.Color.black
import axle.visualize.Color.lightGray
import axle.visualize.Color.yellow
import axle.visualize.KMeansVisualization
import axle.visualize.Plot
import axle.visualize.PlotView
import axle.visualize.Point2D
import axle.visualize.ScatterPlot
import axle.visualize.angleDouble
import axle.visualize.element.BarChartGroupedKey
import axle.visualize.element.BarChartKey
import axle.visualize.element.DataLines
import axle.visualize.element.DataPoints
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Key
import axle.visualize.element.Oval
import axle.visualize.element.Rectangle
import axle.visualize.element.Text
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import axle.arcTangent2
import axle.algebra.DirectedGraph
import axle.pgm.BayesianNetwork
import axle.pgm.BayesianNetworkNode
import axle.syntax.directedgraph.directedGraphOps
import axle.syntax.undirectedgraph.undirectedGraphOps
import axle.jung.undirectedGraphJung
import axle.xml._

@implicitNotFound("Witness not found for SVG[${S}]")
trait SVG[S] {

  def svg(s: S): NodeSeq

}

object SVG {

  final def apply[S](implicit svg: SVG[S]): SVG[S] = svg

  def rgb(color: Color): String = s"rgb(${color.r},${color.g},${color.b})"

  implicit def svgDataLines[S, X, Y, D]: SVG[DataLines[S, X, Y, D]] =
    new SVG[DataLines[S, X, Y, D]] {
      def svg(dl: DataLines[S, X, Y, D]): NodeSeq = {

        import dl._

        val pointRadius = pointDiameter / 2

        data.flatMap {
          case (s, d) => {
            val xs = orderedXs(d).toList
            val centers = xs.map(x => scaledArea.framePoint(Point2D(x, x2y(d, x))))
            val points = (centers map { c => s"${c.x},${c.y}" }).mkString(" ")
            val color = colorOf(s)
            val polyline = <polyline points={ s"$points" } fill="none" stroke={ s"${rgb(color)}" } stroke-width="1"/>
            val pointCircles =
              if (pointRadius > 0) {
                centers map { c =>
                  <circle cx={ s"${c.x}" } cy={ s"${c.y}" } r={ s"${pointRadius}" } fill={ s"${rgb(color)}" }/>
                }
              } else {
                List.empty
              }
            polyline :: pointCircles
          }
        }
      }
    }

  implicit def svgDataPoints[S: Show, X, Y, D]: SVG[DataPoints[S, X, Y, D]] =
    new SVG[DataPoints[S, X, Y, D]] {
      def svg(dl: DataPoints[S, X, Y, D]): NodeSeq = {

        import dl._

        val domain = dataView.dataToDomain(data)

        val circles = domain.toList.zipWithIndex.flatMap {
          case ((x, y), i) => {
            val center = scaledArea.framePoint(Point2D(x, y))
            val pointRadius = diameterOf(x, y) / 2
            val color = colorOf(x, y)
            if (pointRadius > 0) {
              val baseCircle = <circle cx={ s"${center.x}" } cy={ s"${center.y}" } r={ s"${pointRadius}" } fill={ s"${rgb(color)}" }/>
              labelOf(x, y) map {
                case (label, permanent) =>
                  val idCircle = elemWithAttributes(baseCircle, attribute("id", s"circle$i") :: Nil)
                  if (permanent) {
                    idCircle
                  } else {
                    elemWithAttributes(idCircle, attribute("onmousemove", s"ShowTooltip(evt, $i)") :: attribute("onmouseout", s"HideTooltip(evt, $i)") :: Nil)
                  }
              } getOrElse {
                baseCircle
              }
            } else {
              List.empty
            }
          }
        }

        val labels = domain.toList.zipWithIndex.flatMap {
          case ((x, y), i) => {
            val center = scaledArea.framePoint(Point2D(x, y))
            val pointRadius = diameterOf(x, y) / 2
            if (pointRadius > 0) {
              labelOf(x, y) map {
                case (label, permanent) =>
                  elem("text",
                    "class" -> "pointLabel" ::
                      "id" -> (if (permanent) ("pointLabel" + i) else ("tooltiptext" + i)) ::
                      "x" -> s"${center.x + pointRadius}" ::
                      "y" -> s"${center.y - pointRadius}" ::
                      "visibility" -> (if (permanent) "visible" else "hidden") :: Nil,
                    xml.Text(string(label)))
              }
            } else {
              List.empty
            }
          }
        }

        circles ++ labels
      }
    }

  implicit def svgKey[S: Show, X, Y, D]: SVG[Key[S, X, Y, D]] =
    new SVG[Key[S, X, Y, D]] {
      def svg(key: Key[S, X, Y, D]): NodeSeq = {

        import key._

        val lineHeight = plot.fontSize

        val keyTop = plot.keyTopPadding + lineHeight * (if (key.title.isDefined) 1 else 0)

        val ktto = key.title map { kt =>
          <text x={ s"${plot.width - key.width}" } y={ s"${keyTop}" } font-size={ s"${lineHeight}" }>{ kt }</text>
        } toList

        val keyEntries = data.zipWithIndex map {
          case ((label, d), i) => {
            val color = colorOf(label)
            <text x={ s"${plot.width - width}" } y={ s"${topPadding + plot.fontSize * (i + 1)}" } fill={ s"${rgb(color)}" } font-size={ s"${plot.fontSize}" }>{ string(label) }</text>
          }
        }

        ktto ++ keyEntries
      }
    }

  implicit def svgBarChartKey[S, Y, D, H]: SVG[BarChartKey[S, Y, D, H]] =
    new SVG[BarChartKey[S, Y, D, H]] {
      def svg(key: BarChartKey[S, Y, D, H]): NodeSeq = {

        import key._
        import chart._

        val lineHeight = normalFontSize

        val keyTop = keyTopPadding + lineHeight * (if (keyTitle.isDefined) 1 else 0)

        val ktto = keyTitle map { kt =>
          <text x={ s"${width - keyWidth}" } y={ s"${keyTop}" } font-size={ s"${lineHeight}" }>{ kt }</text>
        } toList

        val keyEntries = slices.toList.zipWithIndex map {
          case (slice, i) => {
            val color = colorOf(slice)
            <text x={ s"${width - keyWidth}" } y={ s"${keyTop + lineHeight * (i + 1)}" } fill={ s"${rgb(color)}" } font-size={ s"${lineHeight}" }>{ string(slice) }</text>
          }
        }

        ktto ++ keyEntries
      }
    }

  implicit def svgBarChartGroupedKey[G, S, Y, D, H]: SVG[BarChartGroupedKey[G, S, Y, D, H]] =
    new SVG[BarChartGroupedKey[G, S, Y, D, H]] {
      def svg(key: BarChartGroupedKey[G, S, Y, D, H]): NodeSeq = {
        import key._
        import chart._
        val lineHeight = chart.normalFontSize

        val keyTop = keyTopPadding + lineHeight * (if (keyTitle.isDefined) 1 else 0)

        val ktto = keyTitle map { kt =>
          <text x={ s"${width - keyWidth}" } y={ s"${keyTop}" } font-size={ s"${lineHeight}" }>{ kt }</text>
        } toList

        val keyEntries = for {
          (slice, i) <- slices.toList.zipWithIndex
          (group, j) <- groups.toList.zipWithIndex
        } yield {
          val color = colorOf(group, slice)
          val r = i * groups.size + j
          <text x={ s"${width - keyWidth}" } y={ s"${keyTop + lineHeight * (r + 1)}" } fill={ s"${rgb(color)}" } font-size={ s"${lineHeight}" }>{ string(group) + " " + string(slice) }</text>
        }
        ktto ++ keyEntries
      }
    }

  implicit def svgText: SVG[Text] =
    new SVG[Text] {

      def svg(t: Text): NodeSeq = {

        import t._

        val angled = angle.isDefined

        val textBase =
          elem("text",
            "x" -> s"$x" ::
              "y" -> s"$y" ::
              "text-anchor" -> (if (centered) "middle" else "left") ::
              "fill" -> s"${rgb(color)}" ::
              "font-size" -> s"${fontSize}" :: Nil,
            xml.Text(t.text))

        if (angle.isDefined) {
          import axle.visualize.angleDouble
          import spire.implicits._
          val twist = angle.get.in(angleDouble.degree).magnitude * -1d
          elemWithAttributes(textBase, attribute("transform", s"rotate($twist $x $y)") :: Nil)
        } else {
          textBase
        }
      }
    }

  implicit def svgHorizontalLine[X, Y]: SVG[HorizontalLine[X, Y]] =
    new SVG[HorizontalLine[X, Y]] {

      def svg(hl: HorizontalLine[X, Y]): NodeSeq = {
        import hl._
        val left = scaledArea.framePoint(Point2D(scaledArea.minX, h))
        val right = scaledArea.framePoint(Point2D(scaledArea.maxX, h))
        <line x1={ s"${left.x}" } y1={ s"${left.y}" } x2={ s"${right.x}" } y2={ s"${right.y}" } stroke={ s"${rgb(color)}" } stroke-width="1"/>
      }
    }

  implicit def svgVerticalLine[X, Y]: SVG[VerticalLine[X, Y]] =
    new SVG[VerticalLine[X, Y]] {

      def svg(vl: VerticalLine[X, Y]): NodeSeq = {
        import vl._
        val bottom = scaledArea.framePoint(Point2D(v, scaledArea.minY))
        val top = scaledArea.framePoint(Point2D(v, scaledArea.maxY))
        <line x1={ s"${bottom.x}" } y1={ s"${bottom.y}" } x2={ s"${top.x}" } y2={ s"${top.y}" } stroke={ s"${rgb(color)}" } stroke-width="1"/>
      }
    }

  implicit def svgRectangle[X, Y]: SVG[Rectangle[X, Y]] =
    new SVG[Rectangle[X, Y]] {

      def svg(rectangle: Rectangle[X, Y]): NodeSeq = {

        import rectangle.scaledArea
        val ll = scaledArea.framePoint(rectangle.lowerLeft)
        val ur = scaledArea.framePoint(rectangle.upperRight)
        val width = ur.x - ll.x
        val height = ll.y - ur.y

        val rectBase =
          elem("rect",
            "x" -> s"${ll.x}" ::
              "y" -> s"${ur.y}" ::
              "width" -> s"$width" ::
              "height" -> s"$height" ::
              "stroke-width" -> "1" :: Nil)

        val rectBordered = elemWithAttributes(rectBase, attribute("stroke", (rectangle.borderColor.map(bc => s"${rgb(bc)}").getOrElse("black"))) :: Nil)
        val rectFilled = rectangle.fillColor.map(fc => elemWithAttributes(rectBordered, attribute("fill", s"${rgb(fc)}") :: Nil)).getOrElse(rectBordered)

        rectangle.id.map({
          case (id, hoverText) =>
            elemWithAttributes(rectFilled,
              attribute("id", s"rect$id") :: attribute("onmousemove", s"ShowTooltip(evt, $id)") :: attribute("onmouseout", s"HideTooltip(evt, $id)") :: Nil)
        }).getOrElse(rectFilled)
      }
    }

  implicit def svgOval[X, Y]: SVG[Oval[X, Y]] =
    new SVG[Oval[X, Y]] {

      def svg(oval: Oval[X, Y]): NodeSeq = {
        import oval._
        val c = scaledArea.framePoint(center)
        <ellipse cx={ s"${c.x}" } cy={ s"${c.y}" } rx={ s"${width / 2}" } ry={ s"${height / 2}" } fill={ s"${rgb(oval.color)}" } stroke={ s"${rgb(oval.borderColor)}" } stroke-width="1"/>
      }
    }

  implicit def svgKMeans[D, F, G, M]: SVG[KMeansVisualization[D, F, G, M]] =
    new SVG[KMeansVisualization[D, F, G, M]] {

      def svg(kmv: KMeansVisualization[D, F, G, M]): NodeSeq = {

        import kmv._

        val nodes = (SVG[Rectangle[Double, Double]].svg(boundingRectangle) ::
          SVG[XTics[Double, Double]].svg(xTics) ::
          SVG[YTics[Double, Double]].svg(yTics) ::
          (centroidOvals map { SVG[Oval[Double, Double]].svg }) ::
          (points.toList map { SVG[Oval[Double, Double]].svg })).flatten.reduce(_ ++ _)

        svgFrame(nodes, width, height)
      }
    }

  implicit def svgScatterPlot[S: Show, X, Y, D]: SVG[ScatterPlot[S, X, Y, D]] =
    new SVG[ScatterPlot[S, X, Y, D]] {

      def svg(scatterPlot: ScatterPlot[S, X, Y, D]): NodeSeq = {

        import scatterPlot._

        val border: Seq[xml.Node] = if (drawBorder) {
          SVG[HorizontalLine[X, Y]].svg(hLine) ++
            SVG[VerticalLine[X, Y]].svg(vLine)
        } else {
          Nil
        }

        val xtics: Seq[xml.Node] = if (drawXTics) {
          SVG[XTics[X, Y]].svg(xTics)
        } else {
          Nil
        }

        val ytics: Seq[xml.Node] = if (drawYTics) {
          SVG[YTics[X, Y]].svg(yTics)
        } else {
          Nil
        }

        val nodes =
          (border :: xtics :: ytics ::
            SVG[DataPoints[S, X, Y, D]].svg(dataPoints) ::
            List(
              titleText.map(SVG[Text].svg),
              xAxisLabelText.map(SVG[Text].svg),
              yAxisLabelText.map(SVG[Text].svg)).flatten).reduce(_ ++ _)

        svgFrame(nodes, width, height)
      }
    }

  implicit def svgPlot[S, X, Y, D]: SVG[Plot[S, X, Y, D]] = new SVG[Plot[S, X, Y, D]] {

    def svg(plot: Plot[S, X, Y, D]): NodeSeq = {

      import plot._

      val view = PlotView(plot, plot.initialValue)

      import view._

      val nodes =
        (SVG[HorizontalLine[X, Y]].svg(hLine) ::
          SVG[VerticalLine[X, Y]].svg(vLine) ::
          SVG[XTics[X, Y]].svg(xTics) ::
          SVG[YTics[X, Y]].svg(yTics) ::
          SVG[DataLines[S, X, Y, D]].svg(dataLines) ::
          List(
            titleText.map(SVG[Text].svg),
            xAxisLabelText.map(SVG[Text].svg),
            yAxisLabelText.map(SVG[Text].svg),
            view.keyOpt.map(SVG[Key[S, X, Y, D]].svg)).flatten).reduce(_ ++ _)

      svgFrame(nodes, width, height)
    }
  }

  implicit def svgXTics[X, Y]: SVG[XTics[X, Y]] =
    new SVG[XTics[X, Y]] {

      def svg(xt: XTics[X, Y]): NodeSeq = {

        import xt._
        import scaledArea._

        tics flatMap {
          case (x, label) => {

            val bottom = framePoint(Point2D(x, minY))

            val tic = <line x1={ s"${bottom.x}" } y1={ s"${bottom.y - 2}" } x2={ s"${bottom.x}" } y2={ s"${bottom.y + 2}" } stroke={ s"${rgb(lightGray)}" } stroke-width="1"/>

            import axle.visualize.angleDouble

            val angled = angle.magnitude != 0d

            val text = elem("text", List(
              "text-anchor" -> (if (angled) "start" else "middle"),
              "alignment-baseline" -> "hanging",
              "x" -> bottom.x.toString,
              "y" -> (if (angled) bottom.y else bottom.y + 3).toString,
              "font-size" -> fontSize.toString) ++
              (if (angled) List("transform" -> s"rotate(${angle.in(angleDouble.degree).magnitude},${bottom.x},${bottom.y})") else Nil),
              xml.Text(label))

            if (drawLines) {
              val top = scaledArea.framePoint(Point2D(x, maxY))
              val line = <line x1={ s"${bottom.x}" } y1={ s"${bottom.y}" } x2={ s"${top.x}" } y2={ s"${top.y}" } stroke={ s"${rgb(lightGray)}" } stroke-width="1"/>
              line ++ List(text, tic)
            } else {
              List(text, tic)
            }
          }
        }
      }
    }

  implicit def svgYTics[X, Y]: SVG[YTics[X, Y]] =
    new SVG[YTics[X, Y]] {

      def svg(yt: YTics[X, Y]): NodeSeq = {

        import yt._
        import scaledArea._

        tics.flatMap({
          case (y, label) => {

            val left = framePoint(Point2D(minX, y))
            val right = framePoint(Point2D(maxX, y))

            val ticAndText = List(
              <text text-anchor="end" alignment-baseline="middle" x={ s"${left.x - 5}" } y={ s"${left.y}" } font-size={ s"${fontSize}" }>{ label }</text>,
              <line x1={ s"${left.x - 2}" } y1={ s"${left.y}" } x2={ s"${left.x + 2}" } y2={ s"${left.y}" } stroke={ s"${rgb(lightGray)}" } stroke-width="1"/>)

            if (drawLines) {
              val line = <line x1={ s"${left.x}" } y1={ s"${left.y}" } x2={ s"${right.x}" } y2={ s"${right.y}" } stroke={ s"${rgb(lightGray)}" } stroke-width="1"/>
              line :: ticAndText
            } else {
              ticAndText
            }
          }
        })
      }
    }

  implicit def svgBarChart[C, Y, D, H]: SVG[BarChart[C, Y, D, H]] =
    new SVG[BarChart[C, Y, D, H]] {

      def svg(chart: BarChart[C, Y, D, H]): NodeSeq = {

        import chart._

        val view = BarChartView(chart, chart.initialValue)

        import view._

        val nodes =
          SVG[HorizontalLine[Double, Y]].svg(hLine) ::
            SVG[VerticalLine[Double, Y]].svg(vLine) ::
            SVG[XTics[Double, Y]].svg(gTics) ::
            SVG[YTics[Double, Y]].svg(yTics) ::
            bars.map(SVG[Rectangle[Double, Y]].svg).flatten ::
            (for {
              bar <- bars
              (id, hoverText) <- bar.id
            } yield {
              // TODO if .svg has the notion of "layers", then
              // Rectangle's svg could handle this <text/> node creation
              import bar.scaledArea
              val ll = scaledArea.framePoint(bar.lowerLeft)
              val ur = scaledArea.framePoint(bar.upperRight)
              val width = ur.x - ll.x
              val height = ll.y - ur.y
              <g>
                <rect id={ s"tooltipbg${id}" } x="0" y="0" width="0" height="0" visibility="hidden" fill="white"/>
                <text class="pointLabel" id={ s"tooltiptext${id}" } x={ s"${ll.x}" } y={ s"${ll.y - height / 2}" } fill="black" visibility="hidden">{ hoverText }</text>
              </g>
            }) ::
            List(
              keyOpt.map(SVG[BarChartKey[C, Y, D, H]].svg),
              titleText.map(SVG[Text].svg),
              xAxisLabelText.map(SVG[Text].svg),
              yAxisLabelText.map(SVG[Text].svg)).flatten

        svgFrame(nodes.reduce(_ ++ _), width, height)
      }
    }

  implicit def svgBarChartGrouped[G, S, Y, D, H]: SVG[BarChartGrouped[G, S, Y, D, H]] =
    new SVG[BarChartGrouped[G, S, Y, D, H]] {

      def svg(chart: BarChartGrouped[G, S, Y, D, H]): NodeSeq = {

        import chart._

        val view = BarChartGroupedView(chart, chart.initialValue)

        import view._

        val nodes =
          SVG[HorizontalLine[Double, Y]].svg(hLine) ::
            SVG[VerticalLine[Double, Y]].svg(vLine) ::
            SVG[XTics[Double, Y]].svg(gTics) ::
            SVG[YTics[Double, Y]].svg(yTics) ::
            bars.map(SVG[Rectangle[Double, Y]].svg).flatten ::
            (for {
              bar <- bars
              (id, hoverText) <- bar.id
            } yield {
              // TODO if .svg has the notion of "layers", then
              // Rectangle's svg could handle this <text/> node creation
              import bar.scaledArea
              val ll = scaledArea.framePoint(bar.lowerLeft)
              val ur = scaledArea.framePoint(bar.upperRight)
              val width = ur.x - ll.x
              val height = ll.y - ur.y
              <g>
                <rect id={ s"tooltipbg${id}" } x="0" y="0" width="0" height="0" visibility="hidden" fill="white"/>
                <text class="pointLabel" id={ s"tooltiptext${id}" } x={ s"${ll.x}" } y={ s"${ll.y - height / 2}" } fill="black" visibility="hidden">{ hoverText }</text>
              </g>
            }) ::
            List(
              keyOpt.map(SVG[BarChartGroupedKey[G, S, Y, D, H]].svg),
              titleText.map(SVG[Text].svg),
              xAxisLabelText.map(SVG[Text].svg),
              yAxisLabelText.map(SVG[Text].svg)).flatten

        svgFrame(nodes.reduce(_ ++ _), width, height)
      }
    }

  implicit def svgJungDirectedGraph[VP: Eq: HtmlFrom, EP: Show]: SVG[DirectedSparseGraph[VP, EP]] =
    new SVG[DirectedSparseGraph[VP, EP]] {

      def svg(jdsg: DirectedSparseGraph[VP, EP]): NodeSeq = {

        // TODO make these all configurable
        val width = 800
        val height = 800
        val border = 20
        val radius = 10
        val arrowLength = 10
        val color = yellow
        val borderColor = black
        val fontSize = 12

        val layout = new FRLayout(jdsg)
        layout.setSize(new Dimension(width, height))
        val visualization = new DefaultVisualizationModel(layout)

        val lines: List[xml.Node] = jdsg.getEdges.asScala.map { edge =>
          <line x1={ s"${layout.getX(jdsg.getSource(edge))}" } y1={ s"${layout.getY(jdsg.getSource(edge))}" } x2={ s"${layout.getX(jdsg.getDest(edge))}" } y2={ s"${layout.getY(jdsg.getDest(edge))}" } stroke={ s"${rgb(black)}" } stroke-width="1"/>
        } toList

        val arrows: List[xml.Node] = jdsg.getEdges.asScala.map { edge =>
          val height = layout.getY(jdsg.getSource(edge)) - layout.getY(jdsg.getDest(edge))
          val width = layout.getX(jdsg.getDest(edge)) - layout.getX(jdsg.getSource(edge))
          val svgRotationAngle = 180d - (arcTangent2(height, width) in angleDouble.degree).magnitude
          <polygon points={ s"${radius},0 ${radius + arrowLength},3 ${radius + arrowLength},-3" } fill="black" transform={ s"translate(${layout.getX(jdsg.getDest(edge))},${layout.getY(jdsg.getDest(edge))}) rotate($svgRotationAngle)" }/>
        } toList

        val circles: List[xml.Node] = jdsg.getVertices.asScala.map { vertex =>
          <circle cx={ s"${layout.getX(vertex)}" } cy={ s"${layout.getY(vertex)}" } r={ s"${radius}" } fill={ s"${rgb(color)}" } stroke={ s"${rgb(borderColor)}" } stroke-width="1"/>
        } toList

        val labels: List[xml.Node] = jdsg.getVertices.asScala.map { vertex =>
          val node = HtmlFrom[VP].toHtml(vertex)
          node match {
            case xml.Text(text) =>
              <text text-anchor="middle" alignment-baseline="middle" x={ s"${layout.getX(vertex)}" } y={ s"${layout.getY(vertex)}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ text }</text>
            case _ =>
              <foreignObject x={ s"${layout.getX(vertex)}" } y={ s"${layout.getY(vertex)}" } width="100%" height="100%">
                <html xmlns="http://www.w3.org/1999/xhtml">
                  { node }
                </html>
              </foreignObject>
          }
        } toList

        val edgeLabels: List[xml.Node] = jdsg.getEdges.asScala.map { edge =>
          val node = HtmlFrom[EP].toHtml(edge)
          val cx = (layout.getX(jdsg.getDest(edge)) - layout.getX(jdsg.getSource(edge))) * 0.6 + layout.getX(jdsg.getSource(edge))
          val cy = (layout.getY(jdsg.getDest(edge)) - layout.getY(jdsg.getSource(edge))) * 0.6 + layout.getY(jdsg.getSource(edge))
          node match {
            case xml.Text(text) =>
              <text text-anchor="middle" alignment-baseline="middle" x={ s"${cx}" } y={ s"${cy}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ text }</text>
            case _ =>
              <foreignObject x={ s"${cx}" } y={ s"${cy}" } width="100%" height="100%">
                { node }
              </foreignObject>
          }
        } toList

        val nodes = lines ++ arrows ++ circles ++ labels ++ edgeLabels

        svgFrame(nodes, width, height)
      }

    }

  implicit def svgJungUndirectedGraph[VP: Eq: HtmlFrom, EP: Show]: SVG[UndirectedSparseGraph[VP, EP]] = new SVG[UndirectedSparseGraph[VP, EP]] {

    def svg(jusg: UndirectedSparseGraph[VP, EP]): NodeSeq = {

      // TODO make these all configurable
      val width = 600
      val height = 600
      val border = 20
      val radius = 10
      val color = yellow
      val borderColor = black
      val fontSize = 12

      val layout = new FRLayout(jusg)
      layout.setSize(new Dimension(width, height))
      val visualization = new DefaultVisualizationModel(layout)

      val lines: List[xml.Node] = jusg.getEdges.asScala.map { edge =>
        val (v1, v2) = jusg.vertices(edge)
        <line x1={ s"${layout.getX(v1)}" } y1={ s"${layout.getY(v1)}" } x2={ s"${layout.getX(v2)}" } y2={ s"${layout.getY(v2)}" } stroke={ s"${rgb(black)}" } stroke-width="1"/>
      } toList

      val circles: List[xml.Node] = jusg.getVertices.asScala.map { vertex =>
        <circle cx={ s"${layout.getX(vertex)}" } cy={ s"${layout.getY(vertex)}" } r={ s"${radius}" } fill={ s"${rgb(color)}" } stroke={ s"${rgb(borderColor)}" } stroke-width="1"/>
      } toList

      val labels: List[xml.Node] = jusg.getVertices.asScala.map { vertex =>
        val node = HtmlFrom[VP].toHtml(vertex)
        node match {
          case xml.Text(t) =>
            <text text-anchor="middle" alignment-baseline="middle" x={ s"${layout.getX(vertex)}" } y={ s"${layout.getY(vertex)}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ axle.html(vertex) }</text>
          case _ =>
            <foreignObject x={ s"${layout.getX(vertex)}" } y={ s"${layout.getY(vertex)}" } width="100%" height="100%">
              { node }
            </foreignObject>
        }
      } toList

      val edgeLabels: List[xml.Node] = jusg.getEdges.asScala.map { edge =>
        val node = HtmlFrom[EP].toHtml(edge)
        val (v1, v2) = jusg.vertices(edge)
        val cx = (layout.getX(v2) - layout.getX(v1)) * 0.5 + layout.getX(v1)
        val cy = (layout.getY(v2) - layout.getY(v1)) * 0.5 + layout.getY(v1)
        node match {
          case xml.Text(text) =>
            <text text-anchor="middle" alignment-baseline="middle" x={ s"${cx}" } y={ s"${cy}" } fill={ s"${rgb(black)}" } font-size={ s"${fontSize}" }>{ text }</text>
          case _ =>
            <foreignObject x={ s"${cx}" } y={ s"${cy}" } width="100%" height="100%">
              { node }
            </foreignObject>
        }
      } toList

      val nodes = lines ++ circles ++ labels ++ edgeLabels

      svgFrame(nodes, width, height)
    }

  }

  implicit def svgPgmEdge: SVG[axle.pgm.Edge] =
    new SVG[axle.pgm.Edge] {
      def svg(e: axle.pgm.Edge): NodeSeq =
        NodeSeq.Empty
    }

  implicit def drawBayesianNetwork[T: Manifest: Eq, N: Field: Manifest: Eq, DG](
    implicit svgDG: SVG[DG], dg: DirectedGraph[DG, BayesianNetworkNode[T, N], axle.pgm.Edge]): SVG[BayesianNetwork[T, N, DG]] = {
    new SVG[BayesianNetwork[T, N, DG]] {
      def svg(bn: BayesianNetwork[T, N, DG]): NodeSeq =
        svgDG.svg(bn.graph)
    }
  }

}
