---
layout: page
title: Road Map
permalink: /road_map/
---

See [Release Notes](/release_notes/) for the record of previously released features.

## 0.3.0 (mid April 2017)
* Scala org to Typelevel
* Type-level matrix dimension using `-Yliteral-types` and `singleton-ops` in `LinearAlgebra` typeclass
* Make the `Int` abstract in KMeans{,Visualization}, LinearAlgebra, etc
* Fix malformed distribution in ConditionalProbabilityTable0 and TallyDistribution0

## 0.3.1 (late April 2017)
* Reactive Streams (FS2?) for animating visualizations
* Remove jung dependency from axle-visualize

## 0.3.2 (May 2017)
* ScatterPlot `play` to awt
* Formatted labels/tooltips for BarChart, etc
* KMeansVisualization / ScatterPlot similarity (at least DataPoints)
* Log scale
* Fix axle.algebra.GeoMetricSpaceSpec
* Fix JodaTime Tics handling of timezones (see TZ requirement in axle.joda.TicsSpec)
* Fix multi-color cube rendering

## 0.3.3 (June 2017)
* Rm throws from axle.stats.TallyDistribution
* Rm throws from axle.pgm.BayesianNetwork
* Rm throws from axle.quanta.UnitConverterGraph
* Rm throws from axle.jung
* Code coverage to ?%

## 0.4.0 (July 2017)
* Solve the Spark ClassTag issue (see Frameless?)
* Fix sbt-release plugin
* Publish Scala 2.12 artifacts (depends on spark-core availability)

## 0.4.x (Summer 2017 - Winter 2018)
* Fix logistic regression
* LSA
* LDA
* GLM
* Neural Networks
* Support Vector Machines
* Gradient Boosted Trees
* Decision Trees
* Random Forest
* A* Search
* Conditional Random Fields (CRF)
* Hidden Markov Models
* Max bet for Poker
* WebGL

## 0.5.x (2018)
* P() backed by BayesianNetwork (and Interaction graph, Elimination graph, Jointree)
* SVG Animation
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
* do-calculus (Causality)
* Performance benchmarking
* netlib-java Matrix
* GPU/CUDA support
* Algebird/Scalding for distributed matrices, HyperLogLog, etc
