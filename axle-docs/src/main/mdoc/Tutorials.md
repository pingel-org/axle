---
layout: page
title: Tutorials
in_header: true
permalink: /tutorial/
---

## Core

```sbt
"org.axle-lang" %% "axle-core"  % "@RELEASE_VERSION@"
"org.axle-lang" %% "axle-laws"  % "@RELEASE_VERSION@"
"org.axle-lang" %% "axle-xml"   % "@RELEASE_VERSION@"
"org.axle-lang" %% "axle-jung"  % "@RELEASE_VERSION@"
"org.axle-lang" %% "axle-jblas" % "@RELEASE_VERSION@"
```

### Fundamental data structures and functions

* [Package Object](/tutorial/axle_package_object/) Extensions to core Scala data types. Indexed Power Set, Indexed Cross Product, Combinations, Permutations, and UTF aliases
* [Algebra](/tutorial/algebra/) Typeclasses Functor, Indexed, Finite, LengthSpace
* [Linear Algebra](/tutorial/linear_algebra/) including Principal Component Analysis (PCA)
* [Graph](/tutorial/graph/)
* [Logic](/tutorial/logic/) First-Order Predicate Logic

### Units of measurement

* [Quanta](/tutorial/quanta/) Units (second, mile, gram, etc) for various quanta (Speed, Distance, Mass, etc) and conversions between them
* [Unitted Trigonometry](/tutorial/unitted_trigonometry/)

### Commonly Used Mathematical Functions

* [Pythagorean Means](/tutorial/pythagorean_means/) Arithmetic, Harmonic, Geometric, and Generalized means
* [MAP@K](/tutorial/map_at_k) Mean Average Precision at K (ranking metric)
* Historically important functions
  * [π](/tutorial/pi/) estimation
  * [Fibonacci](/tutorial/fibonacci/)
  * [Ackermann](/tutorial/ackermann/)

### Randomness and Uncertainty

* [Probability Models](/tutorial/probability_model) axiomatic probability models
  * [Two Dice](/tutorial/two_dice/) simulation vs flat mapping distribution monads
* [Statistics](/tutorial/statistics/) Random Variables, Probability, Distributions, Standard Deviation
  * [Root-mean-square deviation](/tutorial/rmsd/) (aka RMSE)
  * [Reservoir Sampling](/tutorial/reservoir_sampling/)
* [Information Theory](/tutorial/information_theory/)
  * [Entropy of a Coin](/tutorial/entropy_biased_coin/)
* [Probabilistic Graphical Models](/tutorial/probabilistic_graphical_models/) (PGM)
  * [Bayesian Networks](/tutorial/bayesian_networks/)
* [Game Theory](/tutorial/game_theory/) Framework for the game playing jar.

### Machine Learning

* [Linear Regression](/tutorial/linear_regression/)
* [Naive Bayes Clustering](/tutorial/naive_bayes/) Tennis example
* k-Means Clustering
  * [Cluster Irises](/tutorial/cluster_irises_k_means/)
  * [Cluster Federalist Papers](/tutorial/cluster_federalist_papers_k_means/)
* [Genetic Algorithms](/tutorial/genetic_algorithms/)

### Domain-specific functions

* [Geo Coordinates](/tutorial/geo_coordinates/)
* [Natural Langage Processing (NLP)](/tutorial/natural_language_processing/)
  * [Language Modules](/tutorial/language_modules/) including Stemming and Stop Words
  * [Edit Distance](/tutorial/edit_distance/) Levenshtein
  * [Vector Space Model](/tutorial/vector_space_model/) including TF-IDF
* [Linguistics](/tutorial/linguistics/)
  * [Angluin Learner](/tutorial/angluin_learner/)
  * [Gold Paradigm](/tutorial/gold_paradigm/)
* [Bioinformatics](/tutorial/bioinformatics/) DNA Sequence alignment algorithms
  * [Smith Waterman](/tutorial/smith_waterman/)
  * [Needleman Wunsch](/tutorial/needleman_wunsch/)
* Chaos Theory
  * [Logistic Map](/tutorial/logistic_map/)
  * [Mandelbrot Set](/tutorial/mandelbrot/)

### Visualization

* [Visualize](/tutorial/visualize/)
  * [Plots](/tutorial/plots/)
  * [ScatterPlot](/tutorial/scatterplot/)
  * [Bar Charts](/tutorial/bar_charts/)
  * [Grouped Bar Charts](/tutorial/grouped_bar_charts/)
  * [Pixelated Colored Area](/tutorial/pixelated_colored_area/)

## Wheel

```sbt
"org.axle-lang" %% "axle-wheel" % "@RELEASE_VERSION@"
```

### Data

* Data sets from `axle.data`
  * Astronomy
  * Evolution
  * Federalist Papers
  * Irises

### Games

* [Tic Tac Toe](/tutorial/tic_tac_toe/)
* [Poker](/tutorial/poker/)
* [Monty Hall](/tutorial/monty_hall/)
* [Prisoners Dilemma](/tutorial/prisoner/)
