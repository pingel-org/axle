package axle.visualize.gl

import java.net.URL

import axle.quanta.Distance
import axle.quanta.UnittedQuantity
import spire.algebra.Field
import spire.algebra.Order

sealed trait Shape

case class Triangle[N: Field: Order](length: UnittedQuantity[Distance, N], color: Color) extends Shape

case class TriColorTriangle[N: Field: Order](length: UnittedQuantity[Distance, N], c1: Color, c2: Color, c3: Color) extends Shape

case class Pyramid[N: Field: Order](length: UnittedQuantity[Distance, N], color: Color) extends Shape

case class MultiColorPyramid[N: Field: Order](length: UnittedQuantity[Distance, N], c1: Color, c2: Color, c3: Color) extends Shape

case class Quad[N: Field: Order](width: UnittedQuantity[Distance, N], height: UnittedQuantity[Distance, N], color: Color) extends Shape

case class Cube[N: Field: Order](length: UnittedQuantity[Distance, N], color: Color) extends Shape

case class MultiColorCube[N: Field: Order](length: UnittedQuantity[Distance, N],
  topColor: Color, bottomColor: Color, frontColor: Color, backColor: Color, leftColor: Color, rightColor: Color) extends Shape

case class TexturedCube[N: Field: Order](length: UnittedQuantity[Distance, N], reflectionColor: Color, textureUrl: URL, textureExtension: String) extends Shape

case class Sphere[N: Field: Order](radius: UnittedQuantity[Distance, N], slices: Int, stacks: Int, color: Color) extends Shape

case class TexturedSphere[N: Field: Order](radius: UnittedQuantity[Distance, N], slices: Int, stacks: Int, reflectionColor: Color, textureUrl: URL, textureExtension: String) extends Shape
