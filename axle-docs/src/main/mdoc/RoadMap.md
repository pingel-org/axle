---
layout: page
title: Road Map
permalink: /road_map/
---

See [Release Notes](/release_notes/) for the record of previously released features.

## 0.5.0 (May 2019)

* Move to Scala 2.12
* Changes in `axle.game` to provide `Generator` where needed, and return a `ConditionalProbabilityTable0`
* Redo `axle.stats`
  * `ProbabilityModel` typeclass (refactored from `Distribution`) including syntactic support
  * Implicitly conjurable `cats.Monad` from a `ProbabilityModel`, which supports for comprehensions via cats syntax support
  * `Variable` instead of `RandomVariable`
  * remove `Bayes`
* `axle.quantumcircuit` package for modelling computing with quantum circuits
* Replace `axle.agebra.Zero` with `spire.algebra.AdditiveMonoid.zero`
* Remove `axle-spark` (Spark "spoke") for now
* Move `axle.ml.distance` to `axle.algebra.distance`
* `axle.dummy` for a handful of scanLeft calls
* Remove Spark impacts on typeclasses in `axle.algebra`. Eg: Spark's `ClassTag` requirement `map` created the difficulty:
  * `Functor`: removed and replaced with `cats.Functor`
  * `Scanner`, `Aggregator`, `Zipper`, `Indexed`, `Talliable`, `Finite`: Refactored as Kind-1 typeclasses
* Vertex and Edge projections for jung graphs
* Fix `axle.joda.TicsSpec` handling of timezones
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
* Fix Order[Card]
* `Deck.riffleShuffle`
* `GuessRiffle` game
* `ProbabilityModel` `sum`, `product`, and `mapValues`

* Fix GeneticAlgorithmSpec, GeneticAlgorithms.md
* Fix NaiveBayesClassifier, NaiveBayesSpec, + .md

