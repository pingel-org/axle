package axle.visualize

import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D

import scala.Vector
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import DataFeedProtocol.Fetch
import akka.pattern.ask
import axle.algebra.Tics
import axle.algebra.Zero
import axle.algebra.LengthSpace
import axle.actor.Defaults.askTimeout
import axle.algebra.Plottable
import axle.quanta.Angle.{ ° => ° }
import axle.quanta.UnittedQuantity
import axle.visualize.element.Text
import javax.swing.JPanel
import spire.algebra.Eq
import spire.math.Number.apply
import spire.implicits.DoubleAlgebra
import spire.implicits.moduleOps

class PlotComponent[X: Zero: Tics: Eq, Y: Zero: Tics: Eq, D](plot: Plot[X, Y, D])(
  implicit xls: LengthSpace[X, _], yls: LengthSpace[Y, _])
  extends JPanel
  with Fed[List[(String, D)]] {

  import plot._

  setMinimumSize(new Dimension(width, height))

  def initialValue = plot.initialValue

  val normalFont = new Font(fontName, Font.BOLD, fontSize)
  val xAxisLabelText = xAxisLabel.map(new Text(_, normalFont, width / 2, height - border / 2))
  val yAxisLabelText = yAxisLabel.map(new Text(_, normalFont, 20, height / 2, angle = Some(90d *: °[Double])))
  val titleFont = new Font(titleFontName, Font.BOLD, titleFontSize)
  val titleText = title.map(new Text(_, titleFont, width / 2, titleFontSize))

  override def paintComponent(g: Graphics): Unit = {

    val data = feeder map { dataFeedActor =>
      val dataFuture = (dataFeedActor ? Fetch()).mapTo[List[(String, D)]]
      // Getting rid of this Await is awaiting a better approach to integrating AWT and Akka
      Await.result(dataFuture, 1.seconds)
    } getOrElse (plot.initialValue)

    val view = new PlotView(plot, data, normalFont)
    import view._
    val paintables =
      Vector(vLine, hLine, xTics, yTics, dataLines) ++
        Vector(titleText, xAxisLabelText, yAxisLabelText, view.keyOpt).flatMap(i => i)

    val g2d = g.asInstanceOf[Graphics2D]
    paintables foreach { _.paint(g2d) }

  }

}
