package axle.awt

import java.awt.Component
import java.awt.Dimension
import java.awt.Graphics2D
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for DrawPanel[${T}]")
trait DrawPanel[T] {

  def dimension(t: T): Dimension

  def paint(t: T, g2d: Graphics2D): Unit
}

@implicitNotFound("Witness not found for Draw[${T}]")
trait Draw[T] {

  def component(t: T): Component
}

object Draw {

  final def apply[T](implicit draw: Draw[T]): Draw[T] = draw

  implicit def fromPanel[T](implicit drawPanel: DrawPanel[T]): Draw[T] =
    new Draw[T] {
      def component(t: T) = AxlePanel(t)
    }
}
