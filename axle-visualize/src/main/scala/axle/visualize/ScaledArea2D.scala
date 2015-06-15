package axle.visualize

import axle.algebra.LengthSpace
import spire.algebra.Eq
import spire.implicits.eqOps

// http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html

case class ScaledArea2D[X, Y](
    width: Int,
    height: Int,
    pad: Int,
    minX: X,
    maxX: X,
    minY: Y,
    maxY: Y)(
        implicit eqX: Eq[X],
        eqY: Eq[Y],
        lengthX: LengthSpace[X, _],
        lengthY: LengthSpace[Y, _]) {

  val nonZeroArea = (!(minX === maxX)) && (!(minY === maxY))

  val drawableWidth = width - (2 * pad)
  val drawableHeight = height - (2 * pad)

  // TODO Double, Double should be parameterized.
  // Could use LengthSpace instead

  def framePoint(sp: Point2D[X, Y]): Point2D[Int, Int] = Point2D(
    pad + (drawableWidth * lengthX.portion(minX, sp.x, maxX)).toInt,
    height - pad - (drawableHeight * lengthY.portion(minY, sp.y, maxY)).toInt)

}
