package axle.visualize

import javax.swing.{ JFrame, JPanel }
import java.awt.{ Dimension, Component, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel

class AxleFrame() {

  val FRAMEWIDTH = 1100
  val FRAMEHEIGHT = 800

  val frame = new JFrame("Axle")
  frame.setBackground(Color.white)
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  frame.setSize(FRAMEWIDTH, FRAMEHEIGHT)

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
      g2d.drawString("-- AXLE --", 20, 20)
    }
  }

}