package axle.visualize

import axle.algebra.LengthSpace
import cats.kernel.Eq

case class PixelatedColoredArea[X: Eq, Y: Eq, V](
    f: (X, Y) => V,
    c: V => Color,
    width: Int = 600,
    height: Int = 600,
    minX: X,
    maxX: X,
    minY: Y,
    maxY: Y)(
        implicit lengthX: LengthSpace[X, X, Double],
        lengthY: LengthSpace[Y, Y, Double]) {

  val border = 0d

  val scaledArea = ScaledArea2D(
    width = width,
    height, border,
    minX, maxX,
    minY, maxY)

}
