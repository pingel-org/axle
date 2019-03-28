---
layout: page
title: Road Map
permalink: /road_map/
---

See [Release Notes](/release_notes/) for the record of previously released features.

## 0.5.0 (April 2019)

* Move to Scala 2.12
* Changes in `axle.game` to provide `Generator` where needed, and return a `ConditionalProbabilityTable0`
* Redo `axle.stats`
  * `ProbabilityModel` typeclass (refactored from `Distribution`)
  * Implicitly conjurable `cats.Monad` from a `ProbabilityModel`, which supports for comprehensions via cats syntax support
  * `Variable` instead of `RandomVariable`
  * remove `Bayes`
* Replace `axle.agebra.Zero` with `spire.algebra.AdditiveMonoid.zero`
* Remove `axle-spark` (Spark "spoke") for now
* Move `axle.ml.distance` to `axle.algebra.distance`
* `axle.dummy` for a handful of scanLeft calls
* Remove Spark impacts on typeclasses in `axle.algebra`. Eg: Spark's `ClassTag` requirement `map` created the difficulty:
  * Functor: removed and replaced with `cats.Functor`
  * Scanner, Aggregator, Zipper, Indexed, Talliable, Finite: Refactored as Kind-1 typeclasses
* Vertex and Edge projections for jung graphs
* Fix axle.joda.TicsSpec handling of timezones
* Update Monix, Kittens, Cats
* ScaleExp works with negative exponent
* ScalaCheck tests for
  * Group and Module of UnittedQuantity
  * MetricSpace axle.algebra.GeoMetricSpace
