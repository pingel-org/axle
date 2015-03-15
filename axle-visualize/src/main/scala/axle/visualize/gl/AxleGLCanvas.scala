package axle.visualize.gl

import scala.Vector

import axle.quanta.Angle
import axle.quanta.AngleMetadata
import axle.quanta.Distance
import axle.quanta.DistanceMetadata
import axle.quanta.UnitOfMeasurement
import axle.quanta.UnittedQuantity
import spire.implicits.DoubleAlgebra
import javax.media.opengl.GL.GL_COLOR_BUFFER_BIT
import javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT
import javax.media.opengl.GL.GL_DEPTH_TEST
import javax.media.opengl.GL.GL_LEQUAL
import javax.media.opengl.GL.GL_LINEAR
import javax.media.opengl.GL.GL_NICEST
import javax.media.opengl.GL.GL_TEXTURE_2D
import javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER
import javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER
import javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT
import javax.media.opengl.GLAutoDrawable
import javax.media.opengl.GLEventListener
import javax.media.opengl.awt.GLCanvas
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_COLOR_MATERIAL
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR
import javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW
import javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION
import javax.media.opengl.glu.GLU
import spire.implicits.FloatAlgebra
import axle.algebra.DirectedGraph

case class AxleGLCanvas[DG[_, _]: DirectedGraph](
  scene: Scene[DG],
  fovy: UnittedQuantity[Angle, Float],
  zNear: UnittedQuantity[Distance, Float],
  zFar: UnittedQuantity[Distance, Float],
  distanceUnit: UnitOfMeasurement[Distance, Float])(
    implicit angleMeta: AngleMetadata[Float, DG],
    distanceMeta: DistanceMetadata[Float, DG])
  extends GLCanvas with GLEventListener {

  this.addGLEventListener(this)

  var glu: GLU = null

  override def init(drawable: GLAutoDrawable): Unit = {
    val gl = drawable.getGL.getGL2
    glu = new GLU()
    gl.glClearColor(0f, 0f, 0f, 0f)
    gl.glClearDepth(1f)
    gl.glEnable(GL_DEPTH_TEST)
    gl.glDepthFunc(GL_LEQUAL)
    gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST)
    gl.glShadeModel(GL_SMOOTH)

    scene.registerTextures()

    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)

    val lightAmbientValue = Vector(0.2f, 0.2f, 0.2f, 1f).toArray
    val lightSpecular = Vector(0.2f, 0.2f, 0.2f, 1f).toArray
    val lightDiffuseValue = Vector(1f, 1f, 1f, 1f).toArray
    val lightDiffusePosition = Vector(5f, 1f, 10f, 1f).toArray

    gl.glEnable(GL_LIGHTING)
    gl.glEnable(GL_LIGHT0)
    gl.glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbientValue, 0)
    gl.glLightfv(GL_LIGHT0, GL_SPECULAR, lightSpecular, 0)
    gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuseValue, 0)
    gl.glLightfv(GL_LIGHT0, GL_POSITION, lightDiffusePosition, 0)

    gl.glEnable(GL_COLOR_MATERIAL)
  }

  override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int): Unit = {
    val gl = drawable.getGL.getGL2

    // import axle.jung.JungDirectedGraph.directedGraphJung // conversion graph

    assert(height > 0)
    val aspect = width.toFloat / height
    gl.glViewport(0, 0, width, height)
    gl.glMatrixMode(GL_PROJECTION)
    gl.glLoadIdentity()
    fovy.in(angleMeta.degree)
    glu.gluPerspective(
      (fovy in angleMeta.degree).magnitude,
      aspect,
      (zNear in scene.distanceUnit).magnitude,
      (zFar in scene.distanceUnit).magnitude)
    gl.glMatrixMode(GL_MODELVIEW)
    gl.glLoadIdentity()
  }

  def display(drawable: GLAutoDrawable): Unit = {

    val gl = drawable.getGL.getGL2
    gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    scene.renderAll(gl, glu)
    scene.tic()
  }

  override def dispose(drawable: GLAutoDrawable) {}
}
