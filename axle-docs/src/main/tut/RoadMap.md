---
layout: page
title: Road Map
permalink: /road_map/
---

## 0.2.9 (April 2017)
* log scale
* Publish Scala 2.12 artifacts (depends on spark-core availability)
* Fix JodaTime Tics handling of timezones (see TZ requirement in axle.joda.TicsSpec)
* Fix sbt-release plugin
* Fix Logistic regression
* Fix axle-test/src/test/scala/axle/algebra/GeoMetricSpaceSpec.scala
* Fix malformed distribution axle.stats.ConditionalProbabilityTable0$$anonfun$observe$2.apply(ConditionalProbabilityTable.scala:68)

## 0.2.10 (May 2017)
* Reactive Streams (FS2?) for animating visualizations
* SVG Animation
* ScatterPlot `play` to awt
* Formatted labels/tooltips for BarChart, etc
* Remove jung dependency from axle-visualize
* Fix multi-color cube rendering
* KMeansVisualization / ScatterPlot similarity (at least DataPoints)

## 0.2.11 (June 2017)
* Max bet for Poker
* Code coverage to ?%

## 0.3.0 (September 2017)
* Dependent types (Shapeless? Refined? ValueOf SIP-23 in typelevel scala 2.11 and 2.12.2) for matrix size
* Solve the Spark ClassTag issue
* Make the `Int` abstract in KMeans{,Visualization}, LinearAlgebra, etc

## 0.3.x (Fall 2017)
* LSA
* LDA
* GLM
* Neural Networks
* Support Vector Machines
* Gradient Boosted Trees
* Decision Trees
* Random Forest
* A* Search

## 0.4.x (2018)
* Shapeless for compound Quanta and Bayesian Networks
* Physics (eg, how Volume relates to Flow)
* Heterogenous Model types
* Redo Logic using Abstract Algebra
* Motivation for Gold Paradigm, Angluin Learner, Nerod Partition
* P() backed by BayesianNetwork (and Interaction graph, Elimination graph, Jointree)
* Conditional Random Fields (CRF)
* Hidden Markov Models
* Stochastic Lambda Calculus
* MCMC
* Metropolis Hastings
* Game Theory: information sets, equilibria
* do-calculus (Causality)
* Performance benchmarking
* netlib-java Matrix
* GPU/CUDA support
* Algebird/Scalding for distributed matrices, HyperLogLog, etc


Previous Milestones
===================

## 0.1.M1 (July 15, 2012)
* Jblas-backed Matrix
* Jung-backed Graph
* Quanta (units of measurement)
* Linear Regression
* K-means

## 0.1.M2 (October 24, 2012)
* Genetic Algorithms
* Bug: x and y axis outside of plot area
* Naive Bayes
* show() in axle.visualize
* PCA
* Immutable matrices
* Optimize Plot of axle.quanta

## 0.1-M3 (December 11, 2012)
* Immutable graphs

## 0.1-M4 (December 16, 2013)
* Clean up axle.graph by scrapping attempt at family polymorphism
* Generalize InfoPlottable to QuantaPlottable

## 0.1-M5 (January 1, 2013)
* Bar Chart
* Minimax
* Texas Hold Em Poker

## 0.1-M6 (February 13, 2013)
* Initial version of <code>axle.algebra</code>
* No mutable state (except for permutations, combinations, and mutable buffer enrichment)
* <code>axle.quanta</code> conversion graph edges as functions
* Redoing JblasMatrixFactory as JblasMatrixModule (preparing for "cake" pattern")

## 0.1-M7 (February 19, 2013)
* Use <code>spire.math.Number</code> in <code>axle.quanta</code>
* Use <code>spire.algebra.MetricSpace</code> for <code>axle.lx.*VectorSpace</code> and <code>axle.ml.distance.*</code>

## 0.1-M8 (March 11, 2013)
* Akka for streaming data updates to Plot and Chart
* Tartarus English stemmer
* Create <code>axle.nlp</code> package and move much of <code>axle.lx</code> there
* Move Bayesian Networks code to <code>axle.pgm</code>
* <code>axle.actor</code> for Akka-related code

## 0.1-M9 (April 7, 2013)
* DNA sequence alignment algorithms in <code>axle.bio</code>
* <code>axle.logic</code>
* multi-project build, rename axle to axle-core, and split out axle-visualize

## 0.1-M10 (May 14, 2013)
* bug fixes in cards and poker
* api changes and bug fixes to visualizations required by hammer
* upgrade to akka 2.2-M3 and spire 0.4.0

## 0.1-M11 (February 26, 2014)
* REPL
* 3d visualizations using OpenGL (via jogl)
* More prevalent use of Spire typeclasses and number types

## 0.1-M12 (June 26, 2014)
* Upgrade to Scala 2.11.1
* Field context bound for classes in axle.stats and pgm
* axle.quanta conversions as Rational

## 0.1.13 through 0.1.17 (October 12, 2014)
* Distribution as a Monad
* Spire 'Module' for axle.quanta

## 0.2.0 (August 12, 2015)
* reorganize to minimize dependencies from axle-core, with witnesses in the axle-X jars (axle.X package) for library X
* LinearAlgebra typeclass
* Functor, Aggregatable typeclasses
* Show, Draw, Play typeclasses
* MAP@k, harmonicMean
* axle-spark
* Apache 2.0 license

## 0.2.2 (October 10, 2015)
* Pythagorean means

## 0.2.3 (July 30, 2016)
* ScatterPlot
* Logistic Map and Mandelbrot
* PixelatedColoredArea

## 0.2.4 (September 5, 2016)
* Redo all and extend documentation using Tut
* Convert Build.scala to build.sbt
* LinearAlgebra doc fixes / clarification
* Make some axle.nlp.Corpus methods more consistent
* Avoid using wget in axle.data._
* float*Module witnesses in axle._

## 0.2.5 (October 2016)
* Typeclasses for axle.game
* Increase test coverage to 78%

## 0.2.6 (November 2016)
* Depends on cats-core (initially just for Show typeclass)
* Strategy: (G, MS) => Distribution[M, Rational]
* LinearAlgebra.from{Column,Row}MajorArray
* Implementation of Monty Hall using axle.game typeclasses
* Implementaiton of Prisoner's Dilemma using axle.game typeclasses
* Minor Poker fixes

## 0.2.7 (January 2016)
* Use cats-kernel's Eq and Order in favor of Spire's (with Shims to continue to work with Spire)
* Convert tests to use scalatest (to match Cats and Spire)

## 0.2.8 (March 2016)
* Fix SVG rendering of negative values in BarChart
* Make more arguments to vis components functions (colorOf, labelOf, diameterOf)
* Depend on Spire 0.14.0 (which depends on Typelevel Algebra)
