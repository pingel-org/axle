package axle.visualize.gl

import axle.quanta._
import java.io.IOException
import com.jogamp.opengl.util.texture.TextureIO
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
import javax.media.opengl.GLException
import javax.media.opengl.awt.GLCanvas
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH
import javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW
import javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION
import javax.media.opengl.glu.GLU
import java.net.URL
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_COLOR_MATERIAL
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR
import com.jogamp.opengl.util.texture.Texture

class AxleGLCanvas(scene: Scene, fovy: Angle.Q, zNear: Distance.Q, zFar: Distance.Q, distanceUnit: Distance.Q) extends GLCanvas with GLEventListener {

  import Distance._
  import Angle._
  
  this.addGLEventListener(this)
  
  var glu: GLU = null
  
  override def init(drawable: GLAutoDrawable): Unit = {
    val gl = drawable.getGL.getGL2
    glu = new GLU()
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    gl.glClearDepth(1.0f)
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

    assert(height > 0)
    val aspect = width.toFloat / height
    gl.glViewport(0, 0, width, height)
    gl.glMatrixMode(GL_PROJECTION)
    gl.glLoadIdentity()
    glu.gluPerspective((fovy in degree).magnitude.toDouble, aspect, (zNear in distanceUnit).magnitude.toDouble, (zFar in distanceUnit).magnitude.toDouble)
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
