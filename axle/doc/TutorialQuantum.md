
Quanta, Quantities, and Units
=============================

The org.pingel.axle.quanta package is an experiment in units of measurement.
It implements expected operators like +, -, *, and /, and unit conversion.

The fun part are the non-standard units of measurement.
How many 60 Watt light bulbs can the Hoover Dam power at peak capacity?
How many parsecs to the Andromeda Galaxy?
Find out below.

Hopefully this creates a sandbox for experimenting with the scale of things.
Like an interactive ["Powers of 10" video](http://www.youtube.com/watch?v=0fKBhvDjuy0)

See the bottom of this page for some possible next steps for this code.

The following code snippets are all preceded by this block of import statements:

```scala
import org.pingel.axle.quanta._
import Quantity._
import Scalar._
```

Additionally, various values within the Quantum objects are imported.
This package uses the definition of "Quantum" as "something that can
be quantified or measured".

```scala
import Mass._
import Time._
import Energy._
import Distance._
import Power._
import Speed._
import Acceleration._
import Force._
import Area._
import Volume._
import Flow._
```

Quanta each define a Wikipedia link where you can find out more
about relative scale:

```scala
scala> Distance.wikipediaUrl
res3: java.lang.String = http://en.wikipedia.org/wiki/Orders_of_magnitude_(length)
```

Units
=====

Standard Units of Measurement are defined:

```scala
scala> gram
res0: org.pingel.axle.quanta.UnitOfMeasurement = gram (g): a measure of Mass$

scala> foot
res3: org.pingel.axle.quanta.UnitOfMeasurement = foot (ft): a measure of Distance$

scala> meter
res4: org.pingel.axle.quanta.UnitOfMeasurement = meter (m): a measure of Distance$
```

All units of measurement specify thier Quantum:

```scala
scala> gram.quantum
res1: org.pingel.axle.quanta.Quantum = Mass$

scala> meter.quantum
res5: org.pingel.axle.quanta.Quantum = Distance$

scala> second.quantum
res6: org.pingel.axle.quanta.Quantum = Time$
```

Quantities
==========

There are also quite a few non-standard interesting Quantities defined.
Any quantity can be used as a UnitOfMeasurement.

```scala
scala> earth
res3: org.pingel.axle.quanta.Quantity = 5.9736 Zt

scala> man
res12: org.pingel.axle.quanta.Quantity = 86.6 Kg

scala> niagaraFalls
res0: org.pingel.axle.quanta.Quantity = 1834 mmm/s

scala> greatLakes
res1: org.pingel.axle.quanta.Quantity = 22671 KmKmKm
```

Some define a link (often to Wikipedia):

```scala
scala> castleBravo
res7: org.pingel.axle.quanta.Quantity = 15 MT

scala> castleBravo.link
res8: Option[String] = Some(http://en.wikipedia.org/wiki/Castle_Bravo)

scala> mustangGT
res10: org.pingel.axle.quanta.Quantity = 420 hp

scala> mustangGT.link
res11: Option[String] = Some(http://en.wikipedia.org/wiki/Ford_Mustang)
```

Unit Conversion
===============

Scalars are implicitly constructed from Strings.
They are converted to Quantities by using the "in" method:

```scala
scala> "10" in gram
res1: org.pingel.axle.quanta.Quantity = 10 g

scala> "3" in lightyear
res12: org.pingel.axle.quanta.Quantity = 3 ly
```

Quantities can be converted into other units of measurement.
This is possible as long as 1) the Quantities are in the same
Quantum, and 2) there is a path in the Quantum between the
two UnitsOfMeasurement.

Never be stumped by a classic Microsoft interview question again!

```scala
scala> ("10" in gram) in kilogram
res14: org.pingel.axle.quanta.Quantity = 0.0100 Kg

scala> earth in sun
res17: org.pingel.axle.quanta.Quantity = 0.000002999941920 ☉

scala> hooverDam in lightBulb
res23: org.pingel.axle.quanta.Quantity = 41600000.00 ?

scala> toAndromeda in parsec
res2: org.pingel.axle.quanta.Quantity = 798200.000 pc
```

A Quantum defines a directed graph, where the UnitsOfMeasurement
are the vertices, and the Conversions (there are two Conversions
for each Quantity -- one forward and one backward) define the
directed edges.

See the [Graph Tutorial](https://github.com/adampingel/pingel.org/blob/master/axle/doc/TutorialGraph.md)
for more on how graphs work.

Math
====

Addition and subtraction are defined on Quantity by converting the
right Quantity to the unit of the left.

```scala
scala> ("1" in kilogram) + ("10" in gram)
res3: org.pingel.axle.quanta.Quantity = 1.0100 Kg

scala> ("7" in mile) - ("123" in foot)
res4: org.pingel.axle.quanta.Quantity = 6.9754 m

scala> (sun - earth) in sun
res5: org.pingel.axle.quanta.Quantity = 0.998997000058080 ☉
```

An example of a case that is not yet fully worked out is calculating
the time required for a volume to be consumed at a flow rate.

```scala
scala> greatLakes / niagaraFalls
res0: org.pingel.axle.quanta.Quantity = 12.36150 KmKmKm/mmm/s

scala> (greatLakes / niagaraFalls) in year
java.lang.Exception: incompatible quanta: QuantumMultiplication and Time$
```

Future
======

The various Quanta also define "derivations".
For instance, Power is Energy / Time.
This isn't used for anything just yet, but it should be soon.

There are obvious paths to take from this point, such as:

1. Less run-time exception throwing.  More static type-safety.
1. Define Monoid for Quantity addition, multiplication
1. Sorting out difference between Quantity and Conversion
1. Fix the "(greatLakes / niagaraFalls) in year" example.

Check back in the future.
