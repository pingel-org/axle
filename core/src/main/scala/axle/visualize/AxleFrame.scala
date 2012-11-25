package axle.visualize

import javax.swing.{ JFrame, JPanel }
import java.awt.{ Dimension, Component, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.JPanel

class BackgroundPanel(title: String) extends JPanel {
  override def paintComponent(g: Graphics): Unit = {
    // super.paintComponent(g)
    val g2d = g.asInstanceOf[Graphics2D]
    g2d.setColor(Color.black)
    g2d.drawString(title, 20, 20)
  }
}

class AxleFrame(width: Int = 1100, height: Int = 800, bgColor: Color = Color.white, title: String = "αχλε")
  extends JFrame(title) {

  def initialize(): Unit = {
    setBackground(bgColor)
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setSize(width, height)
    add(new BackgroundPanel(title))
  }

}