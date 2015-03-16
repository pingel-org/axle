package axle.visualize.gl

import java.net.URL

import scala.Vector
import scala.math.cos
import scala.math.sin

import com.jogamp.opengl.util.texture.Texture
import com.jogamp.opengl.util.texture.TextureIO

import axle.algebra.Position
import axle.algebra.SphericalVector
import axle.quanta.Angle
import axle.quanta.AngleConverter
import axle.quanta.modulize
import axle.quanta.Distance
import axle.quanta.DistanceConverter
import axle.quanta.UnittedQuantity
import axle.quanta.UnitOfMeasurement
import axle.quanta.Distance._
import javax.media.opengl.GL2
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION
import javax.media.opengl.glu.GLU
import spire.implicits.DoubleAlgebra
import spire.implicits.FloatAlgebra
import spire.implicits.moduleOps
import spire.implicits._
import axle.algebra.DirectedGraph
import axle.sine
import axle.cosine

abstract class Scene(val distanceUnit: UnitOfMeasurement[Distance])(
  implicit angleMetaFloat: AngleConverter[Float],
  angleMetaDouble: AngleConverter[Double],
  distanceMetaFloat: DistanceConverter[Float],
  distanceMetaDouble: DistanceConverter[Double]) {

  def render[A: Render](value: A, orienter: GL2 => Unit, gl: GL2, glu: GLU): Unit = {
    gl.glLoadIdentity()
    orienter(gl)
    implicitly[Render[A]].render(value, this, gl, glu)
  }

  var url2texture = Map.empty[URL, Texture]

  def registerTexture(url: URL, texture: Texture): Unit = url2texture += url -> texture

  def textureFor(url: URL): Texture = url2texture(url)

  def registerTextures(): Unit =
    textureUrls foreach {
      case (url, extension) =>
        url2texture += url -> TextureIO.newTexture(url, false, extension)
    }

  def sphericalToCartesian(
    spherical: SphericalVector[Double]): Position[Double] = {
    import spherical._

    Position(
      ρ :* (sine(θ) * cosine(φ)),
      ρ :* (sine(θ) * sine(φ)),
      ρ :* cosine(θ))
  }

  def positionLight(position: Position[Float], gl: GL2): Unit = {
    import position._
    gl.glLightfv(GL_LIGHT0, GL_POSITION, Vector(
      (x in distanceUnit).magnitude,
      (y in distanceUnit).magnitude,
      (z in distanceUnit).magnitude).toArray, 0)
  }

  def translate(
    gl: GL2,
    x: UnittedQuantity[Distance, Float],
    y: UnittedQuantity[Distance, Float],
    z: UnittedQuantity[Distance, Float]): Unit =
    gl.glTranslatef(
      (x in distanceUnit).magnitude,
      (y in distanceUnit).magnitude,
      (z in distanceUnit).magnitude)

  def rotate(gl: GL2, a: UnittedQuantity[Angle, Float], x: Float, y: Float, z: Float): Unit =
    gl.glRotatef((a in angleMetaFloat.degree).magnitude, x, y, z)

  def textureUrls: Seq[(URL, String)]

  def title: String

  def renderAll(gl: GL2, glu: GLU): Unit

  def tic(): Unit
}
