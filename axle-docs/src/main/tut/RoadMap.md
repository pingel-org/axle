---
layout: page
title: Road Map
permalink: /road_map/
---

See [Release Notes](/release_notes/) for the record of previously released features.

## 0.4.2 (October 2017)
* `axle.dummy` for a handful of scanLeft calls
* `axle.ml.GeneticAlgorithm`
  * `axle.poly` package for `Mixer` and `Mutator` of `HLists`, suitable for `RightFolder` with a `spire.random.Generator`
* Changes in `axle.game` to provide `Generator` where needed, and return a `ConditionalProbabilityTable0`
* Redo `axle.stats`
  * `ProbabilityModel` typeclass (refactored from `Distribution`)
  * `Variable` instead of `RandomVariable`
  * remove `Bayes`

  * Fix GeneticAlgorithmSpec

  * eliminate rationalProbabilityDist usage by introducing new `Probability` (or some such) typeclass
  * Kolmogorov's axiom's of probability
  * Finish NaiveBayesClassifier
  * What to do with TD1 and CPT2? Fix "cpt" in InformationTheorySpec
  * Fix ProbabilitySpec
  * ProbabilityModel.probabilityOfNot ProbabilityModel.conditionNot
  * Rename TallyDistribution0 and ConditionalProbabilityTable0 to Tally0 and ProbabilityTable0, respectively

  * Check Monad[ProbabilityModel] with discipline (needs axle.stats.ProbabilityModel.monad.tailRecM)
  * Syntax for ProbabilityModel.probabilityOf, observe, etc
  * Avoid these:
     implicit val monad = ProbabilityModel.monad[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]
     val prob = implicitly[ProbabilityModel[({ type λ[T] = ConditionalProbabilityTable0[T, Rational] })#λ, Rational]]
  * Mandelbrot with Rational
  * fix tut and site
  * publish artifact  

## 0.5.0 (December 2017)
* Remove Spark spoke
* Remove Spark impacts on Functor, etc, and just use Cats versions
* Replace type lambdas with kind projector
* Publish Scala 2.12 artifacts
* Update cats to 1.0.0

## 0.5.1 (January 2017)
* game theory axioms
* axle.game: Observable[T]
* move state dist stream
* redo original monty hall spec
* Max bet for Poker
* Game.strategyFor should return a M[_] type upon which the ProbabilityModel[M, Rational] can act
* replace some refs to {CPT0,TallyDist0} with ProbabilityModel

## 0.5.2 (February 2018)
* Heterogenous Model types
* ProbabilityModel[BayesianNetwork] (using Interaction graph, Elimination graph, Jointree)

## 0.5.3 (Spring 2018)
* Bayes Theorem
* Hypothesis testing
* Describing Data
* P-values
* z & t scores
* Correlation
* Regression
* Accuracy, Precision
* Bias, Variance
* Normalizer axioms
* Make sure animation doc has right return value

## 0.5.4 (May 2018)
* P / Case expression DSL
* Redo Logic using Abstract Algebra

## 0.6.0 (Summer 2018)
* Review remaining usage of: `asInstanceOf`, `ClassTag`, and `Manifest`

## 0.6.x (Fall 2018)
* Honor graph vis params in awt graph visualizations
* Fix JodaTime Tics handling of timezones (see TZ requirement in axle.joda.TicsSpec)
* `axle.web.Table` and `HtmlFrom[Table[T]]`
* Clean up GA doc
* Log scale
* SVG[Matrix]
* `BarChart` Variable width bars
* Horizontal barchart
* `KMeansVisualization` / `ScatterPlot` similarity (at least DataPoints)
* SVG[H] for BarChart hover (wrap with <g> to do getBBox)
* Background box for `ScatterPlot` hover text?
* Fix multi-color cube rendering
* Bloom filter surface
* Fix sbt-release plugin (use sbt-sonatype?)
* … as Stream.from(Int)
* Factor similarity between SVG and Draw?

## 0.7.x (2019)
* Type-level matrix dimension using `-Yliteral-types` and `singleton-ops` in `LinearAlgebra` typeclass
* Make the `Int` abstract in KMeans{,Visualization}, LinearAlgebra, etc
* Featurizing functions should return HLists or other typelevel sequences in order to avoid being told # features
* Fix logistic regression
* Fix `axle.algebra.GeoMetricSpaceSpec`

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
* N Queens
* Multi-armed bandit

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
* Nerod Partition
* Motivation for Gold Paradigm, Angluin Learner
* Stochastic Lambda Calculus
* MCMC
* Metropolis Hastings
* Game Theory: information sets, equilibria
* Redo axle.ast.* (rm throws, more typesafe)
* Shapeless for compound Quanta and Bayesian Networks
* Physics (eg, how Volume relates to Flow)
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

