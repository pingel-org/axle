# Foundation

Data structures and functions

## Functional

To be written

## Scala

To be written...

## Cats

To be written

Axle makes use of several Typelevel libraries including

* Cats
* Cats Effect
* Spire
* Monix
* ...

## Architecture

Axle generally strives to follow the patterns established
by the [Typelevel](http://typelevel.org/) projects.

With few exceptions, the functions are side-effect free.

The typeclass patterns are drawn from two traditions:

1. [Typeclassopedia](https://wiki.haskell.org/Typeclassopedia)
2. Abstract Algebra

The algorithms are increasingly defined only in terms of these typeclasses.
Concrete runtime implementations will require witnesses that map non-Axle data structures
onto the typeclass methods and laws.
Laws are organized into a separate `axle-laws` jar for use in tests by code that builds
upon these typeclasses.
Many such witnesses are provided by Axle for native Scala collections.

Witnesses are also defined for other common jars from the Java and Scala ecosystems.
Read more about ["these third party libraries"](#support-for-third-party-libraries).

### Remaining Design Issues

Please get in touch if you'd like to discuss these or other questions.

## Package Objects

This page describes functions in `axle.logic` and `axle.math` package objects.

Imports

```scala mdoc:silent:reset
import cats.implicits._

import spire.algebra._

import axle.logic._
import axle.math._

implicit val rngInt: Rng[Int] = spire.implicits.IntAlgebra
implicit val ringLong: Ring[Long] = spire.implicits.LongAlgebra
implicit val boolBoolean: Bool[Boolean] = spire.implicits.BooleanStructure
```

Logic aggregators `∃` and `∀`:

```scala mdoc
∃(List(1, 2, 3)) { i: Int => i % 2 == 0 }

∀(List(1, 2, 3)) { i: Int => i % 2 == 0 }
```

Sum and multiply aggregators `Σ` and `Π`.
Note that `Σ` and `Π` are also available in `spire.optional.unicode._`.

```scala mdoc
Σ((1 to 10) map { _ * 2 })

Π((1L to 10L) map { _ * 2 })
```

Doubles, triples, and cross-products

```scala mdoc
doubles(Set(1, 2, 3))

triples(Set(1, 2, 3))

⨯(List(1, 2, 3))(List(4, 5, 6)).toList
```

Powerset

```scala mdoc
℘(0 until 4)

val ps = ℘(Vector("a", "b", "c"))

ps.size

ps(7)
```

### Permutations

```scala mdoc
permutations(0 until 4)(2).toList
```

### Combinations

```scala mdoc
combinations(0 until 4)(2).toList
```

### Indexed Cross Product

```scala mdoc
val icp = IndexedCrossProduct(Vector(
  Vector("a", "b", "c"),
  Vector("d", "e"),
  Vector("f", "g", "h")))

icp.size

icp(4)
```

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

## Linear Algebra

A `LinearAlgebra` typeclass.

The `axle-jblas` spoke provides witnesses for JBLAS matrices.

The default jblas matrix `toString` isn't very readable,
so this tutorial wraps most results in the Axle `string` function,
invoking the `cats.Show` witness for those matrices.

### Imports and implicits

Import JBLAS and Axle's `LinearAlgebra` witness for it.

```scala mdoc:silent:reset
import cats.implicits._

import spire.algebra.Field
import spire.algebra.NRoot

import axle._
import axle.jblas._
import axle.syntax.linearalgebra.matrixOps

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

implicit val laJblasDouble = axle.jblas.linearAlgebraDoubleMatrix[Double]
import laJblasDouble._
```

### Creating Matrices

```scala mdoc
ones(2, 3).show

ones(1, 4).show

ones(4, 1).show
```

### Creating matrices from arrays

```scala mdoc
fromColumnMajorArray(2, 2, List(1.1, 2.2, 3.3, 4.4).toArray).show

fromColumnMajorArray(2, 2, List(1.1, 2.2, 3.3, 4.4).toArray).t.show

val m = fromColumnMajorArray(4, 5, (1 to 20).map(_.toDouble).toArray)
m.show
```

### Random matrices

```scala mdoc
val r = rand(3, 3)

r.show
```

### Matrices defined by functions

```scala mdoc
matrix(4, 5, (r, c) => r / (c + 1d)).show

matrix(4, 5, 1d,
  (r: Int) => r + 0.5,
  (c: Int) => c + 0.6,
  (r: Int, c: Int, diag: Double, left: Double, right: Double) => diag).show
```

### Metadata

```scala mdoc
val x = fromColumnMajorArray(3, 1, Vector(4.0, 5.1, 6.2).toArray)
x.show

val y = fromColumnMajorArray(3, 1, Vector(7.3, 8.4, 9.5).toArray)
y.show

x.isEmpty

x.isRowVector

x.isColumnVector

x.isSquare

x.isScalar

x.rows

x.columns

x.length
```

### Accessing columns, rows, and elements

```scala mdoc
x.column(0).show

x.row(1).show

x.get(2, 0)

val fiveByFive = fromColumnMajorArray(5, 5, (1 to 25).map(_.toDouble).toArray)

fiveByFive.show

fiveByFive.slice(1 to 3, 2 to 4).show

fiveByFive.slice(0.until(5,2), 0.until(5,2)).show
```

### Negate, Transpose, Power

```scala mdoc
x.negate.show

x.transpose.show

// x.log
// x.log10

x.pow(2d).show
```

### Mins, Maxs, Ranges, and Sorts

```scala mdoc
r.max

r.min

// r.ceil
// r.floor

r.rowMaxs.show

r.rowMins.show

r.columnMaxs.show

r.columnMins.show

rowRange(r).show

columnRange(r).show

r.sortRows.show

r.sortColumns.show

r.sortRows.sortColumns.show
```

### Statistics

```scala mdoc
r.rowMeans.show

r.columnMeans.show

// median(r)

sumsq(r).show

std(r).show

cov(r).show

centerRows(r).show

centerColumns(r).show

zscore(r).show
```

### Principal Component Analysis

```scala mdoc
val (u, s) = pca(r)

u.show

s.show
```

### Horizontal and vertical concatenation

```scala mdoc
(x aside y).show

(x atop y).show
```

### Addition and subtraction

```scala mdoc
val z = ones(2, 3)

z.show
```

Matrix addition

```scala mdoc
import spire.implicits.additiveSemigroupOps

(z + z).show
```

Scalar addition (JBLAS method)

```scala mdoc
z.addScalar(1.1).show
```

Matrix subtraction

```scala mdoc
import spire.implicits.additiveGroupOps

(z - z).show
```

Scalar subtraction (JBLAS method)

```scala mdoc
z.subtractScalar(0.2).show
```

### Multiplication and Division

Scalar multiplication

```scala mdoc
z.multiplyScalar(3d).show
```

Matrix multiplication

```scala mdoc
import spire.implicits.multiplicativeSemigroupOps

(z * z.transpose).show
```

Scalar division (JBLAS method)

```scala mdoc
z.divideScalar(100d).show
```

### Map element values

```scala mdoc
implicit val endo = axle.jblas.endoFunctorDoubleMatrix[Double]
import axle.syntax.endofunctor.endofunctorOps

val half = ones(3, 3).map(_ / 2d)

half.show
```

### Boolean operators

```scala mdoc
(r lt half).show

(r le half).show

(r gt half).show

(r ge half).show

(r eq half).show

(r ne half).show

((r lt half) or (r gt half)).show

((r lt half) and (r gt half)).show

((r lt half) xor (r gt half)).show

((r lt half) not).show
```

### Higher order methods

```scala mdoc
(m.map(_ + 1)).show

(m.map(_ * 10)).show

// m.foldLeft(zeros(4, 1))(_ + _)

(m.foldLeft(ones(4, 1))(_ mulPointwise _)).show

// m.foldTop(zeros(1, 5))(_ + _)

(m.foldTop(ones(1, 5))(_ mulPointwise _)).show
```

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

[Linear Algebra](#linear-algebra) and other witnesses for [JBLAS](http://jblas.org/) which itself is a wrapper for [LAPACK](http://www.netlib.org/lapack/).
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
