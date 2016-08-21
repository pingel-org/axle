---
layout: page
title: Index
in_header: true
permalink: /
---

Axle is an open-source Scala-embedded domain specific language for scientific computing.

Core
====

```
"org.axle-lang" %% "axle-core" % "releaseVersion"
```

## [Package Object](/chapter/axle_package_object/)
Extensions to core Scala data types.
Indexed Power Set, Indexed Cross Product, Combinations, Permutations, and UTF aliases

## [Algebra](/chapter/algebra/)
Typeclasses Functor, Indexed, Finite, LengthSpace

## [Game Theory](/chapter/game_theory/)
Framework for the `axle-games` jar.

## [Logic](/chapter/logic/)
First-Order Predicate Logic

## [Quanta](/chapter/quanta/)
Units (second, mile, gram, etc) for various quanta (Speed, Distance, Mass, etc) and conversions between them

## [Unitted Trigonometry](/chapter/unitted_trigonometry/)

## [Pythagorean Means](/chapter/pythagorean_means/)
Arithmetic, Harmonic, Geometric, and Generalized means

## [Statistics](/chapter/statistics/)
Random Variables, Probability, Distributions, Standard Deviation
### [Two Dice](/chapter/two_dice/)
simulation vs flat mapping distribution monads

## [Information Theory](/chapter/information_theory/)
### [Entropy of a Coin](/chapter/entropy_biased_coin/)

Algorithms
==========

```
"org.axle-lang" %% "axle-algorithms" % "releaseVersion"
```

## [Machine Learning](/chapter/machine_earning/)

### [Linear Regression](/chapter/linear_regression/)
### [K-Means Clustering](/chapter/k_means_clustering/)
### [Naive Bayes Clustering](/chapter/naive_bayes/)
### [Genetic Algorithms](/chapter/genetic_algorithms/)

## [Probabilistic Graphical Models](/chapter/probabilistic_graphical_models/) (PGM)

### [Bayesian Networks](/chapter/bayesian_networks/)

## [Natural Langage Processing (NLP)](/chapter/natural_language_processing/)

### [Language Modules](/chapter/language_modules/)
including Stemming and Stop Words
### [Edit Distance](/chapter/edit_distance/)
Levenshtein
### [Vector Space Model](/chapter/vector_space_model/)
including TF-IDF

## [Linguistics](/chapter/linguistics/)

### [Angluin Learner](/chapter/angluin_learner/)
### [Gold Paradigm](/chapter/gold_paradigm/)

## [Bioinformatics](/chapter/bioinformatics/)
DNA Sequence alignment algorithms.

### [Smith Waterman](/chapter/smith_waterman/)
### [Needleman Wunsch](/chapter/needleman_wunsch/)

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

## [Visualize](/chapter/Visualize)

### [Plots](/chapter/Plots)
### [Bar Charts](/chapter/BarCharts)
### [Grouped Bar Charts](/chapter/GroupedBarCharts)

Games
=====

```
"org.axle-lang" %% "axle-games" % "releaseVersion"
```

### [Tic Tac Toe](/chapter/TicTacToe)
### [Poker](/chapter/Poker)

Witnesses for 3rd party libraries
=================================

The "Spokes"

JBLAS
=====

```
"org.axle-lang" %% "axle-jblas" % "releaseVersion"
```

## [Matrix](/chapter/Matrix)
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
## [Graph](/chapter/Graph)
Directed and undirected graph witnesses for the [JUNG](http://jung.sourceforge.net/) library.

Spark
=====

```
"org.axle-lang" %% "axle-spark" % "releaseVersion"
```

Witnesses for [Spark](https://spark.apache.org/).

More Resources
==============

* [Installation](/chapter/installation/) notes
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
* [Project](/about/) background
* See the [Road Map](/road_map/) for more information on the release schedule and priorities
* [Author](/author/) background

