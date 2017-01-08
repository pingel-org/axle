package axle.jogl

import org.scalatest._

import java.util.Date

import com.jogamp.opengl.GL2
import edu.uci.ics.jung.graph.DirectedSparseGraph
import java.nio.file.Paths
import spire.implicits._
import axle.algebra.GeoCoordinates
import axle.algebra.SphericalVector
import axle.algebra.modules.floatDoubleModule
import axle.algebra.modules.floatRationalModule
import axle.jung.directedGraphJung
import axle.quanta.Angle
import axle.quanta.Distance

class ShapesSpec extends FunSuite with Matchers {

  test("axle.jogl shapes") {

    implicit val ddc = {
      import axle.algebra.modules.doubleRationalModule
      Distance.converterGraphK2[Double, DirectedSparseGraph]
    }

    implicit val distanceConverter = Distance.converterGraphK2[Float, DirectedSparseGraph]
    import distanceConverter._

    implicit val angleConverter = Angle.converterGraphK2[Float, DirectedSparseGraph]
    import angleConverter._

    import Color._

    val cameraDistance = 13000f *: km
    val cameraCoordinates = GeoCoordinates(39.828328f *: °, -98.579416f *: °)

    import java.net.URL
    val textureUrl: URL = Paths.get("axle-docs/src/site/images/axle.png").toUri().toURL()

    val sphere = Sphere(1000f *: km, 24, 8, white)
    val texturedSphere = TexturedSphere(1000f *: km, 24, 8, blue, textureUrl, "png")
    val cube = Cube(1000f *: km, red)
    val texturedCube = TexturedCube(1000f *: km, blue, textureUrl, "png")
    val triangle = Triangle(1000f *: km, blue)
    val tritri = TriColorTriangle(1000f *: km, yellow, blue, red)
    val pyramid = Pyramid(1000f *: km, green)
    val multipyr = MultiColorPyramid(1000f *: km, yellow, blue, red)
    val quad = Quad(1000f *: km, 1000f *: km, red)
    val multicube = MultiColorCube(1000f *: km, red, blue, green, white, black, yellow)

    val sunDistance = 1f *: au
    val zeroDegrees = 0f *: °

    val millisPerDay = 1000f * 60 * 60 * 24

    def shapeOrienter(t: Long)(gl: GL2): Unit = {
      translate(gl, km, 7500f *: km, 3500f *: km, -13000f *: km)
      rotate(gl, (-360f * (t / millisPerDay)) *: °, 0f, 1f, 0f)
      rotate(gl, 90f *: °, -1f, 0f, 0f)
    }

    def renderAll(gl: GL2, rc: RenderContext, t: Long): Unit = {

      // val renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36))

      val sunVector = SphericalVector[Float](sunDistance, (-360f * (t / millisPerDay)) *: °, zeroDegrees)

      gl.glLoadIdentity()
      positionLight(sunVector.toPosition, km, gl)

      render(sphere, shapeOrienter(t) _, gl, rc)
      render(texturedSphere, shapeOrienter(t) _, gl, rc)
      render(cube, shapeOrienter(t) _, gl, rc)
      render(texturedCube, shapeOrienter(t) _, gl, rc)
      render(triangle, shapeOrienter(t) _, gl, rc)
      render(tritri, shapeOrienter(t) _, gl, rc)
      render(pyramid, shapeOrienter(t) _, gl, rc)
      render(multipyr, shapeOrienter(t) _, gl, rc)
      render(quad, shapeOrienter(t) _, gl, rc)
      render(multicube, shapeOrienter(t) _, gl, rc)

    }

    val startTimeMillis = new Date().getTime
    val simulatedStartTime = new Date()
    val simulatedStartTimeMillis = simulatedStartTime.getTime

    val timeCoefficient = 3600f // simulate one hour each second

    def tic(previous: Long): Long = {
      val actualMillisElapsed = new Date().getTime - startTimeMillis
      simulatedStartTimeMillis + (actualMillisElapsed * timeCoefficient).toLong
    }

    val width = 640
    val height = 480
    val zNear = 700f *: km
    val zFar = 700000f *: km
    val fps = 2

    val sceneFrame = SceneFrame[Long](
      renderAll,
      startTimeMillis,
      tic,
      "Axle JOGL Shape Demo",
      Vector((textureUrl, "png")),
      km,
      width,
      height,
      zNear,
      zFar,
      fps)

    sceneFrame.run()
    Thread.sleep(5000L)
    sceneFrame.canvas.destroy()

    sceneFrame.title should be("Axle JOGL Shape Demo")
  }

}
