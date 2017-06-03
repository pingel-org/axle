package axle.awt

import java.awt.Dimension
import java.awt.Graphics2D
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Draw[${T}]")
trait Draw[T] {

  def dimension(t: T): Dimension

  def paint(t: T, g2d: Graphics2D): Unit
}

object Draw {

  final def apply[T](implicit draw: Draw[T]): Draw[T] = draw
}
