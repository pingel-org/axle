package axle.visualize

import javax.swing.JFrame
import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D }
import javax.swing.{ JPanel, JFrame }
import java.awt.event.MouseEvent

import axle.ml.KMeans._

class KMeansVisualization[D](classifier: KMeansClassifier[D]) {

  val PAD = 50
  val WIDTH = 600
  val HEIGHT = 600
  val DIAMETER = 10 // of data points

  def draw(): Unit = {
    val frame = new JFrame("KMeans Clustering")
    frame.setBackground(Color.white)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(WIDTH, HEIGHT)
    frame.add(new KMeansPanel(classifier))
    //frame.pack()
    //frame.setLocationRelativeTo(null)
    frame.setVisible(true)
  }

  class KMeansPanel(classifier: KMeansClassifier[D]) extends JPanel {

    val colors = List(Color.blue, Color.red, Color.green, Color.orange, Color.pink, Color.yellow)

    // TODO: paintComponent is executed for many kinds of events that will not change the image

    def unscaled2scaled(x: Double, y: Double): (Int, Int) = {
      val xs = (x - classifier.colMins(0, 0)) / classifier.colRanges(0, 0)
      val ys = (x - classifier.colMins(0, 1)) / classifier.colRanges(0, 1)
      scaled2frame(xs, ys)
    }

    def scaled2frame(x: Double, y: Double): (Int, Int) = {
      val xp = PAD + (x * (WIDTH - 2 * PAD)).toInt
      val yp = PAD + (y * (HEIGHT - 2 * PAD)).toInt
      (xp, yp)
    }

    override def paintComponent(g: Graphics): Unit = {
      println("KMeansPanel.paintComponent")
      // super.paintComponent(g)
      val size = getSize()
      // val insets = getInsets()
      // val w = size.width - (insets.left + insets.right)
      // val h = size.height - (insets.top + insets.bottom)
      val g2d = g.asInstanceOf[Graphics2D]
      for (i <- 0 until classifier.K()) {
        // TODO: inefficient loop
        g2d.setColor(colors(i % colors.length))
        for (r <- 0 until classifier.scaledX.rows) {
          if (classifier.C(r, 0) == i) {
            // TODO figure out what to do when N > 2
            val (xp, yp) = scaled2frame(classifier.scaledX(r, 0), classifier.scaledX(r, 1))
            // g2d.fillOval(xp, yp, DIAMETER, DIAMETER)
            g2d.drawString(r.toString, xp, yp)
          }
        }
      }
      g2d.setColor(Color.black)
      val p0 = scaled2frame(0, 0)
      val p1 = scaled2frame(1, 1)
      g2d.drawRect(p0._1, p0._2, p1._1, p1._2)
    }
  }
}
