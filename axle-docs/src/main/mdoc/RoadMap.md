---
layout: page
title: Road Map
permalink: /road_map/
---

See [Release Notes](/release_notes/) for the record of previously released features.

## 0.5.0 (August 2020)

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
* Fix `Order[Card]`
* `Deck.riffleShuffle`
* `GuessRiffle` game
* `axle.algebra.etc` via `axle.algebra.EnrichedRinged`
* `bernoulliDistribution`
* `axle.stats.expectation(CPT)`
* `axle.IO` consolidates IO to `cats.effect` (eg `[F[_]: ContextShift: Sync]`)
* Create `axle-awt`, `axle-xml`, and `axle-jogl` (leaving `axle.scene.{Shape,Color}` in `axle-core`)

* `axle.probability`
* Create `axle-parallel`?
* `svgJungDirectedGraphVisualization` move to a `axle-jung-xml` jar?
* `federalist.txt`?
* Move `SamplePredicates` to wheel
* Move more stuff out of `axle.math`
* `axle-ast-python`
* `cats.effect` for `axle.ast.python2`

* Release jars
* Publish site using [sbt-site](https://www.scala-sbt.org/sbt-site/publishing.html) and sbt-s3

## 0.5.1

* MonotypeBayesanNetwork.unit (see the two nulls)
* MonotypeBayesanNetwork.map
* MonotypeBayesanNetwork.flatMap
* Reconcile combine1 and combine2 (maybe add to typeclass or trait)
* ScalaCheck `Monad[ProbabilityModel]` (needs missing tailRecM mehod)
* Monad tests for Alarm-Burglary-Earthquake as MonotypeBayesanNetwork

## 0.5.2

* Test: start with `ABE.jointProbabilityTable` (monotype `tuple5[Boolean]`)
  * factor out each variable until
  * the Alarm-Burglary-Earthquake 5-node network is reached
  * Basically the inverse of factor multiplication
  * `bn.factorFor(B) * bn.factorFor(E)` should be defined? (It errors)
  * `MonotypeBayesanNetwork.filter` collapase into a single BNN

* Review `InteractionGraph`, `EliminationGraph`, `JoinTree` and the functions they power

* Consider a "case" to be a `Map` vs a `Vector`
* Consider usefulness of `Factor` in terms of `Region`

## 0.5.3

* Replace `axle.game.moveFromRandomState.mapToProb`
* Wrap `axle.IO.getLine` in `F[_]`
* Wrap `axle.IO.prefixedDisplay` in `F[_]`

## 0.5.4

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

## 0.5.5+

* MonotypeBayesanNetwork.filter -- could be viewed as "belief updating" (vs "conditioning")
  * If it took a ProbabilityModel itself
  * Is there a way of seeing this as flatMap, though?
* `observe` could return a lower-entropy probability model
  * Perhaps in exchange for a given amount of energy
  * Or ask for a 0-entropy model and be told how expensive that was

* Measure Theory

* Laws for `Region` ("Sigma Algebra"? [video](https://www.youtube.com/watch?v=21a85f1YS5Q))
* Laws for `Factor`
* `OrderedRegion` for the `Order` used in `RegionLTE` and `RegionGTE`?
* More diversity of `Region` (vs just `RegionEq`) for probability axiom (Kolm. & Bayes) tests
* Clean up expressions like `RegionIf[TWOROLLS](_._1 == '⚃)`

* Axiom for observe: val a = observe(gen) => ProbabilityOf(RegionEq(a)) > 0
* Axiom? pm.filter(X=x).P(X=x) == 1
* Fix third Kolmogorov law
* Optimize `KolmogorovProbabilityAxioms.combination`

* rename `ConditionalProbabilityTable`?
* Get rid of `implicit val prob = ProbabilityModel[ConditionalProbabilityTable]`
* "marginalize out" as "sumOut" in `ProbabilityModel` typeclass?

## After that

* Configure makeSite to preview: previewFixedPort := Some(9999)
* Copy css using makeSite (not update-docs.sh)
* Fix markdown lint warnings
* Figure out better way to reference images in docs

* Fix `axle.algebra.GeoMetricSpaceSpec`
* Fix `LogisticRegression` and move `LogisticRegression.md` back

* Create a simple graph implementation so that `axle-core` can avoid including `axle-jung`
* Create `axle-png` to help avoid “headless” exception or Xvfb requirement during tests
* Fix "NSWindow drag regions should only be invalidated on the Main Thread! This will throw an exception in the future." warning during axle-jogl test
* Use sbt-ci-release (rm sbt-release)
* Friend of Spire

* Redo Logic using Abstract Algebra

* axioms of partial differentiation

* Tests for `axle.ast`
* Tics should take an argument
* Factor tics and tics-{joda,algebra,spire} into separate libs?
* Get rid of implicit arg passing to KMeans in `ClusterIrises.md` (and KMeansSpecification)
* Demo Mandelbrot with Rational

* `axle.nlp.Corpus` should support `Aggregatable`
* Fix `GeneticAlgorithmSpec`
* Featurizing functions should return HLists or other typelevel sequences in order to avoid being told # features
* Replace Finite with Shapeless's version
* Delete Finite conversions for jung (replace with NaturalTransformation?)
* Replace with Cats: FoldLeft, Bijection, FunctionPair, Endofunctor
* Define laws for Scanner, Aggregator, Zipper, Indexed, Talliable, Finite?
* Sort out MapFrom, FromStream, FromSet
* Test `axle.algebra.tuple2Field`
* Kind projector instead of `type F[T] = ConditionalProbabilityTable[T, Rational]` and `CPTR[T]` ?
* Kind projector instead of type lambdas
* `similarity` syntax for `SimilaritySpace` (see `axle.bio.*`)
* Kind projector for projections of jung graphs for Finite
* Functors for jung should use projections (study cats disjunction, scala Either)
* kittens or magnolia
  * pattern match in FirstOrderPredicateLogic
  * subtyping for Suit and Rank
* Machinist?
* replace some refs to {CPT0,TallyDist0} with ProbabilityModel
* Update Spire (to match cats version)
* Most MapRedicible witnesses are inefficient (eg calling toVector, toSeq, etc)
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
* Gerrymandering sensitivity
* Game theory axioms (Nash?)
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
