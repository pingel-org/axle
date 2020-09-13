---
layout: default
title: Road Map
permalink: /road_map/
---

See [Release Notes](/release_notes/) for the record of previously released features.

## 0.5.4 A bit more theory (Fall 2020)

* Sampler
  * `Sampler[ConditionalProbabilityTable]` cache `bars`
  * Axioms
    1. ProbabilityOf(RegionEq(sample(gen))) > 0?
    2. In the limit, sampled distribution converges to model's

* `SigmaAlgebra` for the CPT
  * Clean up expressions like `RegionIf[TWOROLLS](_._1 == '⚃)`
  * Laws for `Region` ("Sigma Algebra"? [video](https://www.youtube.com/watch?v=21a85f1YS5Q))
  * `OrderedRegion` for the `Order` used in `RegionLTE` and `RegionGTE`?

* Fix third Kolmogorov law
* Optimize `KolmogorovProbabilityAxioms.combination`

* Axiom? `pm.filter(X=x).P(X=x) === 1`
* rename `ConditionalProbabilityTable`?

* Functor for CPT?
* `{CPT,TD}.tailRecM` then ScalaCheck `Monad[CPT]`

## 0.5.5 Adoption blockers (Late 2020)

* Chicklet borders / colors

* move ast view xml (how is it able to refer to `xml.Node`?)
  * ast.view.AstNodeFormatter (xml.Utility.escape)
  * ast.view.AstNodeFormatterXhtmlLines
  * ast.view.AstNodeFormatterXhtml

* Improve Gold and Angluin coverage
* `axle-core/src/main/scala/axle/lx/*.txt`

* Create a simple graph implementation so that `axle-core` can avoid including `axle-jung`

* Create `axle-png` to avoid Xvfb requirement during tests

* Fix occasional MetricSpace failure

## 0.6.x Factoring and Bayesian Networks (2021)

* Test: start with `ABE.jointProbabilityTable` (monotype `tuple5[Boolean]`)
  * factor out each variable until
  * the Alarm-Burglary-Earthquake 5-node network is reached
  * Basically the inverse of factor multiplication
  * `bn.factorFor(B) * bn.factorFor(E)` should be defined? (It errors)
  * `MonotypeBayesanNetwork.filter` collapase into a single BNN

* Laws for `Factor`

* Review `InteractionGraph`, `EliminationGraph`, `JoinTree` and the functions they power

* Consider a "case" to be a `Map` vs a `Vector`
* Consider usefulness of `Factor` in terms of `Region`

* `MonotypeBayesanNetwork`.{`pure`, `map`, `flatMap`, `tailRecR`}
* Reconcile `MBN` `combine1` & `combine2`
* Monad tests for MonotypeBayesanNetwork[Alarm-Burglary-Earthquake]

* `Bayes[MonotypeBayesanNetwork]` -- could be viewed as "belief updating" (vs "conditioning")
  * If it took a ProbabilityModel itself

## 0.7.x Compositional Game Theory (2021)

* Replace `axle.game.moveFromRandomState.mapToProb`
* Wrap `axle.IO.getLine` in `F[_]`
* Wrap `axle.IO.prefixedDisplay` in `F[_]`

* Eliminate entropy consumption of `rng` side-effect (eg `applyMove(Riffle())`)
  * "Chance" should be its own player
  * Each N bits consumed during `Riffle()` is its own move
  * Chance moves consume `UnittedQuantity[Information, N]`
* GuessRiffleSpec: use `moveFromRandomState`
* GuessRiffle.md
  * Walk through game
  * Plot distribution of sum(entropy) for both strategies
  * Plot entropy by turn # for each strategy
  * Plot simulated score distribution for each strategy

* `perceive` could return a lower-entropy probability model
  * Perhaps in exchange for a given amount of energy
  * Or ask for a 0-entropy model and be told how expensive that was

## After that

* `svgJungDirectedGraphVisualization` move to a `axle-jung-xml` jar?
  * Will require externalizing the layout to its own.... typeclass?
  * Layout of bayesian network is quite bad -- check ABE SVG

* Experiment with pandoc

* Configure makeSite to preview: previewFixedPort := Some(9999)
* Copy css using makeSite (not update-docs.sh)
* Fix markdown lint warnings
* Figure out better way to reference images in docs

* Site
  * meta tag with keywords: axle, scala, dsl, data, analysis, science, open-source, adam pingel
  * Remove jekyll from publishing pipeline
    * [sbt-site](https://www.scala-sbt.org/sbt-site/publishing.html)
    * sbt-s3

* Move more stuff out of `axle.math`
* `axle-ast-python`
* `cats.effect` for `axle.ast.python2`

* Fix `axle.algebra.GeoMetricSpaceSpec`
* Fix `LogisticRegression` and move `LogisticRegression.md` back

* Measure Theory
* Redo Logic using Abstract Algebra

* Use sbt-ci-release (rm sbt-release)
* Friend of Spire

* Tests for `axle.ast`
* Tics should take an argument
* Factor tics and tics-{joda,algebra,spire} into separate libs?
* Get rid of implicit arg passing to KMeans in `ClusterIrises.md` (and KMeansSpecification)
* Demo Mandelbrot with Rational

* Fix `GeneticAlgorithmSpec`
* Featurizing functions should return HLists or other typelevel sequences in order to avoid being told # features
* Replace Finite with Shapeless's version
* Delete Finite conversions for jung (replace with NaturalTransformation?)
* Replace with Cats: FoldLeft, Bijection, FunctionPair, Endofunctor
* Define laws for Scanner, Aggregator, Zipper, Indexed, Talliable, Finite?
* Sort out MapFrom, FromStream, FromSet
* Test `axle.algebra.tuple2Field`
* `similarity` syntax for `SimilaritySpace` (see `axle.bio.*`)
* Projections of jung graphs for Finite
* kittens or magnolia
  * pattern match in FirstOrderPredicateLogic
  * subtyping for Suit and Rank
* Machinist?
* replace some refs to {CPT0,TallyDist0} with ProbabilityModel
* Type-level matrix dimension using `-Yliteral-types` and `singleton-ops` in `LinearAlgebra` typeclass
* Make the `Int` abstract in `KMeans{,Visualization}`, `LinearAlgebra`, etc

Future backlog ideas:

## Game Theory and Examples

* Factor `axle.game.moveFromRandomState` in terms of a random walk on a graph.
  * See "TODO scale mass down"
  * Compare to Brownian motion, Random walk, Ito process, ...
  * Provide some axoms
    * no outgoing with path in from non-zero mass monotonically increases
    * no incoming with path out monotonically decreases
  * possibly provide a version for acyclic graphs
* Iterative game playing algorithm is intractible, but shares intent with sequential monte carlo
* Think about Information Theory's "Omega" vis-a-vis Sequential Monte Carlo
* Improve `axle.stats.rationalProbabilityDist` as probabilities become smaller
* SimpsonsParadox.md
* "You split, I choose" as game
* Axioms of partial differentiation
* Gerrymandering sensitivity
* Game theory axioms (Nash, etc)
* `axle.game`: `Observable[T]`
* move state dist stream
* Redo original monty hall spec
* Max bet for Poker
* syntax for `Game` typeclass

## Quantum Circuits

* QuantumCircuit.md
* QBit2.factor
* Fix and enable DeutschOracleSpec
* QBit CCNot
* Property test reversibility (& own inverse)
* Typeclass for "negate" (etc), Binary, CBit
* Typeclass for unindex
* Deutsch-Jozsa algorithm (D.O. for n-bits) (Oracle separation between EQP and P)
* Simon's periodicity problem (oracle separation between BQP and BPP)
* Shor's algorithm
* Grover's algorithm
* Quantum cryptographic key exchange

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

## Compute Engines

* Bring back Spark spoke -- Solve the Spark ClassTag issue (see Frameless?)
* Performance benchmarking
* netlib-java Matrix
* GPU/CUDA support
* Algebird/Scalding for distributed matrices, HyperLogLog, etc
* Most MapRedicible witnesses are inefficient (eg calling toVector, toSeq, etc)

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

* remove unnecessary implicit Field, R{,i}ng, {Additive, Multiplicative}Monoid once spire/cats play well
* Fix "unreachable" default pattern match cases
* Review remaining usage of: `asInstanceOf`, `ClassTag`, and `Manifest`
* Review groupBy uses -- they use university equality.  Replace with Eq
* axle.algorithms coverage > 80%
* axle.core coverage > 80%
* Rm throws from axle.quanta.UnitConverterGraph
* Rm throws from axle.jung
* Rm throws from axle.pgm.BayesianNetwork
* Rm throws from axle.stats.TallyDistribution
* Unchecked constraint in PlotDataView

## Visualization

* WebGL
* SVG Animation
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
* Re-enable `axle-jogl`
  * May require jogamop 2.4, which is not yet released
  * Or possibly use [jogamp archive](https://jogamp.org/deployment/archive/rc/v2.4.0-rc-20200307/jar/)
  * See processing's approach in [this commit](https://github.com/processing/processing4/pull/85/commits/17a20bea37e7bcfa5589dbcb2f4a58c4174f7fe0)