* PM.filter's predicate should also be a Region
* get rid of `implicit val prob = ProbabilityModel[ConditionalProbabilityTable]`
* PM.unit should become PM.construct (again)
* scrutinize remaining `CPTR`
* Is PM.mapValues really needed?
* `ProbabilityModel.construct` shouldn't take a Variable
* Fix `axle.stats.standardDeviation`
* Fix `axle.stats.entropy`
* Fix `axle.game.moveFromRandomState`
* Fix `CPT.probabilityOf` (it's incomplete)
* Laws for `Region` (rename as a "Sigma Algebra"? See https://www.youtube.com/watch?v=21a85f1YS5Q)
* `Show[Region]` TODOs
* `Show[Factor]` should include `Show[A => Boolean]`
* Axiom for observe: val a = observe(gen) => ProbabilityOf(a) > 0
* Fix third Kolmogorov law
* Review groupBy uses -- they use university equality.  Replace with Eq
* Axiom? pm.filter(X=x).P(X=x) == 1
* Fix stuff like the "a" in monadForProbabilityModel Monad.pure

* "marginalize out" as "sumOut" in `ProbabilityModel` typeclass?
* Iterative game playing algorithm is intractible, but shares intent with sequential monte carlo
* Think about Information Theory's "Omega" vis-a-vis Sequential Monte Carlo
* alias `uniform` as `Bernoulli`

* Eliminate entropy consumption of `rng` side-effect (eg `applyMove(Riffle())`)
  * "Chance" should be its own player
  * Each N bits consumed during `Riffle()` is its own move
  * Chance moves consume `UnittedQuantity[Information, N]`

* QBit2.factor

* KolmogorovProbabilityAxioms for Alarm-Burglary-Earthquake model
  * Requires ProbabilityModel[BayesianNetwork] (using Interaction graph, Elimination graph, Jointree)

* LogisticMap md back to 4k x 4k
* Release and publish site
* GuessRiffle.md
  * Walk through game
  * plot distribution of sum(entropy) for both strategies
  * plot entropy by turn # for each strategy
  * plot simulated score distribution for each strategy
* QuantumCircuit.md

## 0.5.1 (June 2019)

* GuessRiffleSpec: use `moveFromRandomState`
* `Monad[ProbabilityModel]` -- factor `flatMap` in terms of `product`
* QBit CCNot
* Property test reversibility (& own inverse)
* Typeclass for "negate" (etc), Binary, CBit
* Typeclass for unindex
* SimpsonsParadox.md
* "You split, I choose" as game
* Gerrymandering sensitivity

## 0.6.0 (July 2019)

* Cats effect/io, FS2, or similar for all `png`, `html`, data fetches, and all `fext scala | xargs egrep -e 'scala.io|java.io' | grep -v 'should be'`
* … (aka "etc") as Stream.from(Int)
* Fix logistic regression and move LogisticRegression.md back
* Is `ConditionalProbabilityTable.variable` necessary?
* Are `ProbabilityModel`'s `sum` and `product` the right names? Consider also `minus`, `divide`, `zero`, `one`, `empty`
* Improve `axle.stats.rationalProbabilityDist` as probabilities become smaller
* Test `axle.algebra.tuple2Field`
* Factor `axle.game.moveFromRandomState` in terms of a random walk on a graph.
  * Compare to Brownian motion, Random walk, Ito process, ...
  * Provide some axoms
    * no outgoing with path in from non-zero mass monotonically increases
    * no incoming with path out monotonically decreases
  * possibly provide a version for acyclic graphs

## 0.7.x (Summer 2019)

* Featurizing functions should return HLists or other typelevel sequences in order to avoid being told # features
* P / Case expression DSL (PExpr, PMultiply, ...)
* CaseIs replaced by T => Boolean as the Expression type?
* Prove and generalize forall E : P(E) + P(not E) = 1 (somewhat redundant with 'combination', but this tests that 'not' on expressions works properly)
* ProbabilityModel.conditionExpression should enforce that `predicate` tests subset of `A` that does not appear in `B` of `screen` function
* Optimize `conditionExpression` implementations
* Optimize `KolmogorovProbabilityAxioms.combination`
* ScalaCheck Monad[ProbabilityModel] (needs missing tailRecM mehod)
* Move KolmogorovProbabilityAxioms to `axle.stats.laws`
* Kind projector instead of `type F[T] = ConditionalProbabilityTable[T, Rational]` and `CPTR[T]` ?
* Kind projector instead of type lambdas
* `similarity` syntax for `SimilaritySpace` (see `axle.bio.*`)

* Tests for `axle.ast`
* Demo Mandelbrot with Rational
* Friend of Spire
* Get rid of implicit arg passing to KMeans in ClusterIrises.md (and KMeansSpecification)
* Fix sbt-release plugin (use sbt-sonatype?)
* Site
  * Fix markdown lint warnings
  * Configure makeSite to preview: previewFixedPort := Some(9999)
  * Copy css using makeSite (not update-docs.sh)
  * Publish site using [sbt-site](https://www.scala-sbt.org/sbt-site/publishing.html) and sbt-s3
  * Figure out better way to reference images

## 0.8.x (Fall 2019)

* Define laws for Scanner, Aggregator, Zipper, Indexed, Talliable, Finite?
* Kind projector for projections of jung graphs for Finite
* Functors for jung should use projections (study cats disjunction, scala Either)
* kittens 1.0.0-RC3 or magnolia
  * pattern match in FirstOrderPredicateLogic
  * subtyping for Suit and Rank
* Machinist?
* Replace Finite with Shapeless's version
* Delete Finite conversions for jung (replace with NaturalTransformation?)
* replace some refs to {CPT0,TallyDist0} with ProbabilityModel
* Tics should take an argument
* Factor tics and tics-{joda,algebra,spire} into separate libs
* Update Spire (to match cats version)
* Replace with Cats: FoldLeft, Bijection, FunctionPair, Endofunctor
* Sort out MapFrom, FromStream, FromSet
* Most MapRedicible witnesses are inefficient (eg calling toVector, toSeq, etc)
* remove unnecessary implicit Field, R{,i}ng, {Additive, Multiplicative}Monoid once spire/cats play well
* Fix `axle.algebra.GeoMetricSpaceSpec`

## 0.9.x (Winter 2020)

## 0.8.x (Winter 2020)

* game theory axioms
* axle.game: Observable[T]
* move state dist stream
* Redo original monty hall spec
* Max bet for Poker
* syntax for `Game` typeclass

## 0.10.x (Spring 2020)

* Type-level matrix dimension using `-Yliteral-types` and `singleton-ops` in `LinearAlgebra` typeclass

* Make the `Int` abstract in KMeans{,Visualization}, LinearAlgebra, etc

## 1.0.x (Late 2020)

* Redo Logic using Abstract Algebra
* Review remaining usage of: `asInstanceOf`, `ClassTag`, and `Manifest`
* Fix "unreachable" default pattern match cases

# Backlog

## Algorithm / Concept breadth

* LSA
* LDA
* GLM
* MCMC
* Metropolis Hastings
* Sequential Monte Carlo (SMC)
* Hamiltonian Monte Carlo (HMC)
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
* Bayes Theorem
* P-values
* z & t scores
* Correlation
* Regression
* Accuracy, Precision
* Bias, Variance
* Cohen's Kappa
* Normalizer axioms
* Fourier transformations
* Abadi Plotkin pathology
* JVP: Jacobian

## Quantum Circuits

* Deutsch-Jozsa algorithm (D.O. for n-bits) (Oracle separation between EQP and P)
* Simon's periodicity problem (oracle separation between BQP and BPP)
* Shor's algorithm
* Grover's algorithm
* Quantum cryptographic key exchange

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
* Honor graph vis params in awt graph visualizations
* `axle.web.Table` and `HtmlFrom[Table[T]]`
* Log scale
* SVG[Matrix]
* `BarChart` Variable width bars
* Horizontal barchart
* `KMeansVisualization` / `ScatterPlot` similarity (at least DataPoints)
* SVG[H] for BarChart hover (wrap with \<g\> to do getBBox)
* Background box for `ScatterPlot` hover text?
* Fix multi-color cube rendering
* Bloom filter surface
* Factor similarity between SVG and Draw?
