---
layout: default
title: Road Map
permalink: /road_map/
---

See [Release Notes](/release_notes/) for the record of previously released features.

## CICD

* try to use gh-pages with current jekyll
* update publishing.txt
* CNAME
* check that release notes have something sane for 0.6.1 release

* github "Releases" in sidebar should show "latest"
* create and publish code coverage reports
* ProbabilityModel.md:207 (mdoc generated code) method any2stringadd in object Predef is deprecated
* timestamp / version to site footer

## 0.6.3+ Further buildout of axle.game (2021H1)

### Missing functionality

* Remove moveStateStream

* For one game (probably Poker)
  * Record witnessed and unwitnessed history `Seq[(M, S)]` in `State`
  * Display to user in interactiveMove
    * `val mm = evGame.maskMove(game, move, mover, observer)`
    * `evGameIO.displayMoveTo(game, mm, mover, observer)`
  * Then generalize and pull into framework

* factor out `axle-ast-python`

### Motivating Examples

* Generalize `OldMontyHall.chanceOfWinning`

* GuessRiffle.md
  * Walk through game
  * Plot distribution of sum(entropy) for both strategies
  * Plot entropy by turn # for each strategy
  * Plot simulated score distribution for each strategy

* GuessRiffleSpec: use `moveFromRandomState`

* Gerrymandering sensitivity

* "You split, I choose" as game

### Deeper changes

* `aiMover.unmask` prevents `MontyHallSpec` "AI vs. AI game produces moveStateStream" from working
  * will be an issue for all non-perfect information

* Identify all uses of `spire.random.Generator` (and other random value generation)

* See uses of `seed` in `GuessRiffleProperties`

* Eliminate entropy consumption of `rng` side-effect (eg `applyMove(Riffle())`)
  * `Chance` should be its own player
  * Consider whether `PM` should be a part of `Strategy` type (`MS => PM[M, V]`)
    * More abstractly, more many intents and purposes, all we are about is that resolving PM to M consumes entropy
    * In which cases should the `PM` be retained?
  * Each N bits consumed during `Riffle()` is its own move
  * Chance moves consume `UnittedQuantity[Information, N]`

* `perceive` could return a lower-entropy probability model
  * Perhaps in exchange for a given amount of energy
  * Or ask for a 0-entropy model and be told how expensive that was

* Game theory axioms (Nash, etc)

* `axle.game`: `Observable[T]`

### Hygeine

* convert to scalameta munit for dotty

* Replace `axle.game.moveFromRandomState.mapToProb`

* Clean up `axle.game.playWithIntroAndOutcomes`

* The references to `movesMap` in `MoveFromRandomStateSpec.scala` illustrate a need for a cleaner way to create a hard-coded strategy -- which could just be in the form of a couple utility functions from `movesMap` to the data needed by `evGame.{moves,applyMove}` and `rm` strategy

* Generalize `ConditionalProbabilityTable.uniform` into typeclass

* Simplify `GuessRiffleProperties` (especially second property)
* stateStreamMap only used in GuessRiffleProperties -- stop using chain?
* stateStrategyMoveStream only used in GuessRiffleProperties

* `Game.players` should be a part of GameState (or take it as an argument)?  Will wait for pressing use case.

## 0.7.x Factoring and Bayesian Networks

* Reorder Probability mdoc (Creation, Kolmogorov/Region, Sampler, Bayes, Monad)
  * Footnotes (Giry, etc)

* `{CPT,TD}.tailRecM` then ScalaCheck `Monad[CPT,TD]`
* Functor for CPT, TD

