package axle.awt

import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

case class AxlePanel[T](t: T)(implicit draw: DrawPanel[T])
    extends JPanel {

  setMinimumSize(draw.dimension(t))

  override def paintComponent(g: Graphics): Unit =
    draw.paint(t, g.asInstanceOf[Graphics2D])

}
