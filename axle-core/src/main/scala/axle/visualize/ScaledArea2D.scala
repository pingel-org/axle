package axle.visualize

import axle.algebra.LengthSpace
import cats.kernel.Eq
import cats.implicits._

case class ScaledArea2D[X, Y](
  left:   Double,
  right:  Double,
  top:    Double, // Note top/bottom is counter-intuitively in reverse order from minY/maxY
  bottom: Double,
  minX:   X,
  maxX:   X,
  minY:   Y,
  maxY:   Y)(
  implicit
  eqX:     Eq[X],
  eqY:     Eq[Y],
  lengthX: LengthSpace[X, _, Double],
  lengthY: LengthSpace[Y, _, Double]) {

  val nonZeroArea = (!(minX === maxX)) && (!(minY === maxY))

  val width = right - left
  val height = top - bottom

  def frameX(x: X): Double =
    left + width * lengthX.portion(minX, x, maxX)

  def unframeX(px: Double): X =
    lengthX.onPath(minX, maxX, (px - left) / width)

  def frameY(y: Y): Double =
    bottom + height * lengthY.portion(minY, y, maxY)

  def unframeY(py: Double): Y =
    lengthY.onPath(minY, maxY, (py - bottom) / height)

  def framePoint(sp: Point2D[X, Y]): Point2D[Double, Double] =
    Point2D(frameX(sp.x), frameY(sp.y))

  def unframePoint(p: Point2D[Double, Double]): Point2D[X, Y] =
    Point2D(unframeX(p.x), unframeY(p.y))

}
