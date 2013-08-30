package axle.visualize.gl

import javax.media.opengl.GL.GL_TRIANGLES
import javax.media.opengl.GL.GL_FRONT_AND_BACK
import javax.media.opengl.GL.GL_FRONT
import javax.media.opengl.GL2
import javax.media.opengl.GL2GL3.GL_QUADS
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_POSITION
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS
import javax.media.opengl.fixedfunc.GLLightingFunc.GL_EMISSION
import com.jogamp.opengl.util.texture.Texture
import javax.media.opengl.glu.GLU

trait Render[A] {
  def render(value: A, scene: Scene, gl: GL2, glu: GLU): Unit
}

object Render {

  implicit val quadRenderer = new Render[Quad] {
    def render(quad: Quad, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import quad._
      gl.glColor3f(color.red, color.green, color.blue)
      gl.glBegin(GL_QUADS)
      gl.glVertex3f(-width / 2, height / 2, 0.0f)
      gl.glVertex3f(width / 2, height / 2, 0.0f)
      gl.glVertex3f(width / 2, -height / 2, 0.0f)
      gl.glVertex3f(-width / 2, -height / 2, 0.0f)
      gl.glEnd()
    }
  }

  implicit val coloredSphereRenderer = new Render[Sphere] {
    def render(sphere: Sphere, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import sphere._
      gl.glColor3f(color.red, color.green, color.blue)
      glu.gluSphere(glu.gluNewQuadric(), radius, 24, 20)
    }
  }

  //  val colorBlack = java.nio.FloatBuffer.wrap(Vector(0f, 0f, 0f, 1f).toArray)
  //  val colorBlue = java.nio.FloatBuffer.wrap(Vector(0f, 0f, 1f, 1f).toArray)
  //  val colorRed = java.nio.FloatBuffer.wrap(Vector(1f, 0f, 0f, 1f).toArray)
  val rgba = Vector(1f, 1f, 1f).toArray

