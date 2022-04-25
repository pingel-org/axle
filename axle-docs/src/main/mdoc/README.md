# Axle

![axle](/images/axle.png)

Axle is an open-source Scala-embedded domain specific language for scientific computing.

* [Installation](introduction/Installation.md) notes
* [Gallery](introduction/Gallery.md) highlight some of the nicer visualizations
* [Spokes](introduction/Spokes.md) are support for 3rd party jars
* External [resources](introduction/Resources.md) and tools

## Tutorials

### Fundamental data structures and functions

* [Package Object](fundamental/PackageObject.md) Extensions to core Scala data types. Indexed Power Set, Indexed Cross Product, Combinations, Permutations, and UTF aliases
* [Algebra](fundamental/Algebra.md) Typeclasses Functor, Indexed, Finite, LengthSpace
* [Linear Algebra](fundamental/LinearAlgebra.md) including Principal Component Analysis (PCA)
* [Graph](fundamental/Graph.md)
* [Logic](fundamental/Logic.md) First-Order Predicate Logic

### Units of measurement

* [Quanta](units/Quanta.md) Units (second, mile, gram, etc) for various quanta (Speed, Distance, Mass, etc) and conversions between them
* [Unitted Trigonometry](units/UnittedTrigonometry.md)

### Commonly Used Mathematical Functions

* [Pythagorean Means](math/PythagoreanMeans.md) Arithmetic, Harmonic, Geometric, and Generalized means
* [MAP@K](math/MeanAveragePrecisionAtK.md) Mean Average Precision at K (ranking metric)
* Historically important functions
  * [π](math/Pi.md) estimation
  * [Fibonacci](math/Fibonacci.md)
  * [Ackermann](math/Ackermann.md)

### Randomness and Uncertainty

* [Probability Models](random_uncertain/ProbabilityModel.md) axiomatic probability models
  * Sampler, Region, Kolmogorov, Bayes, and Monad
* [Statistics](random_uncertain/Statistics.md) Random Variables, Probability, Distributions, Standard Deviation
  * [Root-mean-square deviation](random_uncertain/RootMeanSquareDeviation.md) (aka RMSE)
  * [Reservoir Sampling](random_uncertain/ReservoirSampling.md)
* [Information Theory](random_uncertain/InformationTheory.md)
  * [Entropy of a Coin](random_uncertain/CoinEntropy.md)
* [Probabilistic Graphical Models](random_uncertain/ProbabilisticGraphicalModels.md) (PGM)
  * [Bayesian Networks](random_uncertain/BayesianNetworks.md)

### Game Theory

* [Game Theory](game_theory/GameTheory.md) Framework for the game playing
* [Tic Tac Toe](game_theory/TicTacToe.md)
* [Poker](game_theory/Poker.md)
* [Monty Hall](game_theory/MontyHall.md)
* [Prisoners Dilemma](game_theory/PrisonersDilemma.md)

### Machine Learning

* [Linear Regression](machine_learning/LinearRegression.md)
* [Naive Bayes Clustering](machine_learning/NaiveBayesClassifier.md) Tennis example
* k-Means Clustering
  * [Cluster Irises](machine_learning/ClusterIrises.md)
  * [Cluster Federalist Papers](machine_learning/ClusterFederalistPapers.md)
* [Genetic Algorithms](machine_learning/GeneticAlgorithms.md)

### Domain-specific functions

* [Geo Coordinates](math/GeoCoordinates.md)
* [Natural Langage Processing (NLP)](text/NaturalLanguageProcessing.md)
  * [Language Modules](text/LanguageModules.md) including Stemming and Stop Words
  * [Edit Distance](text/EditDistance.md) Levenshtein
  * [Vector Space Model](text/VectorSpaceModel.md) including TF-IDF
* [Linguistics](text/Linguistics.md)
  * [Angluin Learner](text/AngluinLearner.md)
  * [Gold Paradigm](text/GoldParadigm.md)
* [Bioinformatics](bioinformatics/Bioinformatics.md) DNA Sequence alignment algorithms
  * [Smith Waterman](bioinformatics/SmithWaterman.md)
  * [Needleman Wunsch](bioinformatics/NeedlemanWunsch.md)
* Chaos Theory
  * [Logistic Map](chaos_theory/LogisticMap.md)
  * [Mandelbrot Set](chaos_theory/Mandelbrot.md)

### Visualization

* [Visualize](visualization/Visualize.md)
  * [Plots](visualization/Plots.md)
  * [ScatterPlot](visualization/ScatterPlot.md)
  * [Bar Charts](visualization/BarCharts.md)
  * [Grouped Bar Charts](visualization/GroupedBarCharts.md)
  * [Pixelated Colored Area](visualization/PixelatedColoredArea.md)

### Data

* Data sets from `axle.data`
  * Astronomy
  * Evolution
  * Federalist Papers
  * Irises

## Additional context

* The [history](appendix/History.md) of axle
* See [Release Notes](appendix/ReleaseNotes.md) for the record of previously released features.
* See [Road Map](appendix/RoadMap.md) for the plan of upcoming releases and features.
* [Author](appendix/Author.md) background
* Other related [Videos](appendix/Videos.md)
