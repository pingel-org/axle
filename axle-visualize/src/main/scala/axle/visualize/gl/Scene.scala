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
import axle.quanta.AngleDouble
import axle.quanta.AngleFloat
import axle.quanta.Distance
import axle.quanta.UnittedQuantity4
import axle.quanta.UnitOfMeasurement4
import axle.quanta.Distance._
//import axle.jung.JungDirectedGraph.directedGraphJung // conversion graph
import javax.media.opengl.GL2
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION
import javax.media.opengl.glu.GLU
import spire.implicits.DoubleAlgebra
import spire.implicits.FloatAlgebra
import spire.implicits.moduleOps
import axle.algebra.DirectedGraph

abstract class Scene[DG[_, _]: DirectedGraph](val distanceUnit: UnitOfMeasurement4[Distance[Float], Float])(
  implicit angleCg: DG[UnitOfMeasurement4[axle.quanta.Angle[Double], Double], Double => Double],
  distanceCg: DG[UnitOfMeasurement4[axle.quanta.Distance[Float], Float], Float => Float]) {

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

  def sphericalToCartesian(spherical: SphericalVector[Double]): Position[Double] = {
    import spherical._
    import AngleDouble.radian
    Position(
      ρ :* sin((θ in radian).magnitude) * cos((φ in radian).magnitude),
      ρ :* sin((θ in radian).magnitude) * sin((φ in radian).magnitude),
      ρ :* cos((θ in radian).magnitude))
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
    x: UnittedQuantity4[Distance[Float], Float],
    y: UnittedQuantity4[Distance[Float], Float],
    z: UnittedQuantity4[Distance[Float], Float]): Unit =
    gl.glTranslatef(
      (x in distanceUnit).magnitude,
      (y in distanceUnit).magnitude,
      (z in distanceUnit).magnitude)

  def rotate(gl: GL2, a: UnittedQuantity4[Angle[Float], Float], x: Float, y: Float, z: Float): Unit =
    gl.glRotatef((a in AngleFloat.degree).magnitude, x, y, z)

  def textureUrls: Seq[(URL, String)]

  def title: String

  def renderAll(gl: GL2, glu: GLU): Unit

  def tic(): Unit
}
