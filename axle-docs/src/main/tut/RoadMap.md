---
layout: page
title: Road Map
permalink: /road_map/
---

See [Release Notes](/release_notes/) for the record of previously released features.

## 0.3.1
* Remove Spark spoke
* Remove Spark impacts on Functor, etc, and just use Cats versions
* Publish Scala 2.12 artifacts

## 0.3.2
* Featurizing functions should return HLists or other typelevel sequences in order to avoid being told # features
* Fix logistic regression
* Fix axle.algebra.GeoMetricSpaceSpec

## 0.4.x
* Type-level matrix dimension using `-Yliteral-types` and `singleton-ops` in `LinearAlgebra` typeclass
* Make the `Int` abstract in KMeans{,Visualization}, LinearAlgebra, etc
* Eigenvector

## 0.5.x
* Reactive Streams (FS2? Monix? Akka?) for animating visualizations
* Remove jung dependency from axle-visualize
* ScatterPlot `play` to awt
* Formatted labels/tooltips for BarChart, etc
* KMeansVisualization / ScatterPlot similarity (at least DataPoints)
* Log scale
* Fix JodaTime Tics handling of timezones (see TZ requirement in axle.joda.TicsSpec)
* Fix multi-color cube rendering

## 0.6.x
* game theory axioms
* axle.game: Observable[T]
* redo original monty hall spec
* move state dist stream
* Max bet for Poker
* Fix sbt-release plugin (use sbt-sonatype?)

## 0.7.x
* axle.algorithms coverage > 80%
* axle.core coverage > 80%
* Rm throws from axle.quanta.UnitConverterGraph
* Rm throws from axle.jung
* Rm throws from axle.pgm.BayesianNetwork
* Rm throws from axle.stats.TallyDistribution

## Algorithm breadth
* LSA
* LDA
* GLM
* Neural Networks
* t-distributed stochastic neighbor embedding (t-SNE)
* Support Vector Machines
* Gradient Boosted Trees
* Decision Trees
* Random Forest
* A* Search
* Conditional Random Fields (CRF)
* Hidden Markov Models

## Platform
* Bring back Spark spoke -- Solve the Spark ClassTag issue (see Frameless?)
* Spark syntax
* WebGL
* SVG Animation
* Performance benchmarking
* netlib-java Matrix
* GPU/CUDA support
* Algebird/Scalding for distributed matrices, HyperLogLog, etc

## Deeper exploration
* P() backed by BayesianNetwork (and Interaction graph, Elimination graph, Jointree)
* Nerod Partition
* Motivation for Gold Paradigm, Angluin Learner
* Stochastic Lambda Calculus
* MCMC
* Metropolis Hastings
* Game Theory: information sets, equilibria
* Redo axle.ast.* (rm throws, more typesafe)
* Shapeless for compound Quanta and Bayesian Networks
* Physics (eg, how Volume relates to Flow)
* Heterogenous Model types
* Redo Logic using Abstract Algebra
* Topoi
* do-calculus (Causality)
