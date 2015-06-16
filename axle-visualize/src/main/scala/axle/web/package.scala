package axle

import java.awt.Font

import scala.annotation.implicitNotFound
import scala.xml.Node
import scala.xml.NodeSeq
import scala.xml.XML

import axle.algebra.LengthSpace
import axle.algebra.Tics
import axle.algebra.Zero
import axle.visualize.Plot
import axle.visualize.PlotView
import axle.visualize.angleDouble
import axle.visualize.element.DataLines
import axle.visualize.element.HorizontalLine
import axle.visualize.element.Key
import axle.visualize.element.Text
import axle.visualize.element.VerticalLine
import axle.visualize.element.XTics
import axle.visualize.element.YTics
import spire.algebra.Eq

package object web {

  def bodify(inner: NodeSeq): Node =
    <html>
      <body>{ inner }</body>
    </html>

  def svgFrame(width: Int, height: Int, inner: NodeSeq): NodeSeq =
    <svg width={s"$width"} height={s"$height"}>{ inner }Sorry, your browser does not support inline SVG.</svg>

  def htmlPlot[X: Zero: Tics: Eq, Y: Zero: Tics: Eq, D](
    plot: Plot[X, Y, D],
    filename: String)(
      implicit xls: LengthSpace[X, _],
      yls: LengthSpace[Y, _]): Unit = {

    import plot._

    val normalFont = new Font(fontName, Font.BOLD, fontSize)
    val xAxisLabelText = xAxisLabel.map(Text(_, normalFont, width / 2, height - border / 2))
    val yAxisLabelText = yAxisLabel.map(Text(_, normalFont, 20, height / 2, angle = Some(90d *: angleDouble.degree)))
    val titleFont = new Font(titleFontName, Font.BOLD, titleFontSize)
    val titleText = title.map(Text(_, titleFont, width / 2, titleFontSize))

    val view = PlotView(plot, plot.initialValue, normalFont)

    import view._

    val nodes =
      SVG[HorizontalLine[X, Y]].svg(hLine) ::
        SVG[VerticalLine[X, Y]].svg(vLine) ::
        SVG[XTics[X, Y]].svg(xTics) ::
        SVG[YTics[X, Y]].svg(yTics) ::
        SVG[DataLines[X, Y, D]].svg(dataLines) ::
        List(
          titleText.map(SVG[Text].svg),
          xAxisLabelText.map(SVG[Text].svg),
          yAxisLabelText.map(SVG[Text].svg),
          view.keyOpt.map(SVG[Key[X, Y, D]].svg)).flatten

    XML.save(filename, bodify(svgFrame(width, height, nodes.reduce(_ ++ _))), "UTF-8", true, null)
  }
}