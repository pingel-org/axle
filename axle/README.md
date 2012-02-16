
Axle (org.pingel.axle)
======================

Axle is Domain Specific Language in Scala intended for expressing
algorithms and mathematical concepts in a way that closely
resembles the way the are described in academic texts.
Scala's support of functional programming, type inference, unicode,
and implicits make it an ideal host language.

Tools for Scala such at the Eclipse Plugin and the Ensime plugin for
several other editors create editing environments that give quick
feedback to authors about the validity of the expressions.
The combination of Axle and these editing tools creates a system
like Equation Editor or a wysiwyg LaTeX --
with the added benefit that the expressions actually work!

In cases where there are widely accepted non-textual representations,
code to support these visualizations are planned.
The first such subject is Graph Theory.

Tutorials
---------

1. [Type Classes](/adampingel/pingel.org/blob/master/axle/doc/TutorialTypeClasses.md): Haskell-like [typeclasses](http://www.haskell.org/haskellwiki/Typeclassopedia) such as Functor, Applicative Functor, Monoid, and Monad
1. [Iterators](/adampingel/pingel.org/blob/master/axle/doc/TutorialIterators.md): Power Set, Permuter, Cross Product
1. [Enrichments](/adampingel/pingel.org/blob/master/axle/doc/TutorialEnrichments.md): Boolean, Set, List
1. [Quanta](/adampingel/pingel.org/blob/master/axle/doc/TutorialQuantum.md) units (second, mile, gram, etc) and quantities for various quanta (Speed, Distance, Mass, etc) and conversions between them
1. [Matrix](/adampingel/pingel.org/blob/master/axle/doc/TutorialMatrix.md) Type-safe, expressive wrapper for [JBLAS](http://jblas.org/) which itself is a wrapper for [LAPACK](http://www.netlib.org/lapack/) -- a very fast matrix library.
1. [Graphs](/adampingel/pingel.org/blob/master/axle/doc/TutorialGraph.md): Directed and undirected graphs.  [JUNG](http://jung.sourceforge.net/) is used for visualization.
1. [Machine Learning](axle/doc/TutorialMachineLearning.md): Linear Regression, Logistic Regression, Neural Networks, K-Means Clustering, Principal Component Analysis
1. [Scala](axle/doc/TutorialScala.md)

Details
-------

See [Installation notes](/adampingel/pingel.org/blob/master/axle/doc/Installation.md)

This project is in a "pre-alpha" state.
It's still under active development.  No public jar is maintained.

The [Road Map](/adampingel/pingel.org/blob/master/axle/doc/RoadMap.md) contains some ideas about future direction.

Axle is a component of the [pingel.org](./) umbrella project.
