package axle.awt

import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Draw[${T}]")
trait Draw[T] {

  def component(t: T): Component
}

object Draw {

  final def apply[T](implicit draw: Draw[T]): Draw[T] = draw

  implicit def fromPanel[T](implicit drawPanel: DrawPanel[T]): Draw[T] =
    new Draw[T] {
      def component(t: T): JPanel = new JPanel {

        setMinimumSize(drawPanel.dimension(t))

        override def paintComponent(g: Graphics): Unit =
          drawPanel.paint(t, g.asInstanceOf[Graphics2D])
      }
    }
}
