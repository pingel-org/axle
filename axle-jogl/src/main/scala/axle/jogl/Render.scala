package axle.jogl

import scala.Vector
import scala.annotation.implicitNotFound

import com.jogamp.opengl.GL.GL_FRONT
import com.jogamp.opengl.GL.GL_TRIANGLES
import com.jogamp.opengl.GL2
//import com.jogamp.opengl.GL2GL3.GL_QUADS
import com.jogamp.opengl.GL2GL3.GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION
import com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT
import com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SHININESS
import com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR
import com.jogamp.opengl.glu.GLU

import axle.scene._

@implicitNotFound("Witness not found for Render[${A}]")
trait Render[A] {
  def render(value: A, rc: RenderContext, gl: GL2): Unit
}

object Render {

  final def apply[A](implicit render: Render[A]): Render[A] = render

  implicit val quadRenderer = new Render[Quad[Float]] {
    def render(quad: Quad[Float], rc: RenderContext, gl: GL2): Unit = {
      import quad._
      val w = width.magnitude / 2
      val h = height.magnitude / 2
      gl.glColor3f(color.red, color.green, color.blue)
      gl.glBegin(GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION)
      gl.glVertex3f(-w, h, 0f)
      gl.glVertex3f(w, h, 0f)
      gl.glVertex3f(w, -h, 0f)
      gl.glVertex3f(-w, -h, 0f)
      gl.glEnd()
    }
  }

  implicit val coloredSphereRenderer = new Render[Sphere[Float]] {
    def render(sphere: Sphere[Float], rc: RenderContext, gl: GL2): Unit = {
      import rc.glu
      import sphere._
      gl.glColor3f(color.red, color.green, color.blue)
      glu.gluSphere(glu.gluNewQuadric(), radius.magnitude.toDouble, slices, stacks)
    }
  }

  //  val colorBlack = java.nio.FloatBuffer.wrap(Vector(0f, 0f, 0f, 1f).toArray)
  //  val colorBlue = java.nio.FloatBuffer.wrap(Vector(0f, 0f, 1f, 1f).toArray)
  //  val colorRed = java.nio.FloatBuffer.wrap(Vector(1f, 0f, 0f, 1f).toArray)
  val rgba = Vector(1f, 1f, 1f).toArray

  implicit val sphereRenderer = new Render[TexturedSphere[Float]] {
    def render(sphere: TexturedSphere[Float], rc: RenderContext, gl: GL2): Unit = {

      import rc.glu

      import sphere._
      gl.glColor3f(reflectionColor.red, reflectionColor.green, reflectionColor.blue)
      gl.glMaterialfv(GL_FRONT, GL_AMBIENT, rgba, 0)
      gl.glMaterialfv(GL_FRONT, GL_SPECULAR, rgba, 0)
      gl.glMaterialf(GL_FRONT, GL_SHININESS, 0.5f)

      val texture = rc.url2texture(textureUrl)
      texture.enable(gl)
      texture.bind(gl)

      val quadratic = glu.gluNewQuadric()
      glu.gluQuadricTexture(quadratic, true)
      glu.gluQuadricDrawStyle(quadratic, GLU.GLU_FILL)
      glu.gluQuadricNormals(quadratic, GLU.GLU_FLAT)
      glu.gluQuadricOrientation(quadratic, GLU.GLU_OUTSIDE)
      glu.gluSphere(quadratic, radius.magnitude.toDouble, slices, stacks)
      glu.gluDeleteQuadric(quadratic)
    }
  }

  implicit val triangleRenderer = new Render[Triangle[Float]] {
    def render(triangle: Triangle[Float], rc: RenderContext, gl: GL2): Unit = {
      import triangle._
      val l = length.magnitude
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

  implicit val triColorTriangleRenderer = new Render[TriColorTriangle[Float]] {
    def render(triangle: TriColorTriangle[Float], rc: RenderContext, gl: GL2): Unit = {
      import triangle._
      val l = length.magnitude
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

  implicit val cubeRenderer = new Render[Cube[Float]] {
    def render(cube: Cube[Float], rc: RenderContext, gl: GL2): Unit = {
      import cube._
      val l = length.magnitude
      gl.glBegin(GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION)

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

  implicit val multiColorCubeRenderer = new Render[MultiColorCube[Float]] {
    def render(cube: MultiColorCube[Float], rc: RenderContext, gl: GL2): Unit = {
      import cube._
      val l = length.magnitude
      gl.glBegin(GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION)

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

  implicit val pyramidRenderer = new Render[Pyramid[Float]] {
    def render(pyramid: Pyramid[Float], rc: RenderContext, gl: GL2): Unit = {
      import pyramid._
      val l = length.magnitude
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

  implicit val multiColorPyramidRenderer = new Render[MultiColorPyramid[Float]] {
    def render(pyramid: MultiColorPyramid[Float], rc: RenderContext, gl: GL2): Unit = {
      import pyramid._
      val l = length.magnitude
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

  implicit val texturedCubeRenderer = new Render[TexturedCube[Float]] {

    def render(cube: TexturedCube[Float], rc: RenderContext, gl: GL2): Unit = {

      import cube._

      val l = length.magnitude

      val texture = rc.url2texture(textureUrl)

      val textureCoords = texture.getImageTexCoords()
      val textureTop = textureCoords.top()
      val textureBottom = textureCoords.bottom()
      val textureLeft = textureCoords.left()
      val textureRight = textureCoords.right()

      gl.glColor3f(1f, 1f, 1f)

      texture.enable(gl)
      // gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE)
      texture.bind(gl)

      gl.glBegin(GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION)

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
