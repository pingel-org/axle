package axle.visualize

import collection._
import axle.akka.Defaults._
import akka.actor.Props

case class BarChart[X, S, Y: Plottable](
  xs: Seq[X],
  ss: Seq[S],
  y: (X, S) => Y,
  xLabeller: X => String = (x: X) => x.toString,
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
  yAxisLabel: Option[String] = None) {

  val dataFunction = () => (
    for {
      x <- xs
      s <- ss
    } yield (x, s) -> y(x, s)
  ).toMap

  val dataFeedActor = {
    system.actorOf(Props(new DataFeedActor(dataFunction)))
  }

}
