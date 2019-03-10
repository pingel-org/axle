package axle.jogl

import org.scalatest._

import java.util.Date

import com.jogamp.opengl.GL2

import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.algebra._
import spire.implicits.additiveGroupOps
import spire.implicits.moduleOps

import axle.algebra.GeoCoordinates
import axle.algebra.SphericalVector
import axle.algebra.modules.floatRationalModule
import axle.jung.directedGraphJung
import axle.quanta.Angle
import axle.quanta.Distance
import axle.quanta.UnittedQuantity

class EarthSceneSpec extends FunSuite with Matchers {

  test("axle.jogl earth scene") {

    //    implicit val ddc = {
    //      import axle.algebra.modules.doubleRationalModule
    //      implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
    //      Distance.converterGraphK2[Double, DirectedSparseGraph]
    //    }


    implicit val fieldFloat: Field[Float] = spire.implicits.FloatAlgebra
    implicit val trigFloat: Trig[Float] = spire.implicits.FloatAlgebra

    implicit val distanceConverter = Distance.converterGraphK2[Float, DirectedSparseGraph]
    import distanceConverter._

    implicit val angleConverter = Angle.converterGraphK2[Float, DirectedSparseGraph]
    import angleConverter._

    case class Airport(icaoCode: String, coords: GeoCoordinates[Float], altitude: UnittedQuantity[Distance, Float])

    import Color._

    // val moonUrl = new URL("file:///Users/pingel/github.com/adampingel/axle/axle-docs/src/site/images/axle.png")

    // see http://planetpixelemporium.com/earth.html
    // val earthUrl = new URL("file:///tmp/8081-earthmap10k.jpg")

    val cameraDistance = 13000f *: km
    // http://www.sjsu.edu/faculty/watkins/elevsun.htm
    val cameraCoordinates = GeoCoordinates(39.828328f *: °, -98.579416f *: °)

    // Note: Moon measurements are not accurate

    // val moonSphere = TexturedSphere(1000f *: km, 48, 16, white, moonUrl, "png")
    val moonSphere = Sphere(1000f *: km, 48, 16, white)

    val earthRadius = 6371f *: km

    // val earth = TexturedSphere(earthRadius, 96, 64, white, earthUrl, "jpg")
    val earth = Sphere(earthRadius, 96, 64, blue)

    // val airportMark = Cube(1000f *: km, red)
    val airportMark = Sphere(100f *: km, 10, 10, red)

    val sunDistance = 1f *: au
    val zeroDegrees = 0f *: °

    val millisPerDay = 1000f * 60 * 60 * 24

    val sfo = Airport("SFO", GeoCoordinates(37.6189f *: °, -122.3750f *: °), 10013f *: ft)
    val jfk = Airport("JFK", GeoCoordinates(40.6413f *: °, -73.7781f *: °), 10013.12f *: ft)

    val airports = sfo :: jfk :: Nil

    def moonOrienter(t: Long)(gl: GL2): Unit = {
      translate(gl, km, 7500f *: km, 3500f *: km, -13000f *: km)
      rotate(gl, (-360f * (t / millisPerDay)) *: °, 0f, 1f, 0f)
      rotate(gl, 90f *: °, -1f, 0f, 0f)
    }

    //    def earthOrienter(t: Long)(gl: GL2): Unit = {
    //      translate(gl, km, 0f *: km, 0f *: km, -1f *: cameraDistance)
    //      rotate(gl, cameraCoordinates.latitude, 1f, 0f, 0f)
    //      rotate(gl, cameraCoordinates.longitude, 0f, -1f, 0f)
    //      rotate(gl, 90f *: °, -1f, 0f, 0f)
    //    }

    def airportOrienter(airport: Airport, t: Long)(gl: GL2): Unit = {
      translate(gl, km, 0f *: km, 0f *: km, -1f *: cameraDistance)
      rotate(gl, cameraCoordinates.latitude - airport.coords.latitude, 1f, 0f, 0f)
      rotate(gl, airport.coords.longitude - cameraCoordinates.longitude, 0f, 1f, 0f)
      translate(gl, km, 0f *: km, 0f *: km, earth.radius)
    }

    def renderAll(gl: GL2, rc: RenderContext, t: Long): Unit = {

      // val renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36))

      val sunVector = SphericalVector[Float](sunDistance, (-360f * (t / millisPerDay)) *: °, zeroDegrees)

      gl.glLoadIdentity()
      positionLight(sunVector.toPosition, km, gl)

      render(moonSphere, moonOrienter(t) _, gl, rc)
      // render(earth, earthOrienter(t) _, gl, rc)

      airports foreach { airport =>
        render(airportMark, airportOrienter(airport, t) _, gl, rc)
      }

      // renderer.beginRendering(glu.getWidth(), drawable.getHeight())
      // renderer.setColor(1.0f, 0.2f, 0.2f, 0.8f)
      // renderer.draw("Text to draw", 10, 10)
      // renderer.endRendering()

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
      "Axle Earth Scene Demo",
      Vector.empty, // Vector((moonUrl, "png"), (earthUrl, "jpg")),
      km,
      width,
      height,
      zNear,
      zFar,
      fps)

    sceneFrame.run()
    Thread.sleep(5000L)
    sceneFrame.canvas.destroy()

    sceneFrame.title should be("Axle Earth Scene Demo")
  }
}
