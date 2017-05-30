---
layout: page
title: Road Map
permalink: /road_map/
---

See [Release Notes](/release_notes/) for the record of previously released features.

## 0.4.0 (May 31, 2017)
* axle-visualize and most of axle-algorithm merged with axle-core
* create new axle-??? formed from axle-{test, games, languages} and parts of algorithms
* Remove Spark spoke
* Publish Scala 2.12 artifacts

## 0.4.x (Summer 2017)
* Fix implicit numeric widening warnings
* Remove Spark impacts on Functor, etc, and just use Cats versions
* `axle.web.Table` and `HtmlFrom[Table[T]`
* Factor Draw to return something other than Component
* Clean up GA doc
* Fix sbt-release plugin (use sbt-sonatype?)
* … as Stream.from(Int)
* Configurable visualization parameters for {un,}directedGraph
* SVG[H] for BarChart hover (wrap with <g> to do getBBox)
* SVG[Matrix]
* `BarChart` Variable width bars
* Horizontal barchart
* `KMeansVisualization` / `ScatterPlot` similarity (at least DataPoints)
* Background box for `ScatterPlot` hover text?
* Log scale
* Fix JodaTime Tics handling of timezones (see TZ requirement in axle.joda.TicsSpec)
* Fix multi-color cube rendering
* Bloom filter surface

## 0.5.x (Last half of 2017)
* Type-level matrix dimension using `-Yliteral-types` and `singleton-ops` in `LinearAlgebra` typeclass
* Make the `Int` abstract in KMeans{,Visualization}, LinearAlgebra, etc
* Featurizing functions should return HLists or other typelevel sequences in order to avoid being told # features
* Fix logistic regression
* Fix axle.algebra.GeoMetricSpaceSpec

## 0.6.x (First half of 2018)
* game theory axioms
* axle.game: Observable[T]
* move state dist stream
* redo original monty hall spec
* Max bet for Poker

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

## Visualization
* Box Plot
* Candlestick Chart

