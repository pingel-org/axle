package axle.visualize.element

import axle.visualize.Color
import axle.visualize.Plot

case class Key[S, X, Y, D](
  plot:       Plot[S, X, Y, D],
  title:      Option[String],
  colorOf:    S => Color,
  width:      Int,
  topPadding: Int,
  data:       Seq[(S, D)])
