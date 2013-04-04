package axle.visualize

import collection._
import axle.actor.Defaults._
import akka.actor.Props
import axle.quanta.Time

case class BarChart[G, S, Y: Plottable](
  groups: Seq[G],
  slices: Seq[S],
  initialValue: Map[(G, S), Y],
  gLabeller: G => String = (g: G) => g.toString,
  sLabeller: S => String = (s: S) => s.toString,
  drawKey: Boolean = true,
  width: Int = 700,
  height: Int = 600,
  border: Int = 50,
  barWidthPercent: Double = 0.80,
  keyLeftPadding: Int = 20,
  keyTopPadding: Int = 50,
  keyWidth: Int = 80,
  title: Option[String] = None,
  normalFontName: String = "Courier New",
  normalFontSize: Int = 12,
  titleFontName: String = "Palatino",
  titleFontSize: Int = 20,
  xAxis: Y,
  xAxisLabel: Option[String] = None,
  yAxisLabel: Option[String] = None,
  refresher: Option[(Map[(G, S), Y] => Map[(G, S), Y], Time.Q)] = None) {

  val dataFeedActor = system.actorOf(Props(new DataFeedActor(initialValue, refresher)))

}