* `axle.ml.GeneticAlgorithm` rewritten in terms of [kittens](https://github.com/milessabin/kittens)
* `Show`, `Order`, `Eq` witnesses
  * Eq.fromUniversalEquals where applicable
  * SAM inference elsewhere
* Remove `axle.string` and `axle.show`.
  * Replace uses with `.show` from `cats.implicits` or show string interpolation
* Remove extraneous `cutoff` argument for `PCA`
* Replace Tut with MDoc
* Lawful ScalaCheck tests for
  * `Module`s in `axle.algebra`
  * `SimilaritySpace`s for `SmithWaterman` & `NeedlemanWunsch`

* `similarity` syntax for SimilaritySpace (see axle.bio.*)

* Implement {TD,CPT}.{poe, ce}
* Fix Statistics.md
* Fix TwoDice.md
* ProbabilityModel.conditionExpression should enforce that `predicate` tests subset of `A` that does not appear in `B` of `screen` function

* KolmogorovProbabilityAxioms.combination
* "Combination" generalizes to event sets (where antecedent is rephrased as "A intersect B == emptyset")
  but the default distribution of antecedents would usually be uninteresting (evaluate to false)

* KolmogorovProbabilityAxioms for Monty Hall (for some fixed P(switch))
* Stop specifying ConditionalProbabilityTable0 in axle.game package object methods

* KolmogorovProbabilityAxioms for Alarm-Burglary-Earthquake model
* ProbabilityModel[BayesianNetwork] (using Interaction graph, Elimination graph, Jointree)

* Kind projector instead of `type F[T] = ConditionalProbabilityTable0[T, Rational]` and `CPTR[T]` ?
* Kind projector instead of type lambdas

* CaseIs replaced by T => Boolean as the Expression type?
* Prove and generalize CaseIs(heads) is equal to "not tails"
* P / Case expression DSL
* What to do with TD1 and CPT2? Fix "cpt" in InformationTheorySpec
* ProbabilityModel.probabilityOfNot ProbabilityModel.conditionNot

* Rename TallyDistribution0 to Tally (Or is this really just a Map?)
* Rename and ConditionalProbabilityTable0 to ProbabilityTable0
* Heterogenous Model types

* Move KolmogorovProbabilityAxioms to `axle.stats.laws`
* ScalaCheck Monad[ProbabilityModel] (needs missing tailRecM mehod)
* Move NaiveBayesClassifier.md back
* Fix NaiveBayesSpec (and .md)
* Finish NaiveBayesClassifier
* Fix GeneticAlgorithms.md (as much as possible, then move out of the way)
* Move GeneticAlgorthms.md back
* Fix GeneticAlgorithmSpec

* Fix markdown lint warnings
* LogisticMap back to 4k x 4k
* configure makeSite to preview: previewFixedPort := Some(9999)
* copy css using makeSite (not update-docs.sh)
* publish site using [sbt-site](https://www.scala-sbt.org/sbt-site/publishing.html) and sbt-s3
* figure out better way to reference images
* Publish site

## 0.6.x (Spring 2019)

* Qubit, Hadamard, CNot, etc (quantum "is constant" circuit)
* Cats effect/io, FS2, or similar for all `png`, `html`, data fetches, and all `fext scala | xargs egrep -e 'scala.io|java.io' | grep -v 'should be'`
* Fix logistic regression and move LogisticRegression.md back
* Tests for axle.ast
* Demo Mandelbrot with Rational
* Friend of Spire
* Get rid of implicit arg passing to KMeans in ClusterIrises.md (and KMeansSpecification)
* Kind projector for projections of jung graphs for Finite
* Functors for jung should use projections (study cats disjunction, scala Either)
* kittens 1.0.0-RC3 or magnolia
  * pattern match in FirstOrderPredicateLogic
  * subtyping for Suit and Rank

## 0.7.x (Summer 2019)

* Define laws for Scanner, Aggregator, Zipper, Indexed, Talliable, Finite?
* Machinist?
* Replace Finite with Shapeless's version
* Delete Finite conversions for jung (replace with NaturalTransformation?)
* replace some refs to {CPT0,TallyDist0} with ProbabilityModel
* Tics should take an argument
* Factor tics and tics-{joda,algebra,spire} into separate libs

## 0.8.x (Fall 2020)

* Update Spire (to match cats version)
* Replace with Cats: FoldLeft, Bijection, FunctionPair, Endofunctor
* Sort out MapFrom, FromStream, FromSet
* Most MapRedicible witnesses are inefficient (eg calling toVector, toSeq, etc)
* remove unnecessary implicit Field, R{,i}ng, {Additive, Multiplicative}Monoid once spire/cats play well

* game theory axioms
* axle.game: Observable[T]
* move state dist stream
* redo original monty hall spec
* Max bet for Poker
* Game.strategyFor should return a M[underscore] type upon which the ProbabilityModel[M, Rational] can act
* Type-level matrix dimension using `-Yliteral-types` and `singleton-ops` in `LinearAlgebra` typeclass
* Make the `Int` abstract in KMeans{,Visualization}, LinearAlgebra, etc
* Featurizing functions should return HLists or other typelevel sequences in order to avoid being told # features
* Bayes Theorem
* Hypothesis testing
* Describing Data
* P-values
* z & t scores
* Correlation
* Regression
* Accuracy, Precision
* Bias, Variance
* Cohen's Kappa
* Normalizer axioms

## 1.0.x (Winter 2020)

* Redo Logic using Abstract Algebra
* Review remaining usage of: `asInstanceOf`, `ClassTag`, and `Manifest`
* Fix "unreachable" default pattern match cases

## 1.1.x (2020)

* Fix `axle.algebra.GeoMetricSpaceSpec`
* Honor graph vis params in awt graph visualizations
* `axle.web.Table` and `HtmlFrom[Table[T]]`
* Clean up GA doc
* Log scale
* SVG[Matrix]
* `BarChart` Variable width bars
* Horizontal barchart
* `KMeansVisualization` / `ScatterPlot` similarity (at least DataPoints)
* SVG[H] for BarChart hover (wrap with \<g\> to do getBBox)
* Background box for `ScatterPlot` hover text?
* Fix multi-color cube rendering
* Bloom filter surface
* Fix sbt-release plugin (use sbt-sonatype?)
* … as Stream.from(Int)
* Factor similarity between SVG and Draw?

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
* Connection between dynamic programming and semiring
* Recursive grid search

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

* Bettings odds
* Rainbow Tables
* Blockchain
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
* Unchecked constraint in PlotDataView

## Visualization

* Box Plot
* Candlestick Chart