* `SigmaAlgebra` for the CPT
  * Clean up expressions like `RegionIf[TWOROLLS](_._1 == '⚃)`
  * Laws for `Region` ("Sigma Algebra"? [video](https://www.youtube.com/watch?v=21a85f1YS5Q))
  * `OrderedRegion` for the `Order` used in `RegionLTE` and `RegionGTE`?

* Measure Theory

* Test: start with `ABE.jointProbabilityTable` (monotype `tuple5[Boolean]`)
  * Factor out each variable until original 5-note network is reached
  * Basically the inverse of factor multiplication
  * `bn.factorFor(B) * bn.factorFor(E)` should be defined? (It errors)
  * `MonotypeBayesanNetwork.filter` collapase into a single BNN

* Rename `ConditionalProbabilityTable`?

* Laws for `Factor`

* Review `InteractionGraph`, `EliminationGraph`, `JoinTree` and the functions they power

* Consider a "case" to be a `Map` vs a `Vector`
* Consider usefulness of `Factor` in terms of `Region`

* `MonotypeBayesanNetwork`.{`pure`, `map`, `flatMap`, `tailRecR`}
* Reconcile `MBN` `combine1` & `combine2`
* Monad tests for MonotypeBayesanNetwork[Alarm-Burglary-Earthquake]

* `Bayes[MonotypeBayesanNetwork]` -- could be viewed as "belief updating" (vs "conditioning")
  * If it took a ProbabilityModel itself

* QuantumCircuit.md
* QBit2.factor
* Fix and enable DeutschOracleSpec
* QBit CCNot

* Review complex analysis

## 0.8.x Bugs and adoption barriers

* Fix `LogisticRegression` and move `LogisticRegression.md` back

* Fix `GeneticAlgorithmSpec`
* Featurizing functions should return HLists or other typelevel sequences in order to avoid being told # features

* Redo Logic using Abstract Algebra

* Simple graph implementation so that `axle-core` can avoid including `axle-jung`

* `svgJungDirectedGraphVisualization` move to a `axle-jung-xml` jar?
  * Will require externalizing the layout to its own.... typeclass?
  * Layout of bayesian network is quite bad -- check ABE SVG

* `axle-png` to avoid Xvfb requirement during tests
* Chicklet borders / colors on site

* move ast view xml (how is it able to refer to `xml.Node`?)
  * ast.view.AstNodeFormatter (xml.Utility.escape)
  * ast.view.AstNodeFormatterXhtmlLines
  * ast.view.AstNodeFormatterXhtml
* Tests for `axle.ast`
* `axle-ast-python`
* `cats.effect` for `axle.ast.python2`

* Factor `axle.algebra.chain` in terms of well-known combinators

## After that

It's likely that a 1.0 release will be made at this time.

Future backlog organized by theme are below.
At this point Axle will have enough building blocks to do the research
that has been its goal since inception.

## Game Theory and Examples

* Factor `axle.game.moveFromRandomState` in terms of a random walk on a graph.
  * See "TODO scale mass down"
  * Compare to Brownian motion, Random walk, Ito process, ...
  * Provide some axioms
    * no outgoing with path in from non-zero mass monotonically increases
    * no incoming with path out monotonically decreases
  * possibly provide a version for acyclic graphs
* Iterative game playing algorithm is intractible, but shares intent with sequential monte carlo
* Think about Information Theory's "Omega" vis-a-vis Sequential Monte Carlo
* Improve `axle.stats.rationalProbabilityDist` as probabilities become smaller
* SimpsonsParadox.md
* Axioms of partial differentiation
  * [Plotkin Partial Differentiation](https://math.ucr.edu/home/baez/mathematical/ACTUCR/Plotkin_Partial_Differentiation.pdf)
* Conal Elliott: Efficient automatic differentiation made easy via category theory
* Max bet for Poker
* syntax for `Game` typeclass

## Quantum Circuits

* Property test reversibility (& own inverse)
* Typeclass for "negate" (etc), Binary, CBit
* Typeclass for unindex
* Deutsch-Jozsa algorithm (D.O. for n-bits) (Oracle separation between EQP and P)
* Simon's periodicity problem (oracle separation between BQP and BPP)
* Shor's algorithm
* Grover's algorithm
* Quantum cryptographic key exchange
* Check out Qiskit

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
* Finish Angluin Learner
* Motivation for Gold Paradigm, Angluin Learner
* Stochastic Lambda Calculus
* Game Theory: information sets, equilibria
* Redo axle.ast.* (rm throws, more typesafe)
* Shapeless for compound Quanta and Bayesian Networks
* Physics (eg, how Volume relates to Flow)
* Topoi
* do-calculus (Causality)
* Eigenvector
* Σ ⊣ Δ ⊣ Π means "sums are left adjoint to diagonals, which are left adjoint to products."

## Hygiene

* Get rid of implicit arg passing to KMeans in `ClusterIrises.md` (and KMeansSpecification)
* Factor tics and tics-{joda,algebra,spire} into separate libs?
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

## Mathematics

* Collatz Conjecture [vis](https://en.wikipedia.org/wiki/Collatz_conjecture#/media/File:Collatz-stopping-time.svg)
* Demo Mandelbrot with Rational
* Scrutinize `axle.math` and move out less reusable functions

## Types and Axioms

* Replace Finite with Shapeless's version (eg Sized[Vector[_], nat.2])
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
* Type-level matrix dimension using `-Yliteral-types` and `singleton-ops` in `LinearAlgebra` typeclass
* Make the `Int` abstract in `KMeans{,Visualization}`, `LinearAlgebra`, etc

## Docs, Site, CI/CD

* Friend of Spire

* Experiment with pandoc

* Use sbt-ci-release (rm sbt-release)

* Configure makeSite to preview: previewFixedPort := Some(9999)
* Copy css using makeSite (not update-docs.sh)
* Fix markdown lint warnings
* Figure out better way to reference images in docs

* Site
  * meta tag with keywords: axle, scala, dsl, data, analysis, science, open-source, adam pingel
  * Remove jekyll from publishing pipeline
    * [sbt-site](https://www.scala-sbt.org/sbt-site/publishing.html)
    * sbt-s3
