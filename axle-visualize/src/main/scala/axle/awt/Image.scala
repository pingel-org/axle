package axle.awt

import java.awt.image.BufferedImage

import axle.visualize.Color
import axle.visualize.PixelatedColoredArea
import scala.annotation.implicitNotFound

@implicitNotFound("Witness not found for Image[${T}]")
trait Image[T] {

  def image(t: T): BufferedImage

}

object Image {

  final def apply[T](implicit i: Image[T]): Image[T] = i

  /**
   * http://stackoverflow.com/questions/4028898/create-an-image-from-a-non-visible-awt-component
   */

  implicit def draw2image[T: Draw]: Image[T] = new Image[T] {

    def image(t: T): BufferedImage = {
      val component = Draw[T].component(t)

      val minSize = component.getMinimumSize
      val frame = AxleFrame(minSize.width, minSize.height)
      frame.setUndecorated(true)
      frame.initialize()
      val rc = frame.add(component)
      // rc.setVisible(true)
      frame.setVisible(true)

      val image = new BufferedImage(frame.getWidth, frame.getHeight, BufferedImage.TYPE_INT_RGB)
      val g = image.createGraphics()
      frame.paintAll(g)

      g.dispose()
      frame.setVisible(false)
      frame.dispose

      image
    }
  }

  implicit def pcaImage[X, Y, V]: Image[PixelatedColoredArea[X, Y, V]] =
    new Image[PixelatedColoredArea[X, Y, V]] {

      def image(pca: PixelatedColoredArea[X, Y, V]): BufferedImage = {
        import pca._
        val lastRow = height - 1
        val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        (0 until width) foreach { column =>
          val minX = scaledArea.unframeX(column)
          val maxX = scaledArea.unframeX(column + 1)
          (0 until height) foreach { row =>
            val minY = scaledArea.unframeY(row)
            val maxY = scaledArea.unframeY(row + 1)
            val rgb = Color.toRGB(c(f(minX, maxX, minY, maxY)))
            image.setRGB(column, lastRow - row, rgb)
          }
        }

        image
      }
    }

}
