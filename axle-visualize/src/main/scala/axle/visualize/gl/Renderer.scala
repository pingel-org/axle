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
      val w = width.magnitude.toFloat / 2
      val h = height.magnitude.toFloat / 2
      gl.glColor3f(color.red, color.green, color.blue)
      gl.glBegin(GL_QUADS)
      gl.glVertex3f(-w, h, 0f)
      gl.glVertex3f(w, h, 0f)
      gl.glVertex3f(w, -h, 0f)
      gl.glVertex3f(-w, -h, 0f)
      gl.glEnd()
    }
  }

  implicit val coloredSphereRenderer = new Render[Sphere] {
    def render(sphere: Sphere, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import sphere._
      gl.glColor3f(color.red, color.green, color.blue)
      glu.gluSphere(glu.gluNewQuadric(), radius.magnitude.toFloat, slices, stacks)
    }
  }

  //  val colorBlack = java.nio.FloatBuffer.wrap(Vector(0f, 0f, 0f, 1f).toArray)
  //  val colorBlue = java.nio.FloatBuffer.wrap(Vector(0f, 0f, 1f, 1f).toArray)
  //  val colorRed = java.nio.FloatBuffer.wrap(Vector(1f, 0f, 0f, 1f).toArray)
  val rgba = Vector(1f, 1f, 1f).toArray

  implicit val sphereRenderer = new Render[TexturedSphere] {
    def render(sphere: TexturedSphere, scene: Scene, gl: GL2, glu: GLU): Unit = {

      import sphere._
      gl.glColor3f(reflectionColor.red, reflectionColor.green, reflectionColor.blue)
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
      glu.gluSphere(earth, radius.magnitude.toFloat, slices, stacks)
      glu.gluDeleteQuadric(earth)
    }
  }

  implicit val triangleRenderer = new Render[Triangle] {
    def render(triangle: Triangle, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import triangle._
      val l = length.magnitude.toFloat
      gl.glBegin(GL_TRIANGLES)
      gl.glColor3f(color.red, color.green, color.blue)
      gl.glVertex3f(0f, l, 0f)
      gl.glVertex3f(-l, -l, 0f)
      gl.glVertex3f(l, -l, 0f)
      gl.glEnd()
    }
  }

  // gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, colorRed)
  // gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, colorRed)
  // gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, colorRed)
  // gl.glMateriali(GL_FRONT_AND_BACK, GL_SHININESS, 4)
  // gl.glMaterialfv(GL_FRONT_AND_BACK, GL_EMISSION, colorBlack)

  implicit val triColorTriangleRenderer = new Render[TriColorTriangle] {
    def render(triangle: TriColorTriangle, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import triangle._
      val l = length.magnitude.toFloat
      gl.glBegin(GL_TRIANGLES)
      gl.glColor3f(c1.red, c1.green, c1.blue)
      gl.glVertex3f(0f, l, 0f)
      gl.glColor3f(c2.red, c2.green, c2.blue)
      gl.glVertex3f(-l, -l, 0f)
      gl.glColor3f(c3.red, c3.green, c3.blue)
      gl.glVertex3f(l, -l, 0f)
      gl.glEnd()
    }
  }

  implicit val cubeRenderer = new Render[Cube] {
    def render(cube: Cube, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import cube._
      val l = length.magnitude.toFloat
      gl.glBegin(GL_QUADS)

      gl.glColor3f(color.red, color.green, color.blue)

      gl.glVertex3f(l, l, -l)
      gl.glVertex3f(-l, l, -l)
      gl.glVertex3f(-l, l, l)
      gl.glVertex3f(l, l, l)

      gl.glVertex3f(l, -l, l)
      gl.glVertex3f(-l, -l, l)
      gl.glVertex3f(-l, -l, -l)
      gl.glVertex3f(l, -l, -l)

      gl.glVertex3f(l, l, l)
      gl.glVertex3f(-l, l, l)
      gl.glVertex3f(-l, -l, l)
      gl.glVertex3f(l, -l, l)

      gl.glVertex3f(l, -l, -l)
      gl.glVertex3f(-l, -l, -l)
      gl.glVertex3f(-l, l, -l)
      gl.glVertex3f(l, l, -l)

      gl.glVertex3f(-l, l, l)
      gl.glVertex3f(-l, l, -l)
      gl.glVertex3f(-l, -l, -l)
      gl.glVertex3f(-l, -l, l)

      gl.glVertex3f(l, l, -l)
      gl.glVertex3f(l, l, l)
      gl.glVertex3f(l, -l, l)
      gl.glVertex3f(l, -l, -l)

      gl.glEnd()
    }
  }

  implicit val multiColorCubeRenderer = new Render[MultiColorCube] {
    def render(cube: MultiColorCube, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import cube._
      val l = length.magnitude.toFloat
      gl.glBegin(GL_QUADS)

      gl.glColor3f(topColor.red, topColor.green, topColor.blue)
      gl.glVertex3f(l, l, -l)
      gl.glVertex3f(-l, l, -l)
      gl.glVertex3f(-l, l, l)
      gl.glVertex3f(l, l, l)

      gl.glColor3f(bottomColor.red, bottomColor.green, bottomColor.blue)
      gl.glVertex3f(l, -l, l)
      gl.glVertex3f(-l, -l, l)
      gl.glVertex3f(-l, -l, -l)
      gl.glVertex3f(l, -l, -l)

      gl.glColor3f(frontColor.red, frontColor.green, frontColor.blue)
      gl.glVertex3f(l, l, l)
      gl.glVertex3f(-l, l, l)
      gl.glVertex3f(-l, -l, l)
      gl.glVertex3f(l, -l, l)

      gl.glColor3f(backColor.red, backColor.green, backColor.blue)
      gl.glVertex3f(l, -l, -l)
      gl.glVertex3f(-l, -l, -l)
      gl.glVertex3f(-l, l, -l)
      gl.glVertex3f(l, l, -l)

      gl.glColor3f(leftColor.red, leftColor.green, leftColor.blue)
      gl.glVertex3f(-l, l, l)
      gl.glVertex3f(-l, l, -l)
      gl.glVertex3f(-l, -l, -l)
      gl.glVertex3f(-l, -l, l)

      gl.glColor3f(rightColor.red, rightColor.green, rightColor.blue)
      gl.glVertex3f(l, l, -l)
      gl.glVertex3f(l, l, l)
      gl.glVertex3f(l, -l, l)
      gl.glVertex3f(l, -l, -l)

      gl.glEnd()
    }

  }

  implicit val pyramidRenderer = new Render[Pyramid] {
    def render(pyramid: Pyramid, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import pyramid._
      val l = length.magnitude.toFloat
      gl.glBegin(GL_TRIANGLES)

      gl.glColor3f(color.red, color.green, color.blue)

      gl.glVertex3f(0f, l, 0f)
      gl.glVertex3f(-l, -l, l)
      gl.glVertex3f(l, -l, l)

      gl.glVertex3f(0f, l, 0f)
      gl.glVertex3f(l, -l, l)
      gl.glVertex3f(l, -l, -l)

      gl.glVertex3f(0f, l, 0f)
      gl.glVertex3f(l, -l, -l)
      gl.glVertex3f(-l, -l, -l)

      gl.glVertex3f(0f, l, 0f)
      gl.glVertex3f(-l, -l, -l)
      gl.glVertex3f(-l, -l, l)

      gl.glEnd()
    }
  }

  implicit val multiColorPyramidRenderer = new Render[MultiColorPyramid] {
    def render(pyramid: MultiColorPyramid, scene: Scene, gl: GL2, glu: GLU): Unit = {
      import pyramid._
      val l = length.magnitude.toFloat
      gl.glBegin(GL_TRIANGLES)

      // front
      gl.glColor3f(c1.red, c1.green, c1.blue)
      gl.glVertex3f(0f, l, 0f)
      gl.glColor3f(c2.red, c2.green, c2.blue)
      gl.glVertex3f(-l, -l, l)
      gl.glColor3f(c3.red, c3.green, c3.blue)
      gl.glVertex3f(l, -l, l)

      // right
      gl.glColor3f(c1.red, c1.green, c1.blue)
      gl.glVertex3f(0f, l, 0f)
      gl.glColor3f(c3.red, c3.green, c3.blue)
      gl.glVertex3f(l, -l, l)
      gl.glColor3f(c2.red, c2.green, c2.blue)
      gl.glVertex3f(l, -l, -l)

      // back
      gl.glColor3f(c1.red, c1.green, c1.blue)
      gl.glVertex3f(0f, l, 0f)
      gl.glColor3f(c2.red, c2.green, c2.blue)
      gl.glVertex3f(l, -l, -l)
      gl.glColor3f(c3.red, c3.green, c3.blue)
      gl.glVertex3f(-l, -l, -l)

      // left
      gl.glColor3f(c1.red, c1.green, c1.blue)
      gl.glVertex3f(0f, l, 0f)
      gl.glColor3f(c3.red, c3.green, c3.blue)
      gl.glVertex3f(-l, -l, -l)
      gl.glColor3f(c2.red, c2.green, c2.blue)
      gl.glVertex3f(-l, -l, l)

      gl.glEnd()
    }
  }

  implicit val texturedCubeRenderer = new Render[TexturedCube] {

    def render(cube: TexturedCube, scene: Scene, gl: GL2, glu: GLU): Unit = {

      import cube._

      val l = length.magnitude.toFloat

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
      gl.glVertex3f(-l, -l, l)
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(l, -l, l)
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(l, l, l)
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(-l, l, l)

      // Back Face
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(-l, -l, -l)
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(-l, l, -l)
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(l, l, -l)
      gl.glTexCoord2f(textureLeft, textureBottom)
      gl.glVertex3f(l, -l, -l)

      // Top Face
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(-l, l, -l)
      gl.glTexCoord2f(textureLeft, textureBottom)
      gl.glVertex3f(-l, l, l)
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(l, l, l)
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(l, l, -l)

      // Bottom Face
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(-l, -l, -l)
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(l, -l, -l)
      gl.glTexCoord2f(textureLeft, textureBottom)
      gl.glVertex3f(l, -l, l)
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(-l, -l, l)

      // Right face
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(l, -l, -l)
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(l, l, -l)
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(l, l, l)
      gl.glTexCoord2f(textureLeft, textureBottom)
      gl.glVertex3f(l, -l, l)

      // Left Face
      gl.glTexCoord2f(textureLeft, textureBottom)
      gl.glVertex3f(-l, -l, -l)
      gl.glTexCoord2f(textureRight, textureBottom)
      gl.glVertex3f(-l, -l, l)
      gl.glTexCoord2f(textureRight, textureTop)
      gl.glVertex3f(-l, l, l)
      gl.glTexCoord2f(textureLeft, textureTop)
      gl.glVertex3f(-l, l, -l)

      gl.glEnd()

      texture.disable(gl)
    }

  }
}
