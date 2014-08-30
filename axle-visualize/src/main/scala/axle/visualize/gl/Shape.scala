package axle.visualize.gl

import java.net.URL

import axle.quanta2.Distance
import axle.quanta2.Quantity
import spire.algebra.Field
import spire.algebra.Order

sealed trait Shape

case class Triangle[N: Field: Order](length: Quantity[Distance, N], color: Color) extends Shape

case class TriColorTriangle[N: Field: Order](length: Quantity[Distance, N], c1: Color, c2: Color, c3: Color) extends Shape

case class Pyramid[N: Field: Order](length: Quantity[Distance, N], color: Color) extends Shape

case class MultiColorPyramid[N: Field: Order](length: Quantity[Distance, N], c1: Color, c2: Color, c3: Color) extends Shape

case class Quad[N: Field: Order](width: Quantity[Distance, N], height: Quantity[Distance, N], color: Color) extends Shape

case class Cube[N: Field: Order](length: Quantity[Distance, N], color: Color) extends Shape

case class MultiColorCube[N: Field: Order](length: Quantity[Distance, N],
  topColor: Color, bottomColor: Color, frontColor: Color, backColor: Color, leftColor: Color, rightColor: Color) extends Shape

case class TexturedCube[N: Field: Order](length: Quantity[Distance, N], reflectionColor: Color, textureUrl: URL, textureExtension: String) extends Shape

case class Sphere[N: Field: Order](radius: Quantity[Distance, N], slices: Int, stacks: Int, color: Color) extends Shape

case class TexturedSphere[N: Field: Order](radius: Quantity[Distance, N], slices: Int, stacks: Int, reflectionColor: Color, textureUrl: URL, textureExtension: String) extends Shape
