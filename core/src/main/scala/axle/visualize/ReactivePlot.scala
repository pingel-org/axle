package axle.visualize

import java.awt.Color
import Color._
import scala.collection.immutable.TreeMap
import akka.actor.Props

case class ReactivePlot[X: Plottable, Y: Plottable](
  dataFunction: () => List[(String, TreeMap[X, Y])],
  connect: Boolean = true,
  drawKey: Boolean = true,
  width: Int = 700,
  height: Int = 600,
  border: Int = 50,
  pointDiameter: Int = 4,
  keyLeftPadding: Int = 20,
  keyTopPadding: Int = 50,
  keyWidth: Int = 80,
  fontName: String = "Courier New",
  fontSize: Int = 12,
  titleFontName: String = "Palatino",
  titleFontSize: Int = 20,
  colors: Seq[Color] = List(blue, red, green, orange, pink, yellow),
  title: Option[String] = None,
  xAxis: Y,
  xAxisLabel: Option[String] = None,
  yAxis: X,
  yAxisLabel: Option[String] = None) {

  val dataFeedActor = AxleAkka.system.actorOf(Props(new DataFeedActor(dataFunction)))

}
