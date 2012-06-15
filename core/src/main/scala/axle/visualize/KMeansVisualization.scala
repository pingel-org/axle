package axle.visualize

import javax.swing.JFrame
import java.awt.{ Dimension, BasicStroke, Color, Paint, Stroke, Insets, Graphics, Graphics2D, Point }
import javax.swing.{ JPanel, JFrame }
import java.awt.event.MouseEvent

import axle.ml.KMeans._

// http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html

class KMeansVisualization[D](classifier: KMeansClassifier[D]) {

  val PAD = 50
  val WIDTH = 600
  val HEIGHT = 600
  val DIAMETER = 10 // of data points

  val DRAWABLEWIDTH = WIDTH - (2 * PAD)
  val DRAWABLEHEIGHT = HEIGHT - (2 * PAD)

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

    def unscaled2scaled(x: Double, y: Double): Point = {
      val xs = (x - classifier.colMins(0, 0)) / classifier.colRanges(0, 0)
      val ys = (y - classifier.colMins(0, 1)) / classifier.colRanges(0, 1)
      scaled2frame(xs, ys)
    }

    def scaled2frame(x: Double, y: Double): Point = {
      val xp = PAD + (x * DRAWABLEWIDTH).toInt
      val yp = HEIGHT - PAD - (y * DRAWABLEHEIGHT).toInt
      new Point(xp, yp)
    }

    def boundingRectangle(g2d: Graphics2D): Unit = {
      g2d.setColor(Color.black)
      val p0 = scaled2frame(0, 0)
      val p1 = scaled2frame(1, 1)
      g2d.drawRect(math.min(p0.x, p1.x), math.min(p0.y, p1.y), math.abs(p0.x - p1.x), math.abs(p0.y - p1.y))
      // g2d.drawRect(p0._1, p0._2, p1._1, p1._2)
      //println("p0 = " + p0)
      //println("p1 = " + p1)
      //g2d.drawRect(p0._1, p0._2, DRAWABLEWIDTH, -1 * DRAWABLEHEIGHT)
    }

    def centroids(g2d: Graphics2D): Unit = {
      for (i <- 0 until classifier.K()) {
        g2d.setColor(Color.darkGray)
        val (x, y) = (classifier.μ(i, 0), classifier.μ(i, 1))
        val fp = scaled2frame(x, y)
        g2d.fillOval(fp.x, fp.y, 3 * DIAMETER, 3 * DIAMETER)
      }
    }

    def cluster(g2d: Graphics2D, i: Int): Unit = {
      g2d.setColor(colors(i % colors.length))
      for (r <- 0 until classifier.scaledX.rows) {
        if (classifier.C(r, 0) == i) {
          // TODO figure out what to do when N > 2
          val (x, y) = (classifier.scaledX(r, 0), classifier.scaledX(r, 1))
          val fp = scaled2frame(x, y)
          g2d.fillOval(fp.x, fp.y, DIAMETER, DIAMETER)
          // g2d.drawString(r.toString + "(%.2f,%.2f)".format(x, y), xp, yp)
        }
      }
    }

    override def paintComponent(g: Graphics): Unit = {
      println("KMeansPanel.paintComponent")
      // super.paintComponent(g)
      val size = getSize()
      // val insets = getInsets()
      // val w = size.width - (insets.left + insets.right)
      // val h = size.height - (insets.top + insets.bottom)
      val g2d = g.asInstanceOf[Graphics2D]
      boundingRectangle(g2d)
      centroids(g2d)
      for (i <- 0 until classifier.K()) {
        // TODO: inefficient loop
        cluster(g2d, i)
      }
    }
  }
}
