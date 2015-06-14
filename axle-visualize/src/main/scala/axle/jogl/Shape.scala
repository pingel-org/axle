package axle.jogl

import java.net.URL

import axle.quanta.Distance
import axle.quanta.UnittedQuantity

sealed trait Shape

case class Triangle[N](length: UnittedQuantity[Distance, N], color: Color) extends Shape

case class TriColorTriangle[N](length: UnittedQuantity[Distance, N], c1: Color, c2: Color, c3: Color) extends Shape

case class Pyramid[N](length: UnittedQuantity[Distance, N], color: Color) extends Shape

case class MultiColorPyramid[N](length: UnittedQuantity[Distance, N], c1: Color, c2: Color, c3: Color) extends Shape

case class Quad[N](width: UnittedQuantity[Distance, N], height: UnittedQuantity[Distance, N], color: Color) extends Shape

case class Cube[N](length: UnittedQuantity[Distance, N], color: Color) extends Shape

case class MultiColorCube[N](length: UnittedQuantity[Distance, N],
                             topColor: Color, bottomColor: Color, frontColor: Color, backColor: Color, leftColor: Color, rightColor: Color) extends Shape

case class TexturedCube[N](length: UnittedQuantity[Distance, N], reflectionColor: Color, textureUrl: URL, textureExtension: String) extends Shape

case class Sphere[N](radius: UnittedQuantity[Distance, N], slices: Int, stacks: Int, color: Color) extends Shape

case class TexturedSphere[N](radius: UnittedQuantity[Distance, N], slices: Int, stacks: Int, reflectionColor: Color, textureUrl: URL, textureExtension: String) extends Shape
