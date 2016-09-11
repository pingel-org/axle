package axle

import scala.Vector

import com.jogamp.opengl.GL2
import com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0
import com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_POSITION

import axle.algebra.Position3DSpace
import axle.quanta.Angle
import axle.quanta.AngleConverter
import axle.quanta.Distance
import axle.quanta.DistanceConverter
import axle.quanta.UnitOfMeasurement
import axle.quanta.UnittedQuantity
import spire.implicits.FloatAlgebra

package object jogl {

  def translate(
    gl: GL2,
    distanceUnit: UnitOfMeasurement[Distance],
    x: UnittedQuantity[Distance, Float],
    y: UnittedQuantity[Distance, Float],
    z: UnittedQuantity[Distance, Float])(
      implicit distanceMetaFloat: DistanceConverter[Float]): Unit =
    gl.glTranslatef(
      (x in distanceUnit).magnitude,
      (y in distanceUnit).magnitude,
      (z in distanceUnit).magnitude)

  def rotate(
    gl: GL2,
    a: UnittedQuantity[Angle, Float],
    x: Float,
    y: Float,
    z: Float)(
      implicit angleMetaFloat: AngleConverter[Float]): Unit =
    gl.glRotatef((a in angleMetaFloat.degree).magnitude, x, y, z)

  def positionLight[P](
    p: P,
    distanceUnit: UnitOfMeasurement[Distance],
    gl: GL2)(
      implicit position: Position3DSpace[Float, P],
      distanceMetaFloat: DistanceConverter[Float]): Unit = {

    gl.glLightfv(GL_LIGHT0, GL_POSITION, Vector(
      (position.x(p) in distanceUnit).magnitude,
      (position.y(p) in distanceUnit).magnitude,
      (position.z(p) in distanceUnit).magnitude).toArray, 0)
  }

  def render[A: Render](shape: A, orienter: GL2 => Unit, gl: GL2, rc: RenderContext): Unit = {
    gl.glLoadIdentity()
    orienter(gl)
    Render[A].render(shape, rc, gl)
  }

}