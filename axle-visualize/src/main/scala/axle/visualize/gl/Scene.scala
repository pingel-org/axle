package axle.visualize.gl

import java.net.URL

import axle.quanta._
import scala.math._

import com.jogamp.opengl.util.texture.Texture
import com.jogamp.opengl.util.texture.TextureIO

import javax.media.opengl.GL2
import javax.media.opengl.glu.GLU
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION

abstract class Scene(_distanceUnit: Distance.Q) {

  def distanceUnit: Distance.Q = _distanceUnit

  import Distance._
  import Angle._

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

  def sphericalToCartesian(spherical: SphericalVector): Position = {
    import spherical._
    Position(
      ρ * sin((θ in radian).magnitude.toDouble) * cos((φ in radian).magnitude.toDouble),
      ρ * sin((θ in radian).magnitude.toDouble) * sin((φ in radian).magnitude.toDouble),
      ρ * cos((θ in radian).magnitude.toDouble))
  }

  def positionLight(position: Position, gl: GL2): Unit = {
    import position._
    gl.glLightfv(GL_LIGHT0, GL_POSITION, Vector(
      (x in distanceUnit).magnitude.toFloat,
      (y in distanceUnit).magnitude.toFloat,
      (z in distanceUnit).magnitude.toFloat).toArray, 0)
  }

  def translate(gl: GL2, x: Distance.Q, y: Distance.Q, z: Distance.Q): Unit =
    gl.glTranslatef(
      (x in distanceUnit).magnitude.toFloat,
      (y in distanceUnit).magnitude.toFloat,
      (z in distanceUnit).magnitude.toFloat)

  def rotate(gl: GL2, a: Angle.Q, x: Float, y: Float, z: Float): Unit =
    gl.glRotatef((a in degree).magnitude.toFloat, x, y, z)

  def textureUrls: Seq[(URL, String)]

  def title: String

  def renderAll(gl: GL2, glu: GLU): Unit

  def tic(): Unit
}
