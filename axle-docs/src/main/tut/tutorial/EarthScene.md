---
layout: page
title: Earth Scene
permalink: /tutorial/earth_scene/
---

Imports

```tut:silent
import java.net.URL
import java.util.Date

import scala.Vector

import com.jogamp.opengl.GL2

import axle.algebra.GeoCoordinates
import axle.algebra.SphericalVector
import axle.algebra.modules.floatDoubleModule
import axle.algebra.modules.floatRationalModule
import axle.jung.directedGraphJung
import axle.quanta.Angle
import axle.quanta.Distance
import axle.quanta.UnittedQuantity
import edu.uci.ics.jung.graph.DirectedSparseGraph
import spire.implicits.FloatAlgebra
import spire.implicits.additiveGroupOps
import spire.implicits.moduleOps

import axle.jogl._
import Color._
```

Distance and Angle unit conversions

```tut:silent
implicit val ddc = {
  import axle.algebra.modules.doubleRationalModule
  import spire.implicits.DoubleAlgebra
  Distance.converterGraphK2[Double, DirectedSparseGraph]
}

implicit val distanceConverter = Distance.converterGraphK2[Float, DirectedSparseGraph]
import distanceConverter._

implicit val angleConverter = Angle.converterGraphK2[Float, DirectedSparseGraph]
import angleConverter._

val zeroDegrees = 0f *: °
```

Constants and scalars

```tut:book
val millisPerDay = 1000f * 60 * 60 * 24

val startTimeMillis = new Date().getTime
val simulatedStartTime = new Date()
val simulatedStartTimeMillis = simulatedStartTime.getTime

val timeCoefficient = 3600f // simulate one hour each second
```

Camera position

```tut:book
val cameraDistance = 13000f *: km

// http://www.sjsu.edu/faculty/watkins/elevsun.htm
val cameraCoordinates = GeoCoordinates(39.828328f *: °, -98.579416f *: °)
```

Earth and Moon spheres

```tut:book
val earthRadius = 6371f *: km

val earth = Sphere(earthRadius, 96, 64, blue)

def earthOrienter(t: Long)(gl: GL2): Unit = {
  translate(gl, km, 0f *: km, 0f *: km, -1f *: cameraDistance)
  rotate(gl, cameraCoordinates.latitude, 1f, 0f, 0f)
  rotate(gl, cameraCoordinates.longitude, 0f, -1f, 0f)
  rotate(gl, 90f *: °, -1f, 0f, 0f)
}

// Note: Moon measurements are not accurate

val moonSphere = Sphere(1000f *: km, 48, 16, white)

def moonOrienter(t: Long)(gl: GL2): Unit = {
  translate(gl, km, 7500f *: km, 3500f *: km, -13000f *: km)
  rotate(gl, (-360f * (t / millisPerDay)) *: °, 0f, 1f, 0f)
  rotate(gl, 90f *: °, -1f, 0f, 0f)
}
```

Sun (the light source)

```
val sunDistance = 1f *: au
```

Airports

```tut:book
case class Airport(icaoCode: String, coords: GeoCoordinates[Float], altitude: UnittedQuantity[Distance, Float])

val airportMark = Sphere(100f *: km, 10, 10, red)

val sfo = Airport("SFO", GeoCoordinates(37.6189f *: °, -122.3750f *: °), 13f *: ft)
val jfk = Airport("JFK", GeoCoordinates(40.6413f *: °, -73.7781f *: °), 13.12f *: ft)

val airports = sfo :: jfk :: Nil

def airportOrienter(airport: Airport, t: Long)(gl: GL2): Unit = {
  translate(gl, km, 0f *: km, 0f *: km, -1f *: cameraDistance)
  rotate(gl, cameraCoordinates.latitude - airport.coords.latitude, 1f, 0f, 0f)
  rotate(gl, airport.coords.longitude - cameraCoordinates.longitude, 0f, 1f, 0f)
  translate(gl, km, 0f *: km, 0f *: km, earth.radius)
}
```

Scene renderer

```tut:book
def renderAll(gl: GL2, rc: RenderContext, t: Long): Unit = {

  val sunVector = SphericalVector[Float](sunDistance, (-360f * (t / millisPerDay)) *: °, zeroDegrees)
  gl.glLoadIdentity()
  positionLight(sunVector.toPosition, km, gl)

  render(moonSphere, moonOrienter(t) _, gl, rc)

  render(earth, earthOrienter(t) _, gl, rc)

  airports foreach { airport =>
    render(airportMark, airportOrienter(airport, t) _, gl, rc)
  }

}
```

State-changing function

```tut:book
def tic(previous: Long): Long = {
  val actualMillisElapsed = new Date().getTime - startTimeMillis
  simulatedStartTimeMillis + (actualMillisElapsed * timeCoefficient).toLong
}
```

SceneFrame

```tut:book
val sceneFrame = SceneFrame(
  renderAll,
  startTimeMillis,
  tic,
  "Axle Scene Demo",
  Vector.empty,  // texture (URL, String) pairs (second half is file extension)
  km,
  640,           // width
  480,           // height
  700f *: km,    // zNear
  700000f *: km, // zFar
  30)            // fps (frames per second)
```

Run the scene

```scala
sceneFrame.run()
```

![earth scene](/tutorial/images/earth_scene.png)
