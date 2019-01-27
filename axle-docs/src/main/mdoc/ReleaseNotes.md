---
layout: page
title: Release notes
permalink: /release_notes/
---

See [Road Map](/road_map/) for the plan of upcoming releases and features.

## 0.4.1 (June 4, 2017)

* Fix all warnings, and turn on fatal warnings
* DrawPanel typeclass
* Configurable visualization parameters for {un,}directedGraph and BayesianNetwork
* Make Monix "provided"

## 0.4.0 (May 30, 2017)

* axle-core gets axle-visualize and most of axle-algorithm
* new axle-wheel formed from axle-{test, games, languages} and parts of axle-algorithms

## 0.3.6 (May 29, 2017)

* Replace Akka with Monix for animating visualizations
* `ScatterPlot` `play` to awt

## 0.3.5 (May 23, 2017)

* Move math methods from `axle.algebra._` package object to `axle.math._`

## 0.3.4 (May 22, 2017)

* Move mathy methods from `axle._` package object to new `axle.math._` package object
* Sieve of Eratosthenes
* Remove some `Eq` and `Order` witnesses from `axle._` as they are now available in `cats._`
* Revert Tut to version 0.4.8

## 0.3.3 (May 7, 2017)

* `BarChart.hoverof` -- center text in bar
* `BarChart{,Grouped}.linkOf`

## 0.3.2 (May 6, 2017)

* Remove `axle.jblas.{additiveCMonoidDoubleMatrix, multiplicativeMonoidDoubleMatrix, module, ring}
* `axle.math.exponentiateByRecursiveSquaring`
* Rename `fibonacci*` methods
* `PixelatedColoredArea` should take a function that is given a rectangle (not just a point)
* Logistic Map vis using `PixelatedColoredArea` (documentation)

## 0.3.1 (May 1, 2017)

* `BarChart*.hoverOf`
* `BarChart*` label angle is Option. None indicates no labels below bars.
* `axle.xml` package in axle-visualize

## 0.3.0 (April 12, 2017)

* Scala org to Typelevel
* Fix malformed distribution in ConditionalProbabilityTable0 and TallyDistribution0
* Depend on Spire 0.14.1 (fix mistaken dependency on snapshot release in 0.2.8)

## 0.2.8 (March 28, 2016)

* Fix SVG rendering of negative values in BarChart
* Make more arguments to vis components functions (colorOf, labelOf, diameterOf)
* Depend on Spire 0.13.1-SNAPSHOT (which depends on Typelevel Algebra)

## 0.2.7 (January 2016)

* Use cats-kernel's Eq and Order in favor of Spire's (with Shims to continue to work with Spire)
* Convert tests to use scalatest (to match Cats and Spire)

## 0.2.6 (November 2016)

* Depends on cats-core (initially just for Show typeclass)
* Strategy: (G, MS) => Distribution[M, Rational]
* LinearAlgebra.from{Column,Row}MajorArray
* Implementation of Monty Hall using axle.game typeclasses
* Implementaiton of Prisoner's Dilemma using axle.game typeclasses
* Minor Poker fixes

## 0.2.5 (October 2016)

* Typeclasses for axle.game
* Increase test coverage to 78%

## 0.2.4 (September 5, 2016)

* Redo all and extend documentation using Tut
* Convert Build.scala to build.sbt
* LinearAlgebra doc fixes / clarification
* Make some axle.nlp.Corpus methods more consistent
* Avoid using wget in axle.data._
* float*Module witnesses in axle._

## 0.2.3 (July 30, 2016)

* ScatterPlot
* Logistic Map and Mandelbrot
* PixelatedColoredArea

## 0.2.2 (October 10, 2015)

* Pythagorean means

## 0.2.0 (August 12, 2015)

* reorganize to minimize dependencies from axle-core, with witnesses in the axle-X jars (axle.X package) for library X
* LinearAlgebra typeclass
* Functor, Aggregatable typeclasses
* Show, Draw, Play typeclasses
* MAP@k, harmonicMean
* axle-spark
* Apache 2.0 license

## 0.1.13 through 0.1.17 (October 12, 2014)

* Distribution as a Monad
* Spire 'Module' for axle.quanta

## 0.1-M12 (June 26, 2014)

* Upgrade to Scala 2.11.1
* Field context bound for classes in axle.stats and pgm
* axle.quanta conversions as Rational

## 0.1-M11 (February 26, 2014)

* REPL
* 3d visualizations using OpenGL (via jogl)
* More prevalent use of Spire typeclasses and number types

## 0.1-M10 (May 14, 2013)

* bug fixes in cards and poker
* api changes and bug fixes to visualizations required by hammer
* upgrade to akka 2.2-M3 and spire 0.4.0

## 0.1-M9 (April 7, 2013)

* DNA sequence alignment algorithms in `axle.bio`
* `axle.logic`
* multi-project build, rename axle to axle-core, and split out axle-visualize

## 0.1-M8 (March 11, 2013)

* Akka for streaming data updates to Plot and Chart
* Tartarus English stemmer
* Create `axle.nlp` package and move much of `axle.lx` there
* Move Bayesian Networks code to `axle.pgm`
* `axle.actor` for Akka-related code

## 0.1-M7 (February 19, 2013)

* Use `spire.math.Number` in `axle.quanta`
* Use `spire.algebra.MetricSpace` for `axle.lx.*VectorSpace` and `axle.ml.distance.*`

## 0.1-M6 (February 13, 2013)

* Initial version of `axle.algebra`
* No mutable state (except for permutations, combinations, and mutable buffer enrichment)
* `axle.quanta` conversion graph edges as functions
* Redoing `JblasMatrixFactory` as `JblasMatrixModule` (preparing for "cake" pattern")

## 0.1-M5 (January 1, 2013)

* Bar Chart
* Minimax
* Texas Hold Em Poker

## 0.1-M4 (December 16, 2013)

* Clean up `axle.graph` by scrapping attempt at family polymorphism
* Generalize InfoPlottable to QuantaPlottable

## 0.1-M3 (December 11, 2012)

* Immutable graphs

## 0.1.M2 (October 24, 2012)

* Genetic Algorithms
* Bug: x and y axis outside of plot area
* Naive Bayes
* `show()` in `axle.visualize`
* PCA
* Immutable matrices
* Optimize Plot of `axle.quanta`

## 0.1.M1 (July 15, 2012)

* Jblas-backed Matrix
* Jung-backed Graph
* Quanta (units of measurement)
* Linear Regression
* K-means
