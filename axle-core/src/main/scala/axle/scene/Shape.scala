package axle.scene

import java.net.URL

import axle.quanta.Distance
import axle.quanta.UnittedQuantity

case class Triangle[N](length: UnittedQuantity[Distance, N], color: Color)

case class TriColorTriangle[N](length: UnittedQuantity[Distance, N], c1: Color, c2: Color, c3: Color)

case class Pyramid[N](length: UnittedQuantity[Distance, N], color: Color)

case class MultiColorPyramid[N](length: UnittedQuantity[Distance, N], c1: Color, c2: Color, c3: Color)

case class Quad[N](width: UnittedQuantity[Distance, N], height: UnittedQuantity[Distance, N], color: Color)

case class Cube[N](length: UnittedQuantity[Distance, N], color: Color)

case class MultiColorCube[N](
  length:   UnittedQuantity[Distance, N],
  topColor: Color, bottomColor: Color, frontColor: Color, backColor: Color, leftColor: Color, rightColor: Color)

case class TexturedCube[N](length: UnittedQuantity[Distance, N], reflectionColor: Color, textureUrl: URL, textureExtension: String)

case class Sphere[N](radius: UnittedQuantity[Distance, N], slices: Int, stacks: Int, color: Color)

case class TexturedSphere[N](radius: UnittedQuantity[Distance, N], slices: Int, stacks: Int, reflectionColor: Color, textureUrl: URL, textureExtension: String)
