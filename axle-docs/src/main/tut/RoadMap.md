---
layout: page
title: Road Map
permalink: /road_map/
---

See [Release Notes](/release_notes/) for the record of previously released features.

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

