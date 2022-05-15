# Appendix

## Road Map

See [Release Notes](#release-notes) for the record of previously released features.

### 0.6.7

* Near-term stuff from [quantum circuit future work](QuantumCircuits.md#future-work)

### 0.7.x Scala 3

See Scala 3 section of [future work](Foundation.md#future-work) for foundation

### 0.8.x Game

See [Future Work](GameTheory.md#future-work) for Axle Game

### 0.9.x Randomness and Uncertainty

Factoring and Bayesian Networks

And [Future Work](ProbabilityModel.md#future-work) for Probability Model

See [Future Work](ProbabilisticGraphicalModels.md#future-work) for PGMs

### 0.10.x Bugs and adoption barriers

See [Future work](Foundation.md#future-work) for Foundation

### 0.11.x Text improvements

* Near-term stuff from [text](Text.md#future-work)

### 0.12.x Visualization

See [future work](Visualization.md#future-work) for axle visualization

### 0.13.x Mathematics

See [future work](Math.md#future-work)

## Build and Release

For contributors

* [Source code](https://github.com/axlelang/axle) on GitHub
* Build status on Github Actions [![Build status](https://github.com/axlelang/axle/workflows/CI%20Release/badge.svg)](https://github.com/axlelang/axle/actions?query=workflow%3A%22CI+Release%22)
* Code coverage [![codecov](http://codecov.io/github/axlelang/axle/coverage.svg?branch=main)](http://codecov.io/github/axlelang/axle?branch=main)

### Publish snapshots

Push commits to repo.

Monitor [progress](https://github.com/axlelang/axle/actions/workflows/ci_release.yml) of github action.

Confirm jars are present at the [sonatype snapshot repo](https://oss.sonatype.org/content/repositories/snapshots/org/axle-lang/)

### Release new version

For example, tag with a version:

```bash
git tag -a v0.1.6 -m "v.0.1.6"
git push origin v0.1.6
```

Monitor [progress](https://github.com/axlelang/axle/actions/workflows/ci_release.yml)

Confirm jars are present at the [sonatype repo](https://oss.sonatype.org/content/repositories/releases/org/axle-lang/)

### Update Site

Run the `site-update.sh` script

Monitor [progress](https://github.com/axlelang/axle/actions/workflows/pages/pages-build-deployment) of action.

Verify by browsing to the [site](https://www.axle-lang.org) or look at
the `gh-pages` [branch](https://github.com/axlelang/axle/tree/gh-pages)

### Verify before update

Just to do the build locally, run

```bash
sbt -J-Xmx8G 'project axle-docs' mdoc
sbt 'project axle-docs' laikaSite
```

To preview the changes, do:

```bash
sbt 'project axle-docs' laikaPreview
```

then browse to [https://localhost:4242](https://localhost:4242)

If it looks good, push with:

```bash
sbt 'project axle-docs' ghpagesCleanSite ghpagesPushSite
```

Monitor and verify as before.

### References for Build and Deploy

* [Laika](https://planet42.github.io/Laika/index.html)
  * [http4s Laika PR](https://github.com/http4s/http4s/pull/5313)
* [sbt-site](https://www.scala-sbt.org/sbt-site/)
* [sbt-ghpages](https://github.com/sbt/sbt-ghpages)
  * Note the instructions to set up a `gh-pages` branch
* [custom domain for github pages](https://docs.github.com/en/pages/configuring-a-custom-domain-for-your-github-pages-site)
  * Note instructions for apex domains
* [sbt-sonatype](https://github.com/xerial/sbt-sonatype)
  * [sonatype](https://oss.sonatype.org/) using credentials in `~/.sbt/1.0/sonatype.sbt`
* [sbt-ci-release](https://github.com/sbt/sbt-ci-release)

## History

### αχλε

Axle models a set of formal subjects that the author has encountered throughout his lifetime.
They take the form of functioning code that allows the reader to experiment with alternative examples.

Although the primary aim of this code is education and clarity, scalability and performance
are secondary goals.

The name "axle" was originally chosen because it sounds like "Haskell".
Given the use of UTF symbols, I tried spelling it using Greek letters to get "αχλε".
It turns out that this is the Etruscan spelling of
[Achilles](http://en.wikipedia.org/wiki/Achilles)

![Achilles](https://upload.wikimedia.org/wikipedia/commons/4/4c/Etruscan_mural_achilles_Troilus.gif)

([image context](http://en.wikipedia.org/wiki/File:Etruscan_mural_achilles_Troilus.gif))

Follow [@axledsl](https://twitter.com/axledsl) on Twitter.

### Project History References

#### Quanta

The first time I had the idea to group units into quanta was at NOCpulse (2000-2002).
NOCpulse was bought by Red Hat, which open-sourced the code.
There is still [evidence](http://spacewalk.redhat.com/documentation/schema-doc/sql_sources/table/rhn_quanta.sql) of that early code online.

In a 2006 class given by [Alan Kay](http://en.wikipedia.org/wiki/Alan_Kay) at UCLA,
I proposed a system for exploring and learning about scale.
The idea occurred to me after reading a news article about a new rocket engine that used the Hoover Dam as a
point of reference.
I wound up implementing another idea, but always meant to come back to it.

#### Machine Learning

Based on many classes at Stanford (in the 90's) and UCLA (in the 00's),
and more recently the Coursera machine learning course in the Fall of 2011.
The inimitable [Artificial Intelligence: A Modern Approach](http://www.amazon.com/Artificial-Intelligence-Modern-Approach-Edition/dp/0136042597/ref=sr_1_1?ie=UTF8) has been a mainstay throughout.

#### Statistics, Information Theory, Bayesian Networks, &amp; Causality

The Information Theory code is based on [Thomas Cover](https://en.wikipedia.org/wiki/Thomas_M._Cover)'s
[Elements of Information Theory](http://www.amazon.com/Elements-Information-Theory-Telecommunications-Processing/dp/0471241954/ref=sr_1_1?ie=UTF8)
and his EE 376A course.

I implemented some Bayesian Networks code in Java around 2006 while
[Adnan Darwiche](http://www.cs.ucla.edu/~darwiche/) class on the subject at UCLA.
The Axle version is based on his book,
[Modeling and Reasoning with Bayesian Networks](http://www.amazon.com/Modeling-Reasoning-Bayesian-Networks-Darwiche/dp/0521884381/ref=sr_1_1?ie=UTF8)

Similarly, I implemented ideas from [Judea Pearl](http://bayes.cs.ucla.edu/jp_home.html)
UCLA course on Causality in Java.
The Axle version is based on his classic text [Causality](http://www.amazon.com/Causality-Reasoning-Inference-Judea-Pearl/dp/052189560X/ref=sr_1_1?ie=UTF8)

#### Game Theory

As a senior CS major at Stanford in 1996, I did some
independent research with
Professor [Daphne Koller](http://ai.stanford.edu/~koller/) and
PhD student [Avi Pfeffer](http://www.gelberpfeffer.net/avi).

This work spanned two quarters.
The first quarter involved using Koller and Pfeffer's
[Gala](http://ai.stanford.edu/~koller/Papers/Koller+Pfeffer:AIJ97.pdf) language
(a Prolog-based DSL for describing games) to study a small
version of Poker and solve for the [Nash equilibria](http://en.wikipedia.org/wiki/Nash_equilibrium).
The second (still unfinished) piece was to extend the
solver to handle non-zero-sum games.

The text I was using at the time was
[Eric Rasmusen](http://www.rasmusen.org/)'s
[Games and Information](http://www.amazon.com/Games-Information-Introduction-Game-Theory/dp/1405136669/ref=sr_1_1?ie=UTF8)

#### Linguistics

Based on notes from [Ed Stabler](http://www.linguistics.ucla.edu/people/stabler/)'s
graduate courses on language evolution and
computational linguistics (Lx 212 08) at UCLA.

## Author

![Adam Pingel](http://www.gravatar.com/avatar/e6a25e87deb4da4ff6aa52d3376d3767.png)

Adam Pingel is an Iowa native who wrote his first lines of code on an Apple ][ in 1983.

He moved to the San Francisco Bay Area in 1992 to study Computer Science at Stanford.
After graduating in 1996, spent several years at Excite.com, helping it scale to become the 3rd largest site on the web at the time.  After Excite he spent two years at NOCpulse -- a startup acquired by Red Hat.

In 2002 he left Silicon Valley to join the UCLA Computer Science department's PhD program.
His major field was programming languages and systems, and his minor fields were AI and Linguistics.
His first year he worked as a TA for the undergraduate Artificial Intelligence class.
The second was spent as a graduate student researcher working on programming tools for artists at
the Hypermedia Lab (a part of the UCLA School of Theater, Film, and Television).
From 2005 - 2009 he mixed graduate studies with consulting.
He received an MS along the way, and ultimately decided to pursue his research interests in the open source community.

In April 2009 he moved to San Francisco and joined the Independent Online Distribution Alliance (IODA) as the Lead Systems Engineer.
During his time there, IODA was acquired by Sony Music and then The Orchard.
In April 2012 he co-founded Eddgy.
In May 2013 he joined VigLink as Staff Software Engineer and later managed a team there.
In September 2015 he became VP of Engineering at Ravel Law.
In June 2017 Ravel Law was acquired by LexisNexis, where he became a Sr. Director.
In late 2019, he became the CTO of Global Platforms.
In early 2022, he joined IBM's [Accelerated Discovery](https://research.ibm.com/teams/accelerated-discovery) team as Technical Lead for Generative Discovery.

For more background, see his accounts on:

* [LinkedIn](http://www.linkedin.com/in/adampingel)
* [StackOverflow](http://stackoverflow.com/users/528536/adam-p)
* [@pingel](https://twitter.com/#!/pingel) on Twitter
* [Google Scholar](http://scholar.google.com/citations?user=xj0ZeKQAAAAJ)

He can be reached at `adam@axle-lang.org`.

## Videos

### "Axle" talk at Scala by the Bay 2015

[![Adam at SBTB 2015](https://img.youtube.com/vi/Y6NiPx-YpdE/hqdefault.jpg)](https://www.youtube.com/watch?v=Y6NiPx-YpdE)

### "Lawful AI" talk at Scale by the Bay 2017

[![Adam at SBTB 2017](https://img.youtube.com/vi/YfMmU4JA27c/hqdefault.jpg)](https://www.youtube.com/watch?v=YfMmU4JA27c)

## Release Notes

See [Road Map](Appendix.md#road-map) for the plan of upcoming releases and features.

### 0.6.x

#### 0.6.1-6 (April-May 2022)

* CI/CD and Site enhancements
  * Automated releases via `sbt-ci-release`
  * Move away from PRs
  * Adopt Laika for site and PDF generation

#### 0.6.0 cats.effect for axle.game (December 31, 2020)

* Wrap `axle.IO.getLine` in `F[_]`
* Remove from `Game`: method `probabilityDist`, `sampler`, and type params `V` and `PM[_, _]`
* Move `strategyFor` from `Game` to `strategies` argument in `axle.game` package methods
* Define `Indexed.slyce` for non-1-step Ranges
* Improve `axle.lx.{Gold, Angluin}` coverage
* `axle.laws.generator` includes generators for GeoCoordinates, UnittedQuantities, and Units
* Simpler `hardCodedStrategy` and `aiMover` signatures
* Replace `randomMove` with `ConditionalProbabilityTable.uniform`

### 0.5.x

#### 0.5.4 Sampler Axioms + package reorg (September 28, 2020)

* Sampler Axioms
  1. ProbabilityOf(RegionEq(sample(gen))) > 0?
  2. Sampled distribution converges to model's
* Pre-compute `ConditionalProbabilityTable.bars` for `Sampler` witness

* Move everything from `axle._` into sub-packages (`algebra`, `math`, `logic`)
* Organize `axle.algebra._` package object
* `axle.laws.generator`

* `rationalProbabilityDist` is now implicitly available

#### 0.5.3 (September 13, 2020)

* Split `ProbabilityModel` into three new typeclasses
  -- `Bayes`, `Kolmogorov`, `Sampler` --
  as well as `cats.Monad`.
  The three axle typeclasses include syntax.

* Rename `ConditionalProbabilityTable.values` to `domain`

* Bugs fixed
  * Bayes axiom should avoid P(A) == P(B) == 0
  * `UnittedQuantity` `LengthSpace` unit mismatch
  * `BarChart` was missing `Order[C]`

* Expanded documentation

#### 0.5.2 (September 7, 2020)

* Move to Scala 2.12 and 2.13
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
* Remove `axle-jogl` due to instability of underlying dependencies

### 0.4.x

#### 0.4.1 (June 4, 2017)

* Fix all warnings, and turn on fatal warnings
* DrawPanel typeclass
* Configurable visualization parameters for {un,}directedGraph and BayesianNetwork
* Make Monix "provided"

#### 0.4.0 (May 30, 2017)

* axle-core gets axle-visualize and most of axle-algorithm
* new axle-wheel formed from axle-{test, games, languages} and parts of axle-algorithms

### 0.3.x

#### 0.3.6 (May 29, 2017)

* Replace Akka with Monix for animating visualizations
* `ScatterPlot` `play` to awt

#### 0.3.5 (May 23, 2017)

* Move math methods from `axle.algebra._` package object to `axle.math._`

#### 0.3.4 (May 22, 2017)

* Move mathy methods from `axle._` package object to new `axle.math._` package object
* Sieve of Eratosthenes
* Remove some `Eq` and `Order` witnesses from `axle._` as they are now available in `cats._`
* Revert Tut to version 0.4.8

#### 0.3.3 (May 7, 2017)

* `BarChart.hoverof` -- center text in bar
* `BarChart{,Grouped}.linkOf`

#### 0.3.2 (May 6, 2017)

* Remove `axle.jblas.{additiveCMonoidDoubleMatrix, multiplicativeMonoidDoubleMatrix, module, ring}
* `axle.math.exponentiateByRecursiveSquaring`
* Rename `fibonacci*` methods
* `PixelatedColoredArea` should take a function that is given a rectangle (not just a point)
* Logistic Map vis using `PixelatedColoredArea` (documentation)

#### 0.3.1 (May 1, 2017)

* `BarChart*.hoverOf`
* `BarChart*` label angle is Option. None indicates no labels below bars.
* `axle.xml` package in axle-visualize

#### 0.3.0 (April 12, 2017)

* Scala org to Typelevel
* Fix malformed distribution in ConditionalProbabilityTable and TallyDistribution0
* Depend on Spire 0.14.1 (fix mistaken dependency on snapshot release in 0.2.8)

### 0.2.x

#### 0.2.8 (March 28, 2016)

* Fix SVG rendering of negative values in BarChart
* Make more arguments to vis components functions (colorOf, labelOf, diameterOf)
* Depend on Spire 0.13.1-SNAPSHOT (which depends on Typelevel Algebra)

#### 0.2.7 (January 2016)

* Use cats-kernel's Eq and Order in favor of Spire's (with Shims to continue to work with Spire)
* Convert tests to use scalatest (to match Cats and Spire)

#### 0.2.6 (November 2016)

* Depends on cats-core (initially just for Show typeclass)
* Strategy: `(G, MS) => Distribution[M, Rational]`
* LinearAlgebra.from{Column,Row}MajorArray
* Implementation of Monty Hall using axle.game typeclasses
* Implementaiton of Prisoner's Dilemma using axle.game typeclasses
* Minor Poker fixes

#### 0.2.5 (October 2016)

* Typeclasses for axle.game
* Increase test coverage to 78%

#### 0.2.4 (September 5, 2016)

* Redo all and extend documentation using Tut
* Convert Build.scala to build.sbt
* LinearAlgebra doc fixes / clarification
* Make some axle.nlp.Corpus methods more consistent
* Avoid using wget in axle.data._
* float*Module witnesses in axle._

#### 0.2.3 (July 30, 2016)

* ScatterPlot
* Logistic Map and Mandelbrot
* PixelatedColoredArea

#### 0.2.2 (October 10, 2015)

* Pythagorean means

#### 0.2.0 (August 12, 2015)

* reorganize to minimize dependencies from axle-core, with witnesses in the axle-X jars (axle.X package) for library X
* LinearAlgebra typeclass
* Functor, Aggregatable typeclasses
* Show, Draw, Play typeclasses
* MAP@k, harmonicMean
* axle-spark
* Apache 2.0 license

### 0.1.x

#### 0.1.13 through 0.1.17 (October 12, 2014)

* Distribution as a Monad
* Spire 'Module' for axle.quanta

#### 0.1-M12 (June 26, 2014)

* Upgrade to Scala 2.11.1
* Field context bound for classes in axle.stats and pgm
* axle.quanta conversions as Rational

#### 0.1-M11 (February 26, 2014)

* REPL
* 3d visualizations using OpenGL (via jogl)
* More prevalent use of Spire typeclasses and number types

#### 0.1-M10 (May 14, 2013)

* bug fixes in cards and poker
* api changes and bug fixes to visualizations required by hammer
* upgrade to akka 2.2-M3 and spire 0.4.0

#### 0.1-M9 (April 7, 2013)

* DNA sequence alignment algorithms in `axle.bio`
* `axle.logic`
* multi-project build, rename axle to axle-core, and split out axle-visualize

#### 0.1-M8 (March 11, 2013)

* Akka for streaming data updates to Plot and Chart
* Tartarus English stemmer
* Create `axle.nlp` package and move much of `axle.lx` there
* Move Bayesian Networks code to `axle.pgm`
* `axle.actor` for Akka-related code

#### 0.1-M7 (February 19, 2013)

* Use `spire.math.Number` in `axle.quanta`
* Use `spire.algebra.MetricSpace` for `axle.lx.*VectorSpace` and `axle.ml.distance.*`

#### 0.1-M6 (February 13, 2013)

* Initial version of `axle.algebra`
* No mutable state (except for permutations, combinations, and mutable buffer enrichment)
* `axle.quanta` conversion graph edges as functions
* Redoing `JblasMatrixFactory` as `JblasMatrixModule` (preparing for "cake" pattern")

#### 0.1-M5 (January 1, 2013)

* Bar Chart
* Minimax
* Texas Hold Em Poker

#### 0.1-M4 (December 16, 2013)

* Clean up `axle.graph` by scrapping attempt at family polymorphism
* Generalize InfoPlottable to QuantaPlottable

#### 0.1-M3 (December 11, 2012)

* Immutable graphs

#### 0.1.M2 (October 24, 2012)

* Genetic Algorithms
* Bug: x and y axis outside of plot area
* Naive Bayes
* `show()` in `axle.visualize`
* PCA
* Immutable matrices
* Optimize Plot of `axle.quanta`

#### 0.1.M1 (July 15, 2012)

* Jblas-backed Matrix
* Jung-backed Graph
* Quanta (units of measurement)
* Linear Regression
* K-means
