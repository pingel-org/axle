package axle.algebra

import axle.quanta.UnittedQuantity
import axle.quanta.Distance

/**
 *
 * TODO: Perhaps the 1 .. N distinction could be handled by making QX, QY, etc an HList
 * instead of hard-coding the arity in the name.
 * This would help address representing the relationship between, for instance, a Vector1D and a Vector2D
 */

case class Position1D[Q, N](
  x: UnittedQuantity[Q, N])

case class Vector1D[Q, N](
  from: Position1D[Q, N],
  to: Position1D[Q, N])

case class Position2D[QX, QY, N](
  x: UnittedQuantity[QX, N],
  y: UnittedQuantity[QY, N])

case class Vector2D[QX, QY, N](
  from: Position2D[QX, QY, N],
  to: Position2D[QX, QY, N])

case class Position3D[QX, QY, QZ, N](
  x: UnittedQuantity[QX, N],
  y: UnittedQuantity[QY, N],
  z: UnittedQuantity[QZ, N])

case class Vector3D[QX, QY, QZ, N](
  from: Position3D[QX, QY, QZ, N],
  to: Position3D[QX, QY, QZ, N])

case class Position4D[QW, QX, QY, QZ, N](
  w: UnittedQuantity[QW, N],
  x: UnittedQuantity[QX, N],
  y: UnittedQuantity[QY, N],
  z: UnittedQuantity[QZ, N])

case class Vector4D[QW, QX, QY, QZ, N](
  from: Position4D[QW, QX, QY, QZ, N],
  to: Position4D[QW, QX, QY, QZ, N])
