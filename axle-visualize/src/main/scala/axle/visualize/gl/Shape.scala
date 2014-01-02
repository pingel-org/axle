package axle.visualize.gl

import java.net.URL

import axle.quanta._

sealed trait Shape

case class Triangle(length: Distance.Q, color: Color) extends Shape

case class TriColorTriangle(length: Distance.Q, c1: Color, c2: Color, c3: Color) extends Shape

case class Pyramid(length: Distance.Q, color: Color) extends Shape

case class MultiColorPyramid(length: Distance.Q, c1: Color, c2: Color, c3: Color) extends Shape

case class Quad(width: Distance.Q, height: Distance.Q, color: Color) extends Shape

case class Cube(length: Distance.Q, color: Color) extends Shape

case class MultiColorCube(length: Distance.Q,
  topColor: Color, bottomColor: Color, frontColor: Color, backColor: Color, leftColor: Color, rightColor: Color) extends Shape

case class TexturedCube(length: Distance.Q, reflectionColor: Color, textureUrl: URL, textureExtension: String) extends Shape

case class Sphere(radius: Distance.Q, slices: Int, stacks: Int, color: Color) extends Shape

case class TexturedSphere(radius: Distance.Q, slices: Int, stacks: Int, reflectionColor: Color, textureUrl: URL, textureExtension: String) extends Shape
