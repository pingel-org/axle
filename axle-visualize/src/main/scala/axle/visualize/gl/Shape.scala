package axle.visualize.gl

import java.net.URL

sealed trait Shape

case class Triangle(length: Float) extends Shape

case class Pyramid(length: Float) extends Shape

case class Quad(width: Float, height: Float, color: Color) extends Shape

case class Cube(length: Float) extends Shape

case class TexturedCube(length: Float, textureUrl: URL, textureExtension: String) extends Shape

case class Sphere(radius: Float, color: Color) extends Shape

case class TexturedSphere(radius: Float, textureUrl: URL, textureExtension: String) extends Shape