# Units of Measurement

## Quanta

### Quanta, Units, and Conversions

`UnittedQuantity` is the primary case class in `axle.quanta`

The `axle.quanta` package models units of measurement.
Via typeclasses, it implements expected operators like `+`, `-`,
a unit conversion operator `in`,
and a right associative value constructor `*:`

The "quanta" are
Acceleration, Area, Angle, Distance, Energy, 
Flow, Force, Frequency, Information, Mass, Money, MoneyFlow, MoneyPerForce, Power, Speed, Temperature, Time, and Volume.
Axle's values are represented in such a way that a value's "quantum" is present in the type,
meaning that nonsensical expressions like `mile + gram` can be rejected at compile time.

Additionally, various values within the Quantum objects are imported.
This package uses the definition of "Quantum" as "something that can
be quantified or measured".

```scala mdoc:silent
import axle._
import axle.quanta._
import axle.jung._
```

Quanta each define a Wikipedia link where you can find out more
about relative scale:

```scala mdoc
Distance().wikipediaUrl
```

A visualization of the Units of Measurement for a given Quantum can be produced by first creating the converter:

```scala mdoc:silent
import edu.uci.ics.jung.graph.DirectedSparseGraph
import cats.implicits._
import spire.algebra.Field
import axle.algebra.modules.doubleRationalModule

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val distanceConverter = Distance.converterGraphK2[Double, DirectedSparseGraph]
```

Create a `DirectedGraph` visualization for it.

```scala mdoc:silent
import cats.Show

implicit val showDDAt1 = new Show[Double => Double] {
  def show(f: Double => Double): String = f(1d).toString
}

import axle.visualize._

val dgVis =
  DirectedGraphVisualization[
    DirectedSparseGraph[UnitOfMeasurement[Distance],Double => Double],
    UnitOfMeasurement[Distance], Double => Double](distanceConverter.conversionGraph)
```

Render to an SVG.

```scala mdoc:silent
import axle.web._
import cats.effect._

dgVis.svg[IO]("@DOCWD@/images/Distance.svg").unsafeRunSync()
```

![Distance conversions](/images/Distance.svg)

### Units

A conversion graph must be created with type parameters specifying the numeric type to
be used in unitted quantity, as well as a directed graph type that will store the conversion
graph.
The conversion graphs should be placed in implicit scope.
Within each are defined units of measurement which can be imported.

```scala mdoc:silent
implicit val massConverter = Mass.converterGraphK2[Double, DirectedSparseGraph]
import massConverter._

implicit val powerConverter = Power.converterGraphK2[Double, DirectedSparseGraph]
import powerConverter._

import axle.algebra.modules.doubleRationalModule

// reuse distanceConverter defined in preceding section
import distanceConverter._

implicit val timeConverter = Time.converterGraphK2[Double, DirectedSparseGraph]
import timeConverter._
```

Standard Units of Measurement are defined:

```scala mdoc
gram

foot

meter
```

### Construction

Values with units are constructed with the right-associative `*:` method on any spire `Number` type
as long as a spire `Field` is implicitly available.

```scala mdoc:silent
10d *: gram

3d *: lightyear

5d *: horsepower

3.14 *: second

200d *: watt
```

### Show

A witness for the `cats.Show` typeclass is defined.
`show` will return a `String` representation.

```scala mdoc
import cats.implicits._

(10d *: gram).show
```

### Conversion

A Quantum defines a directed graph, where the UnitsOfMeasurement
are the vertices, and the Conversions define the directed edges.
See [Graph Theory](GraphTheory.md) for more on how graphs work.

Quantities can be converted into other units of measurement.
This is possible as long as 1) the values are in the same
Quantum, and 2) there is a path in the Quantum between the two.

```scala mdoc
(10d *: gram in kilogram).show
```

Converting between quanta is not allowed, and is caught at compile time:

```scala mdoc:fail
(1 *: gram) in mile
```

### Math

Addition and subtraction are defined on Quantity by converting the
right Quantity to the unit of the left.

```scala mdoc
import spire.implicits.additiveGroupOps

((7d *: mile) - (123d *: foot)).show
```

```scala mdoc
{
  import spire.implicits._
  ((1d *: kilogram) + (10d *: gram)).show
}
```

Addition and subtraction between different quanta is rejected at compile time:

```scala mdoc:fail
(1d *: gram) + (2d *: foot)
```

Scalar multiplication comes from Spire's `CModule` typeclass:

```scala mdoc
import spire.implicits.rightModuleOps

((5.4 *: second) :* 100d).show
```

```scala mdoc
((32d *: century) :* (1d/3)).show
```

## Unitted Trigonometry

Versions of the trigonometric functions sine, cosine, and tangent, require that the arguments are Angles.

### Preamble

Imports, implicits, etc

```scala mdoc:silent:reset
import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.algebra.Field
import spire.algebra.Trig

import axle.math._
import axle.quanta.Angle
import axle.quanta.UnitOfMeasurement
import axle.algebra.modules.doubleRationalModule
import axle.jung.directedGraphJung

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val trigDouble: Trig[Double] = spire.implicits.DoubleAlgebra

implicit val angleConverter = Angle.converterGraphK2[Double, DirectedSparseGraph]

import angleConverter.degree
import angleConverter.radian
```

### Examples

```scala mdoc
cosine(10d *: degree)

sine(3d *: radian)

tangent(40d *: degree)
```

## Geo Coordinates

Imports and implicits

```scala mdoc:silent:reset
import edu.uci.ics.jung.graph.DirectedSparseGraph

import cats.implicits._

import spire.algebra.Field
import spire.algebra.Trig
import spire.algebra.NRoot

import axle._
import axle.quanta._
import axle.algebra.GeoCoordinates
import axle.jung.directedGraphJung
import axle.algebra.modules.doubleRationalModule

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val trigDouble: Trig[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

implicit val angleConverter = Angle.converterGraphK2[Double, DirectedSparseGraph]
import angleConverter.°
```

Locations of SFO and HEL airports:

```scala mdoc:silent
val sfo = GeoCoordinates(37.6189 *: °, 122.3750 *: °)
```

```scala mdoc
sfo.show
```

```scala mdoc:silent
val hel = GeoCoordinates(60.3172 *: °, -24.9633 *: °)
```

```scala mdoc
hel.show
```

Import the `LengthSpace`

```scala mdoc
import axle.algebra.GeoCoordinates.geoCoordinatesLengthSpace
```

Use it to compute the points at 10% increments from SFO to HEL

```scala mdoc:silent
val midpoints = (0 to 10).map(i => geoCoordinatesLengthSpace.onPath(sfo, hel, i / 10d))
```

```scala mdoc
midpoints.map(_.show)
```

![SFO to HEL](/images/sfo_hel.png)

## Future Work

The methods `over` and `by` are used to multiply and divide other values with units.
This behavior is not yet implemented.

* Shapeless for compound Quanta and Bayesian Networks
* Physics (eg, how Volume relates to Flow)
* Rm throws from axle.quanta.UnitConverterGraph
