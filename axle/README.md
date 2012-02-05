
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

Highlights include:

1. Haskell-like [typeclasses](http://www.haskell.org/haskellwiki/Typeclassopedia) such as Functor, Monoid, and Monad
1. Directed and undirected graphs.  [JUNG](http://jung.sourceforge.net/) is used for visualization.
1. Iterators like PowerSet and Permuter
1. Matrices.  Type-safe, expressive wrapper for [JBLAS](http://jblas.org/) which itself is a wrapper for [LAPACK](http://www.netlib.org/lapack/) -- a very fast matrix library.
1. Enrichments to Boolean, Set, and List

In cases where there are widely accepted non-textual representations,
code to support these visualizations are planned.
The first such subject is Graph Theory.

This project is in a "pre-alpha" state.
It's still under active development.  No public jar is maintained.

Table of Contents
-----------------

* [Installation](axle/doc/Installation.md)
* [Tutorial](axle/doc/Tutorial.md)
* [Road Map](axle/doc/RoadMap.md)
