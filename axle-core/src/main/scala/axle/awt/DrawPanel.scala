package axle.awt

import java.awt.Dimension
import java.awt.Graphics2D
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for DrawPanel[${T}]")
trait DrawPanel[T] {

  def dimension(t: T): Dimension

  def paint(t: T, g2d: Graphics2D): Unit
}
