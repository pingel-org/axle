# Foundation

## Functional Programming

* Typeclasses
* Curry-Howard Isomorphism
* Theorems for Free
* Referential Transparency
* Equational Reasoning
* Life After Monoids
* Algebird
* Kmett's mapping of abstract algebra to software patterns

## Scala

Scala is the host of Axle for many reasons, principled and not.

It's support for functional programming is chief among them.

Axle makes extensive use of Scala 2's "Context Bounds".

When Axle moves to Scala 3, much of this will use the enw `given` / `using` syntax.

## Typelevel and Cats

Axle makes use of several Typelevel libraries including

* Cats (`Show`, `Eq`)
* Cats Effect (`IO`, `Async`)
* Spire (`Field`, `Ring`)
* Monix
* ...

## Architecture

### Ideal

Laws are organized into a separate `axle-laws` jar for use in tests by code that builds
upon these typeclasses.
Many such witnesses are provided by Axle for native Scala collections.

Witnesses are also defined for other common jars from the Java and Scala ecosystems.
Read more about ["these third party libraries"](#support-for-third-party-libraries).

### Remaining Design Issues

Please get in touch if you'd like to discuss these or other questions.

## Algebra

The [spire](http://github.com/non/spire) project is a dependency of Axle.
`spire.algebra` defines typeclasses for Monoid, Group, Ring, Field, VectorSpace, etc, and
witnesses for many common numeric types as well as those defined in `spire.math`

The `axle.algebra` package defines several categories of typeclasses:

* higher-kinded: Functor, Finite, Indexed, Aggregatable
* mathematical: LinearAlgebra, LengthSpace
* visualization: Tics, Plottable

Axioms are defined in the
[axle.algebra.laws](https://github.com/axlelang/axle/tree/master/axle-core/src/main/scala/axle/algebra/laws) package
as [ScalaCheck](http://scalacheck.org/) properties.

They are organized with [Discipline](https://github.com/typelevel/discipline).

## Logic

### Conjunctive Normal Form Converter

Imports

```scala mdoc:silent:reset
import cats.implicits._
import axle.logic.FirstOrderPredicateLogic._
```

Example CNF conversion

```scala mdoc:silent
import axle.logic.example.SamplePredicates._

val z = Symbol("z")

val s = ∃(z ∈ Z, (A(z) ∧ G(z)) ⇔ (B(z) ∨ H(z)))

val (cnf, skolemMap) = conjunctiveNormalForm(s)
```

```scala mdoc
cnf.show

skolemMap
```

## Support for Third Party Libraries

Witnesses for 3rd party libraries.

### Parallel Collections

```sbt
"org.axle-lang" %% "axle-parallel" % "@RELEASE_VERSION@"
```

For use with Scala [Parallel Collections](https://github.com/scala/scala-parallel-collections) library
(`"org.scala-lang.modules" %% "scala-parallel-collections" % ...`)

### XML

```sbt
"org.axle-lang" %% "axle-xml" % "@RELEASE_VERSION@"
```

For use with Scala [XML](https://github.com/scala/scala-xml) library
(`"org.scala-lang.modules" %% "scala-xml" % ...`)

XML includes `axle.web`, where HTML and SVG visualizations reside.

### JBLAS

```sbt
"org.axle-lang" %% "axle-jblas" % "@RELEASE_VERSION@"
```

[Linear Algebra](LinearAlgebra.md) and other witnesses for [JBLAS](http://jblas.org/) which itself is a wrapper for [LAPACK](http://www.netlib.org/lapack/).
Includes Principal Component Analysis (PCA).

### JODA

```sbt
"org.axle-lang" %% "axle-joda" % "@RELEASE_VERSION@"
```

Witnesses for the [Joda](http://www.joda.org/joda-time/) time library.

### JUNG

```sbt
"org.axle-lang" %% "axle-jung" % "@RELEASE_VERSION@"
```

Directed and Undirected [Graph](GraphTheory.md) witnesses for the [JUNG](http://jung.sourceforge.net/) library.

### AWT

```sbt
"org.axle-lang" %% "axle-awt" % "@RELEASE_VERSION@"
```

Witnesses for [AWT](https://docs.oracle.com/javase/7/docs/api/java/awt/package-summary.html)

## Future Work

### Scala 3

* Scala 3
* convert to scalameta munit
* correct "Package Objects" doc

### Bugs and adoption barriers

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

### Types and Axioms

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

### Compute Engines

* Bring back Spark spoke -- Solve the Spark ClassTag issue (see Frameless?)
* Performance benchmarking
* netlib-java Matrix
* GPU/CUDA support
* Algebird/Scalding for distributed matrices, HyperLogLog, etc
* Most MapRedicible witnesses are inefficient (eg calling toVector, toSeq, etc)

### Hygiene

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

### Site

#### Near term / minor

  * General
    * Expand acronyms and include wikipedia links in "Future Work" sections
    * Make dependencies clear in each section
  * Introduction
    * Smaller images for Gallery
  * Foundation
    * Architecture
  * Math
    * Intro section bullets not nesting
  * Random, Uncertain
    * Bayesian network rendering is missing tables
  * Text
    * Say more about Python Grammar

#### Later

* `laikaIncludeAPI := true` in `build.sbt`
* look at more of [these options](https://planet42.github.io/Laika/0.18/03-preparing-content/03-theme-settings.html)
* Meta tag with keywords: axle, scala, dsl, data, analysis, science, open-source, adam pingel
* update google analytics version
* test animation with monix 3.4.0

* Friend of Spire
* README: data sets from `axle.data` (Astronomy, Evolution, Federalist Papers, Irises)

### Build

* ghpagesCleanSite leaving stale files?
* GitHub "Releases" in sidebar should show "latest"
* keep axle.g8 and axle versions in sync
* site publish (`git push`) via github action?
* make axle.g8 more axle-flavored (use cats.IO App as parent for HelloWorld)
