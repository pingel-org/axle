package axle.awt

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage

import axle.visualize.Color
import axle.visualize.PixelatedColoredArea
import javax.swing.JPanel

case class PixelatedColoredAreaComponent[X, Y, V](
  pca: PixelatedColoredArea[X, Y, V])
    extends JPanel {

  import pca._

  setMinimumSize(new Dimension(width, height))

  val image: BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

  (0 until width) foreach { column =>
    val x = scaledArea.unframeX(column)
    (0 until height) foreach { row =>
      val y = scaledArea.unframeY(row)
      val rgb = Color.toRGB(c(f(x, y)))
      image.setRGB(column, row, rgb)
    }
  }

  override def paintComponent(g: Graphics): Unit = {

    val g2d = g.asInstanceOf[Graphics2D]
    g2d.drawImage(image, 0, 0, null)
  }

}