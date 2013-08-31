package axle.visualize.gl

import java.net.URL

sealed trait Shape

case class Triangle(length: Float, color: Color) extends Shape

case class TriColorTriangle(length: Float, c1: Color, c2: Color, c3: Color) extends Shape

case class Pyramid(length: Float, color: Color) extends Shape

case class MultiColorPyramid(length: Float, c1: Color, c2: Color, c3: Color) extends Shape

case class Quad(width: Float, height: Float, color: Color) extends Shape

case class Cube(length: Float, color: Color) extends Shape

case class MultiColorCube(length: Float, topColor: Color, bottomColor: Color, frontColor: Color, backColor: Color, leftColor: Color, rightColor: Color) extends Shape

case class TexturedCube(length: Float, reflectionColor: Color, textureUrl: URL, textureExtension: String) extends Shape

case class Sphere(radius: Float, slices: Int, stacks: Int, color: Color) extends Shape

case class TexturedSphere(radius: Float, slices: Int, stacks: Int, reflectionColor: Color, textureUrl: URL, textureExtension: String) extends Shape