  implicit val sphereRenderer = new Render[TexturedSphere] {
    def render(sphere: TexturedSphere, scene: Scene, gl: GL2, glu: GLU): Unit = {

      import sphere._
      gl.glColor3f(1f, 1f, 1f)
      gl.glMaterialfv(GL_FRONT, GL_AMBIENT, rgba, 0)
      gl.glMaterialfv(GL_FRONT, GL_SPECULAR, rgba, 0)
      gl.glMaterialf(GL_FRONT, GL_SHININESS, 0.5f)

      val texture = scene.textureFor(textureUrl)

      texture.enable(gl)
      texture.bind(gl)

      val earth = glu.gluNewQuadric()
      glu.gluQuadricTexture(earth, true)
      glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL)
      glu.gluQuadricNormals(earth, GLU.GLU_FLAT)
      glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE)
      val slices = 96
      val stacks = 64
      glu.gluSphere(earth, radius, slices, stacks)
      glu.gluDeleteQuadric(earth)
    }
  }

  implicit val triangleRenderer = new Render[Triangle] {
    def render(triangle: Triangle, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import triangle._
      gl.glBegin(GL_TRIANGLES)

      //      gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, colorRed)
      //      gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, colorRed)
      //      gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, colorRed)
      //      gl.glMateriali(GL_FRONT_AND_BACK, GL_SHININESS, 4)
      //      gl.glMaterialfv(GL_FRONT_AND_BACK, GL_EMISSION, colorBlack)

      gl.glColor3f(1.0f, 0.0f, 0.0f)
      gl.glVertex3f(0.0f, length, 0.0f)
      gl.glColor3f(0.0f, 1.0f, 0.0f)
      gl.glVertex3f(-length, -length, 0.0f)
      gl.glColor3f(0.0f, 0.0f, 1.0f)
      gl.glVertex3f(length, -length, 0.0f)
      gl.glEnd()
    }
  }

  implicit val cubeRenderer = new Render[Cube] {
    def render(cube: Cube, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import cube._
      gl.glBegin(GL_QUADS)

      // Top-face
      gl.glColor3f(0.0f, 1.0f, 0.0f) // green
      gl.glVertex3f(length, length, -length)
      gl.glVertex3f(-length, length, -length)
      gl.glVertex3f(-length, length, length)
      gl.glVertex3f(length, length, length)

      // Bottom-face
      gl.glColor3f(1.0f, 0.5f, 0.0f) // orange
      gl.glVertex3f(length, -length, length)
      gl.glVertex3f(-length, -length, length)
      gl.glVertex3f(-length, -length, -length)
      gl.glVertex3f(length, -length, -length)

      // Front-face
      gl.glColor3f(1.0f, 0.0f, 0.0f) // red
      gl.glVertex3f(length, length, length)
      gl.glVertex3f(-length, length, length)
      gl.glVertex3f(-length, -length, length)
      gl.glVertex3f(length, -length, length)

      // Back-face
      gl.glColor3f(1.0f, 1.0f, 0.0f) // yellow
      gl.glVertex3f(length, -length, -length)
      gl.glVertex3f(-length, -length, -length)
      gl.glVertex3f(-length, length, -length)
      gl.glVertex3f(length, length, -length)

      // Left-face
      gl.glColor3f(0.0f, 0.0f, 1.0f) // blue
      gl.glVertex3f(-length, length, length)
      gl.glVertex3f(-length, length, -length)
      gl.glVertex3f(-length, -length, -length)
      gl.glVertex3f(-length, -length, length)

      // Right-face
      gl.glColor3f(1.0f, 0.0f, 1.0f) // violet
      gl.glVertex3f(length, length, -length)
      gl.glVertex3f(length, length, length)
      gl.glVertex3f(length, -length, length)
      gl.glVertex3f(length, -length, -length)

      gl.glEnd()
    }

  }

  implicit val pyramidRenderer = new Render[Pyramid] {
    def render(pyramid: Pyramid, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import pyramid._
      gl.glBegin(GL_TRIANGLES)

      // Font-face triangle
      gl.glColor3f(1f, 0f, 0f)
      gl.glVertex3f(0f, length, 0f)
      gl.glColor3f(0f, 1f, 0f)
      gl.glVertex3f(-length, -length, length)
      gl.glColor3f(0f, 0f, 1f)
      gl.glVertex3f(length, -length, length)

      // Right-face triangle                                                                                                                                                        
      gl.glColor3f(1f, 0f, 0f)
      gl.glVertex3f(0f, length, 0f)
      gl.glColor3f(0f, 0f, 1f)
      gl.glVertex3f(length, -length, length)
      gl.glColor3f(0f, 1f, 0f)
      gl.glVertex3f(length, -length, -length)

      // Back-face triangle
      gl.glColor3f(1f, 0f, 0f)
      gl.glVertex3f(0f, length, 0f)
      gl.glColor3f(0f, 1f, 0f)
      gl.glVertex3f(length, -length, -length)
      gl.glColor3f(0f, 0f, 1f)
      gl.glVertex3f(-length, -length, -length)

      // Left-face triangle
      gl.glColor3f(1f, 0f, 0f)
      gl.glVertex3f(0f, length, 0f)
      gl.glColor3f(0f, 0f, 1f)
      gl.glVertex3f(-length, -length, -length)
      gl.glColor3f(0f, 1f, 0f)
      gl.glVertex3f(-length, -length, length)

      gl.glEnd()
    }
  }

  implicit val texturedCubeRenderer = new Render[TexturedCube] {

    def render(cube: TexturedCube, scene: Scene, gl: GL2, glu: GLU): Unit = {

      import cube._

      val texture = scene.textureFor(textureUrl)

      val textureCoords = texture.getImageTexCoords()
      val textureTop = textureCoords.top()
      val textureBottom = textureCoords.bottom()
      val textureLeft = textureCoords.left()
      val textureRight = textureCoords.right()

      gl.glColor3f(1f, 1f, 1f)
      
      texture.enable(gl)
      // gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE)
      texture.bind(gl)

      gl.glBegin(GL_QUADS)

      // Front Face
      gl.glTexCoord2f(textureLeft, textureBottom)
      gl.glVertex3f(-length, -length, length)
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(length, -length, length)
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(length, length, length)
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(-length, length, length)

      // Back Face
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(-length, -length, -length)
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(-length, length, -length)
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(length, length, -length)
      gl.glTexCoord2f(textureLeft, textureBottom)
      gl.glVertex3f(length, -length, -length)

      // Top Face
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(-length, length, -length)
      gl.glTexCoord2f(textureLeft, textureBottom)
      gl.glVertex3f(-length, length, length)
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(length, length, length)
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(length, length, -length)

      // Bottom Face
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(-length, -length, -length)
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(length, -length, -length)
      gl.glTexCoord2f(textureLeft, textureBottom)
      gl.glVertex3f(length, -length, length)
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(-length, -length, length)

      // Right face
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(length, -length, -length)
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(length, length, -length)
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(length, length, length)
      gl.glTexCoord2f(textureLeft, textureBottom)
      gl.glVertex3f(length, -length, length)

      // Left Face
      gl.glTexCoord2f(textureLeft, textureBottom)
      gl.glVertex3f(-length, -length, -length)
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(-length, -length, length)
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(-length, length, length)
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(-length, length, -length)

      gl.glEnd()

      texture.disable(gl)
    }

  }
}
