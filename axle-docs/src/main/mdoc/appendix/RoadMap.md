# Road Map

See [Release Notes](ReleaseNotes.md) for the record of previously released features.

## CICD

* move much of Road Map to chapter-specific "future work" sections
* move GeoCoordinates.md to "Units"

* Foundation should include Architecture, Cats, Spire, FP, ...
* gallery images should be same width

* look for opportunities to use `mdoc:silent`
* make dependencies clear in each section

* rename "Spokes"
* more exposition for Architecture, FP, Cats
* simplify "Installation"
* copy some test cases to QuantumCircuits.md
* Reorder Probability mdoc (Creation, Kolmogorov/Region, Sampler, Bayes, Monad)
  * Footnotes (Giry, etc)

* document Quantum Circuits
* tweet / post

## 0.6.4

* stop hard-coding PDF_VERSION n build.sbt
* `laikaIncludeAPI := true` in `build.sbt`
* look at more of [these options](https://planet42.github.io/Laika/0.18/03-preparing-content/03-theme-settings.html)
* README: data sets from `axle.data` (Astronomy, Evolution, Federalist Papers, Irises)
* what to do about empty right sidebars? convert bullets into sections? disable somehow?
* update google analytics version
* merge mdoc and site directories?
* site build via github action?

## 0.6.5

* QBit2.factor
* Fix and enable DeutschOracleSpec
* QBit CCNot

## 0.6.6

* factor out `axle-ast-python`
* move ast view xml (how is it able to refer to `xml.Node`?)
  * ast.view.AstNodeFormatter (xml.Utility.escape)
  * ast.view.AstNodeFormatterXhtmlLines
  * ast.view.AstNodeFormatterXhtml
* Tests for `axle.ast`
* `axle-ast-python`
* `cats.effect` for `axle.ast.python2`

## 0.7.x Scala 3

* Scala 3
* convert to scalameta munit
* correct "Package Objects" doc

## 0.8.x Mature Game

See [Future Work](../game_theory/FutureWork.md) for Axle Game

## 0.9.x Factoring and Bayesian Networks

See [Future Work](../random_uncertain/FutureWork.md) for Randomness and Uncertainty

## 0.10.x Bugs and adoption barriers

* Fix `LogisticRegression` and move `LogisticRegression.md` back

* Fix `GeneticAlgorithmSpec`
* Featurizing functions should return HLists or other typelevel sequences in order to avoid being told the number of features

* Redo Logic using Abstract Algebra

* Simple graph implementation so that `axle-core` can avoid including `axle-jung`

* `svgJungDirectedGraphVisualization` move to a `axle-jung-xml` jar?
  * Will require externalizing the layout to its own.... typeclass?
  * Layout of bayesian network is quite bad -- check ABE SVG

* `axle-png` to avoid Xvfb requirement during tests
* Chicklet borders / colors on site

* Factor `axle.algebra.chain` in terms of well-known combinators

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
* Review `groupBy` uses -- they use university equality.  Replace with `Eq`
* axle.algorithms coverage > 80%
* axle.core coverage > 80%
* Rm throws from axle.quanta.UnitConverterGraph
* Rm throws from axle.jung
* Rm throws from axle.pgm.BayesianNetwork
* Rm throws from axle.stats.TallyDistribution
* Unchecked constraint in PlotDataView

## Visualization

See [future work](../visualization/FutureWork.md) for axle visualization

## Mathematics

* Collatz Conjecture [vis](https://en.wikipedia.org/wiki/Collatz_conjecture#/media/File:Collatz-stopping-time.svg)
* Demo Mandelbrot with Rational
* Scrutinize `axle.math` and move out less reusable functions
* Complex Analysis

## Types and Axioms

* Replace `Finite` with Shapeless's version (eg `Sized[Vector[_], nat.2]`)
* Delete `Finite` conversions for jung
* Replace with Cats: `FoldLeft`, `Bijection`, `FunctionPair`, `Endofunctor`
* Define laws for `Scanner`, `Aggregator`, `Zipper`, `Indexed`, `Talliable`, `Finite`?
* Sort out `MapFrom`, `FromStream`, `FromSet`
* Test `axle.algebra.tuple2Field`
* `similarity` syntax for `SimilaritySpace` (see `axle.bio.*`)
* Projections of jung graphs for `Finite`
* kittens or magnolia
  * pattern match in `FirstOrderPredicateLogic`
  * subtyping for `Suit` and `Rank`
* Machinist?
* Type-level matrix dimension using `-Yliteral-types` and `singleton-ops` in `LinearAlgebra` typeclass
* Make the `Int` abstract in `KMeans{,Visualization}`, `LinearAlgebra`, etc

## Site

* Friend of Spire
* Meta tag with keywords: axle, scala, dsl, data, analysis, science, open-source, adam pingel
* Debug problems with laikaIncludePDF
* GitHub "Releases" in sidebar should show "latest"
* ghpagesCleanSite leaving stale files?
* Timestamp / version to site footer
