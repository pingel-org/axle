
Axle
====

Axle is an open-source Scala-embedded domain specific language for scientific computing.

Primary Jars
------------

```
"org.axle-lang" %% "axle-core" % "releaseVersion"
```

* [Package Object](PackageObject.md): Extensions to core Scala data types.  Indexed Power Set, Indexed Cross Product, Combinations, Permutations, and UTF aliases
* Algebra.chapter.link: Typeclasses Functor, Indexed, Finite, LengthSpace
* GameTheory.chapter.link: Framework for the `axle-games` jar.
* Logic.chapter.link: First-Order Predicate Logic
* Quanta.chapter.link: Units (second, mile, gram, etc) for various quanta (Speed, Distance, Mass, etc) and conversions between them
* UnittedTrigonometry.chapter.link
* PythagoreanMeans.chapter.link: Arithmetic, Harmonic, Geometric, and Generalized means
* Statistics.chapter.link: Random Variables, Probability, Distributions, Standard Deviation
  * TwoDice.chapter.link simulation vs flat mapping distribution monads
* InformationTheory.chapter.link
  * CoinEntropy.chapter.link and visualization

```
"org.axle-lang" %% "axle-algorithms" % "releaseVersion"
```

MachineLearning.chapter.link

* LinearRegression.chapter.link
* KMeansClustering.chapter.link
* NaiveBayesClassifier.chapter.link
* GeneticAlgorithms.chapter.link

ProbabilisticGraphicalModels.chapter.link (PGM)

* BayesianNetworks.chapter.link

NaturalLanguageProcessing.chapter.link

* LanguageModules.chapter.link including Stemming and Stop Words
* EditDistance.chapter.link: Levenshtein
* VectorSpaceModel.chapter.link including TF-IDF

Linguistics.chapter.link

* AngluinLearner.chapter.link
* GoldParadigm.chapter.link

Bioinformatics.chapter.link DNA Sequence alignment algorithms.

* SmithWaterman.chapter.link
* NeedlemanWunsch.chapter.link

Data sets from `axle.data`

* Astronomy
* Evolution
* Federalist Papers
* Irises

```
"org.axle-lang" %% "axle-visualize" % "releaseVersion"
```
The Visualize.chapter.link jar provides three basic styles of visualization:

* Plots.chapter.link
* BarCharts.chapter.link
* GroupedBarCharts.chapter.link

```
"org.axle-lang" %% "axle-games" % "releaseVersion"
```
* TicTacToe.chapter.link
* Poker.chapter.link

Witnesses for 3rd party libraries -- the "Spokes"
-------------------------------------------------

```
"org.axle-lang" %% "axle-jblas" % "releaseVersion"
```

* Matrix.chapter.link: LinearAlgebra and other witnesses for <a href="http://jblas.org/">JBLAS</a> which itself is a wrapper for <a href="http://www.netlib.org/lapack/">LAPACK</a>.  Includes Principal Component Analysis (PCA).

```
"org.axle-lang" %% "axle-joda" % "releaseVersion"
```
* Witnesses for the <a href="http://www.joda.org/joda-time/">Joda</a> time library.

```
"org.axle-lang" %% "axle-jung" % "releaseVersion"
```
Graph.chapter.link: Directed and undirected graph witnesses for the <a href="http://jung.sourceforge.net/">JUNG</a> library.

```
"org.axle-lang" %% "axle-spark" % "releaseVersion"
```
* Witnesses for <a href="https://spark.apache.org/">Spark</a>.

More Resources
--------------

* Installation.chapter.link notes
* <a href="https://github.com/axlelang/axle">Source code</a> on github
* Current build status on Travis: <a href="http://travis-ci.org/axlelang/axle"><img src="https://secure.travis-ci.org/axlelang/axle.png" alt="Build Status"/></a>
* <a href="http://codecov.io/github/axlelang/axle?branch=master"><img src="http://codecov.io/github/axlelang/axle/coverage.svg?branch=master"/></a>
* <a href="/scaladoc">Scaladoc</a>
* <a href="axle-user">https://groups.google.com/forum/#!forum/axle-user</a> google group
* Join the chat on the gitter channel: <a href="https://gitter.im/axlelang/axle?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge"><img src="https://badges.gitter.im/Join%20Chat.svg"/></a>
* <a href="https://twitter.com/axledsl">@axledsl</a> twitter handle
* <a href="http://www.youtube.com/user/axlelang">AxleLang YouTube Channel</a>
* Other related Videos.link
* Waffle project management: <a href="http://waffle.io/axlelang/axle"><img src="https://badge.waffle.io/axlelang/axle.png?label=ready&title=Ready" alt="Stories in Ready"/></a>
* Project.link background
* See the RoadMap.link for more information on the release schedule and priorities
* Author.link background

Join the chat at https://gitter.im/axlelang/axle
https://badges.gitter.im/Join%20Chat.svg
https://gitter.im/axlelang/axle?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
