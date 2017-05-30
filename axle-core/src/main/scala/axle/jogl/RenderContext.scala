package axle.jogl

import java.net.URL
import com.jogamp.opengl.util.texture.Texture
import com.jogamp.opengl.glu.GLU

case class RenderContext(
  url2texture: Map[URL, Texture],
  glu: GLU)
