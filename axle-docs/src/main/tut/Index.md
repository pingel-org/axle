---
layout: page
title: Axle
permalink: /
---

Axle is an open-source Scala-embedded domain specific language for scientific computing.

Core
====

```
"org.axle-lang" %% "axle-core" % "releaseVersion"
```

## [Package Object](chapter/PackageObject.md)
Extensions to core Scala data types.
Indexed Power Set, Indexed Cross Product, Combinations, Permutations, and UTF aliases

## [Algebra](chapter/Algebra.md)
Typeclasses Functor, Indexed, Finite, LengthSpace

## [Game Theory](chapter/GameTheory.md)
Framework for the `axle-games` jar.

## [Logic](chapter/Logic.md)
First-Order Predicate Logic

## [Quanta](chapter/Quanta.md)
Units (second, mile, gram, etc) for various quanta (Speed, Distance, Mass, etc) and conversions between them

## [Unitted Trigonometry](chapter/UnittedTrigonometry.md)

## [Pythagorean Means](chapter/PythagoreanMeans.md)
Arithmetic, Harmonic, Geometric, and Generalized means

## [Statistics](chapter/Statistics.md)
Random Variables, Probability, Distributions, Standard Deviation
### [Two Dice](chapter/TwoDice.md)
simulation vs flat mapping distribution monads

## [Information Theory](chapter/InformationTheory.md)
### [Entropy of a Coin](chapter/CoinEntropy.md)

Algorithms
==========

```
"org.axle-lang" %% "axle-algorithms" % "releaseVersion"
```

## [Machine Learning](chapter/MachineLearning.md)

### [Linear Regression](chapter/LinearRegression.md)
### [K-Means Clustering](chapter/KMeansClustering.md)
### [Naive Bayes Clustering](chapter/NaiveBayesClassifier.md)
### [Genetic Algorithms](chapter/GeneticAlgorithms.md)

## [Probabilistic Graphical Models](chapter/ProbabilisticGraphicalModels.md) (PGM)

### [Bayesian Networks](chapter/BayesianNetworks.md)

## [Natural Langage Processing (NLP)](chapter/NaturalLanguageProcessing.md)

### [Language Modules](chapter/LanguageModules.md)
including Stemming and Stop Words
### [Edit Distance](chapter/EditDistance.md)
Levenshtein
### [Vector Space Model](chapter/VectorSpaceModel.md)
including TF-IDF

## [Linguistics](chapter/Linguistics.md)

### [Angluin Learner](chapter/AngluinLearner.md)
### [Gold Paradigm](chapter/GoldParadigm.md)

## [Bioinformatics](chapter/Bioinformatics.md)
DNA Sequence alignment algorithms.

### [Smith Waterman](chapter/SmithWaterman.md)
### [Needleman Wunsch](chapter/NeedlemanWunsch.md)

## Data sets from `axle.data`

### Astronomy
### Evolution
### Federalist Papers
### Irises

Visualizations
==============

```
"org.axle-lang" %% "axle-visualize" % "releaseVersion"
```

## [Visualize](chapter/Visualize.md)

### [Plots](chapter/Plots.md)
### [Bar Charts](chapter/BarCharts.md)
### [Grouped Bar Charts](chapter/GroupedBarCharts.md)

Games
=====

```
"org.axle-lang" %% "axle-games" % "releaseVersion"
```

### [Tic Tac Toe](chapter/TicTacToe.md)
### [Poker](chapter/Poker.md)

Witnesses for 3rd party libraries
=================================

The "Spokes"

JBLAS
=====

```
"org.axle-lang" %% "axle-jblas" % "releaseVersion"
```

## [Matrix](chapter/Matrix.md)
LinearAlgebra and other witnesses for [JBLAS](http://jblas.org/) which itself is a wrapper for
[LAPACK](http://www.netlib.org/lapack/)
Includes Principal Component Analysis (PCA).

JODA
====

```
"org.axle-lang" %% "axle-joda" % "releaseVersion"
```

Witnesses for the [Joda](http://www.joda.org/joda-time/) time library.

JUNG
====

```
"org.axle-lang" %% "axle-jung" % "releaseVersion"
```
## [Graph](chapter/Graph.md)
Directed and undirected graph witnesses for the [JUNG](http://jung.sourceforge.net/) library.

Spark
=====

```
"org.axle-lang" %% "axle-spark" % "releaseVersion"
```

Witnesses for [Spark](https://spark.apache.org/).

More Resources
==============

* [Installation](chapter/Installation.md) notes
* [Source code](https://github.com/axlelang/axle) on github
* Current build status on Travis: <a href="http://travis-ci.org/axlelang/axle"><img src="https://secure.travis-ci.org/axlelang/axle.png" alt="Build Status"/></a>
* <a href="http://codecov.io/github/axlelang/axle?branch=master"><img src="http://codecov.io/github/axlelang/axle/coverage.svg?branch=master"/></a>
* [Scaladoc](/scaladoc)
* [axle-user](https://groups.google.com/forum/#!forum/axle-user) google group
* Join the chat on the gitter channel: <a href="https://gitter.im/axlelang/axle?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge"><img src="https://badges.gitter.im/Join%20Chat.svg"/></a> https://gitter.im/axlelang/axle?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
* [@axledsl](https://twitter.com/axledsl) twitter handle
* [AxleLang YouTube Channel](http://www.youtube.com/user/axlelang)
* Other related [Videos](/videos/)
* Waffle project management: <a href="http://waffle.io/axlelang/axle"><img src="https://badge.waffle.io/axlelang/axle.png?label=ready&title=Ready" alt="Stories in Ready"/></a>
* [Project](/project/) background
* See the [Road Map](/road_map/) for more information on the release schedule and priorities
* [Author](/author/) background

