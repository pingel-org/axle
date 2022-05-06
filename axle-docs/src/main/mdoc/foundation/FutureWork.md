# Future Work

## Scala 3

* Scala 3
* convert to scalameta munit
* correct "Package Objects" doc

## Bugs and adoption barriers

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

* Eigenvectors
* Σ ⊣ Δ ⊣ Π means "sums are left adjoint to diagonals, which are left adjoint to products."

## Compute Engines

* Bring back Spark spoke -- Solve the Spark ClassTag issue (see Frameless?)
* Performance benchmarking
* netlib-java Matrix
* GPU/CUDA support
* Algebird/Scalding for distributed matrices, HyperLogLog, etc
* Most MapRedicible witnesses are inefficient (eg calling toVector, toSeq, etc)

## Hygiene

* Get rid of implicit arg passing to KMeans in `ClusterIrises.md` (and KMeansSpecification)
* Factor tics and tics-{joda,algebra,spire} into separate libs?
* remove unnecessary implicit Field, R{,i}ng, {Additive, Multiplicative}Monoid once spire/cats play well
* Fix "unreachable" default pattern match cases
* Review remaining usage of: `asInstanceOf`, `ClassTag`, and `Manifest`
* Review `groupBy` uses -- they use university equality.  Replace with `Eq`
* axle.algorithms coverage > 80%
* axle.core coverage > 80%
* Rm throws from axle.jung
* Rm throws from axle.pgm.BayesianNetwork

## Site

### Near term

* Copy some test cases to QuantumCircuits.md

* How to make chapters more prominent in pdf?
  * See [this example](https://planet42.github.io/Laika/0.18/03-preparing-content/03-theme-settings.html)

* Cats, Scala, etc are at end -- how to get them in correct order?

* Docs
  * General
    * A few words in README for each section
    * Expand acronyms and include wikipedia links in "Future Work" sections
    * Make dependencies clear in each section
  * Introduction
    * Write "Objectives"
    * Smaller images for Gallery
  * Foundation
    * Architecture
    * Functional Programming
    * Scala
    * Cats: Show, EQ, IO, Algebra, Spire
    * rename "Spokes"
    * rename "Resources"
  * Units
    * Quanta: fix Distance, Energy, Time links
  * Math
    * Intro section bullets not nesting
  * Random, Uncertain
    * Bayesian network rendering is missing tables
  * Text
    * Say more about Python Grammar

### Later

* `laikaIncludeAPI := true` in `build.sbt`
* look at more of [these options](https://planet42.github.io/Laika/0.18/03-preparing-content/03-theme-settings.html)
* Meta tag with keywords: axle, scala, dsl, data, analysis, science, open-source, adam pingel
* Timestamp / version to site footer
* GitHub "Releases" in sidebar should show "latest"
* update google analytics version
* test animation with monix 3.4.0

* make axle.g8 more axle-flavored (use cats.IO App as parent for HelloWorld)

* stop hard-coding PDF_VERSION n build.sbt
* ghpagesCleanSite leaving stale files?
* Friend of Spire
* README: data sets from `axle.data` (Astronomy, Evolution, Federalist Papers, Irises)
* what to do about empty right sidebars? convert bullets into sections? disable somehow?
* merge mdoc and site directories?
* site build via github action?
