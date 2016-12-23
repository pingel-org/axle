package axle.visualize

import axle.algebra.LengthSpace
import cats.kernel.Eq
import cats.implicits._

// http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html

case class ScaledArea2D[X, Y](
    width: Double,
    height: Double,
    pad: Double,
    minX: X,
    maxX: X,
    minY: Y,
    maxY: Y)(
        implicit eqX: Eq[X],
        eqY: Eq[Y],
        lengthX: LengthSpace[X, _, Double],
        lengthY: LengthSpace[Y, _, Double]) {

  val nonZeroArea = (!(minX === maxX)) && (!(minY === maxY))

  val drawableWidth = width - (2 * pad)
  val drawableHeight = height - (2 * pad)

  def frameX(x: X): Double =
    pad + (drawableWidth * lengthX.portion(minX, x, maxX))

  def unframeX(px: Double): X =
    lengthX.onPath(minX, maxX, (px - pad) / drawableWidth)

  def frameY(y: Y): Double =
    height - pad - (drawableHeight * lengthY.portion(minY, y, maxY))

  def unframeY(py: Double): Y =
    lengthY.onPath(minY, maxY, (py - pad) / drawableHeight)

  def framePoint(sp: Point2D[X, Y]): Point2D[Double, Double] = Point2D(
    frameX(sp.x),
    frameY(sp.y))

  def unframePoint(p: Point2D[Double, Double]): Point2D[X, Y] = Point2D(
    unframeX(p.x),
    unframeY(p.y))

}
