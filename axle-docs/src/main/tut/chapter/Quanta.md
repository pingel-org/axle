Quanta, Units, and Conversions
==============================

`UnittedQuantity` is the primary case class in `axle.quanta`

Introduction
------------

The `axle.quanta` package models units of measurement.
Via typeclasses, it implements expected operators like `+ - * over by`,
a unit conversion operator `in`,
and a right associative value constructor `*:`

The "quanta" are
Acceleration, Area, Angle, 
<a href={"https://github.com/adampingel/axle/blob/master/axle-core/src/main/scala/axle/quanta/Distance.scala"}>Distance</a>,
<a href={"https://github.com/adampingel/axle/blob/master/axle-core/src/main/scala/axle/quanta/Energy.scala"}>Energy</a>,
Flow, Force, Frequency, Information, Mass, Money, MoneyFlow, MoneyPerForce, Power, Speed, Temperature,
<a href={"https://github.com/adampingel/axle/blob/master/axle-core/src/main/scala/axle/quanta/Time.scala"}>Time</a>,
and Volume.
Axle's values are represented in such a way that a value's "quantum" is present in the type,
meaning that nonsensical expressions like `mile + gram` can be rejected at compile time.

![Distance conversions](../images/Distance.svg)

Additionally, various values within the Quantum objects are imported.
This package uses the definition of "Quantum" as "something that can
be quantified or measured".

```tut
import axle._
import axle.quanta._
import axle.jung.directedGraphJung
import spire.implicits.additiveSemigroupOps
import spire.implicits.additiveGroupOps
import spire.implicits.moduleOps
```

Quanta each define a Wikipedia link where you can find out more
about relative scale:

```tut
Distance().wikipediaUrl
```

A visualization of each Quantum (like the one for Distance shown above) is produced with:

```tut
import axle.algebra.modules.doubleRationalModule
import spire.implicits.DoubleAlgebra
import axle.jung.directedGraphJung
import edu.uci.ics.jung.graph.DirectedSparseGraph

implicit val distanceConverter = Distance.converterGraphK2[Double, DirectedSparseGraph]

implicit val showDDAt1 = new Show[Double => Double] {
  def text(f: Double => Double): String = f(1d).toString
}

import axle.visualize._
import axle.web._

svg(distanceConverter.conversionGraph, "Distance.svg")
```

// TODO
// import axle.syntax.directedgraph.directedGraphOps
// import axle.syntax.functor.functorOps
// val g = cg.mapEdges(f => f(1d))

Units
-----

A conversion graph must be created with type parameters specifying the numeric type to
be used in unitted quantity, as well as a directed graph type that will store the conversion
graph.
The conversion graphs should be placed in implicit scope.
Within each are defined units of measurement which can be imported.

```tut
import spire.implicits.DoubleAlgebra

implicit val massConverter = Mass.converterGraphK2[Double, DirectedSparseGraph]
import massConverter._

implicit val powerConverter = Power.converterGraphK2[Double, DirectedSparseGraph]
import powerConverter._

implicit val energyConverter = Energy.converterGraphK2[Double, DirectedSparseGraph]
import energyConverter._

import axle.algebra.modules.doubleRationalModule

implicit val distanceConverter = Distance.converterGraphK2[Double, DirectedSparseGraph]
import distanceConverter._

implicit val timeConverter = Time.converterGraphK2[Double, DirectedSparseGraph]
import timeConverter._
```

Standard Units of Measurement are defined:

```tut
gram

foot

meter
```

Construction
------------

Values with units are constructed with the right-associative `*:` method on any spire `Number` type
as long as a spire `Field` is implicitly available.

```tut
10d *: gram

3d *: lightyear

5d *: horsepower

3.14 *: second

200d *: watt
```

Conversion
----------

A Quantum defines a directed graph, where the UnitsOfMeasurement
are the vertices, and the Conversions define the directed edges.
See the <a href={"/graph.html"}>Graph</a> package for more on how graphs work.

Quantities can be converted into other units of measurement.
This is possible as long as 1) the values are in the same
Quantum, and 2) there is a path in the Quantum between the two.

```tut
10d *: gram in kilogram
```

Converting between quanta is not allowed, and is caught at compile time:

```tut:fail
(1 *: gram) in mile
```

Show
----

A witness for the `Show` typeclass is defined, meaning that `string` will return
a `String` representation, and `show` will send it to stdout.

```tut
string(10d *: gram in kilogram)
```

Math
----

Addition and subtraction are defined on Quantity by converting the
right Quantity to the unit of the left.

```tut
(1d *: kilogram) + (10d *: gram)

(7d *: mile) - (123d *: foot)
```

Addition and subtraction between different quanta is rejected at compile time:

```tut:fail
(1d *: newton) + (2d *: foot)
```

Multiplication comes from spire's Module typeclass:

```tut
(5.4 *: second) :* 100d

(32d *: century) :* (1d/3)
```

The methods `over` and `by` are used to multiply and divide other values with units.
This behavior is not yet implemented.