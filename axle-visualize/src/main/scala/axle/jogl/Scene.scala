package axle.jogl

import java.net.URL

import scala.Vector

import com.jogamp.opengl.util.texture.Texture
import com.jogamp.opengl.util.texture.TextureIO

import axle.algebra.Position3DSpace
import axle.algebra.SphericalVector
import axle.quanta.Angle
import axle.quanta.AngleConverter
import axle.quanta.Distance
import axle.quanta.DistanceConverter
import axle.quanta.UnitOfMeasurement
import axle.quanta.UnittedQuantity
import axle.quanta.modulize
import com.jogamp.opengl.GL2
import com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0
import com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_POSITION
import com.jogamp.opengl.glu.GLU
import spire.implicits.DoubleAlgebra
import spire.implicits.FloatAlgebra
import spire.implicits.moduleOps

abstract class Scene(val distanceUnit: UnitOfMeasurement[Distance])(
  implicit angleMetaFloat: AngleConverter[Float],
  distanceMetaFloat: DistanceConverter[Float],
  distanceMetaDouble: DistanceConverter[Double]) {

  def render[A: Render](value: A, orienter: GL2 => Unit, gl: GL2, glu: GLU): Unit = {
    gl.glLoadIdentity()
    orienter(gl)
    Render[A].render(value, this, gl, glu)
  }

  var url2texture = Map.empty[URL, Texture]

  def registerTexture(url: URL, texture: Texture): Unit = url2texture += url -> texture

  def textureFor(url: URL): Texture = url2texture(url)

  def registerTextures(): Unit =
    textureUrls foreach {
      case (url, extension) =>
        url2texture += url -> TextureIO.newTexture(url, false, extension)
    }

  def positionLight[P](p: P, gl: GL2)(implicit position: Position3DSpace[Float, P]): Unit = {

    gl.glLightfv(GL_LIGHT0, GL_POSITION, Vector(
      (position.x(p) in distanceUnit).magnitude,
      (position.y(p) in distanceUnit).magnitude,
      (position.z(p) in distanceUnit).magnitude).toArray, 0)
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
