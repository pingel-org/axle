---
layout: page
title: Road Map
permalink: /road_map/
---

See [Release Notes](/release_notes/) for the record of previously released features.

## 0.3.3 (May 7, 2017)
* Scatterplot hoverOver in doc
* `BarChart` Center hover text
* `BarChart` Bar as link (also alter color to indicate link)
* Case class -> table visualization
* SVG[Matrix]
* `BarChart` Variable width bars
* Horizontal barchart

## 0.4.0 (May 21, 2017)
* Pull out axle-web and axle-awt (algorithm and visualize should go to axle-core)
* Monix for animating visualizations
* Remove jung dependency from axle-visualize
* Remove Spark spoke
* Remove Spark impacts on Functor, etc, and just use Cats versions
* Publish Scala 2.12 artifacts

## 0.4.x (Summer 2017)
* Box Plot
* Candlestick Chart
* `KMeansVisualization` / `ScatterPlot` similarity (at least DataPoints)
* Background box for `ScatterPlot` hover text?
* `ScatterPlot` `play` to awt
* Configurable visualization parameters for {un,}directedGraph
* Log scale
* Fix JodaTime Tics handling of timezones (see TZ requirement in axle.joda.TicsSpec)
* Fix multi-color cube rendering

## 0.5.x (September 2017)
* Type-level matrix dimension using `-Yliteral-types` and `singleton-ops` in `LinearAlgebra` typeclass
* Make the `Int` abstract in KMeans{,Visualization}, LinearAlgebra, etc
* Featurizing functions should return HLists or other typelevel sequences in order to avoid being told # features
* Fix logistic regression
* Fix axle.algebra.GeoMetricSpaceSpec

## 0.6.x (November 2017)
* game theory axioms
* axle.game: Observable[T]
* redo original monty hall spec
* move state dist stream
* Max bet for Poker
* Fix sbt-release plugin (use sbt-sonatype?)

# Backlog

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
* Eigenvector

## Hygiene
* axle.algorithms coverage > 80%
* axle.core coverage > 80%
* Rm throws from axle.quanta.UnitConverterGraph
* Rm throws from axle.jung
* Rm throws from axle.pgm.BayesianNetwork
* Rm throws from axle.stats.TallyDistribution
