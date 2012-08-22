package axle.visualize

import javax.swing.{ JFrame, JPanel }
import java.awt.{ Dimension, Component, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel

class AxleFrame(
  width: Int = 1100, height: Int = 800,
  bgColor: Color = Color.white, title: String = "αχλε") {

  val frame = new JFrame(title)
  frame.setBackground(bgColor)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  frame.setSize(width, height)

  add(new BackgroundPanel())

  def add(panel: JPanel): Unit = {
    frame.add(panel)
    frame.setVisible(true)
  }

  def add(component: Component): Unit = {
    frame.add(component)
    frame.setVisible(true)
  }

  class BackgroundPanel extends JPanel {
    override def paintComponent(g: Graphics): Unit = {
      // super.paintComponent(g)
      val g2d = g.asInstanceOf[Graphics2D]
      g2d.setColor(Color.black)
      g2d.drawString(title, 20, 20)
    }
  }

}