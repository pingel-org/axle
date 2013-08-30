package axle.visualize.gl

import java.net.URL

import com.jogamp.opengl.util.texture.Texture
import com.jogamp.opengl.util.texture.TextureIO

import javax.media.opengl.GL2
import javax.media.opengl.glu.GLU

abstract class Scene {

  def render[A: Render](value: A, orienter: GL2 => Unit, gl: GL2, glu: GLU) = {
    gl.glLoadIdentity()
    orienter(gl)
    implicitly[Render[A]].render(value, this, gl, glu)
  }

  val url2texture = collection.mutable.Map.empty[URL, Texture]

  def registerTexture(url: URL, texture: Texture) = url2texture += url -> texture

  def textureFor(url: URL): Texture = url2texture(url)

  def registerTextures() =
    textureUrls foreach {
      case (url, extension) =>
        url2texture += url -> TextureIO.newTexture(url, false, extension)
    }

  def textureUrls: Seq[(URL, String)]

  def title: String

  def renderAll(gl: GL2, glu: GLU): Unit

  def tic(): Unit
}
