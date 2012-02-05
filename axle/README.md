
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
like Equation Editor or a wysiwig LaTeX --
with the added benefit that the expressions actually work!

Highlights include:

1. Haskell-like [typeclasses](http://www.haskell.org/haskellwiki/Typeclassopedia) such as Functor, Monoid, and Monad
1. Directed and undirected graphs.  [JUNG](http://jung.sourceforge.net/) is used for visualization.
1. Iterators like PowerSet and Permuter
1. Matrices.  Type-safe, expressive wrapper for [JBLAS](http://jblas.org/) which itself is a wrapper for [LAPACK](http://www.netlib.org/lapack/) -- a very fast matrix library.
1. Enrichments to Boolean and Set

The examples in
["Learn You a Haskell for Great Good"](http://learnyouahaskell.com/)
should be expressible with a syntax as similar to Haskell as possible --
but in Scala.
Typeclasses like Functor, Applicative Functor, Monoid, and Monad are modelled explicitely.
(This is similar to the goal of the [Scalaz](https://github.com/scalaz/scalaz) project.)
One exception is that little syntactic details like the order of arguments to "fmap"
are the same as in Haskell.

Future versions may include examples of Miles Sabin's work with HList, including his [Shapeless](https://github.com/milessabin/shapeless) project.

Status
------

Pre-alpha.  Still under active development.  No public jar is maintained.

Installation
------------

TODO

The Matrix class depends on JBLAS, which is not hosted on any public repository.

AXLE itself it not yet hosted in a public repository.

### SBT

```bash
curl http://cloud.github.com/downloads/mikiobraun/jblas/jblas-1.2.0.jar -o lib/jblas-1.2.0.jar
```

### Maven

Download and install in the local build tool:

```bash
curl http://cloud.github.com/downloads/mikiobraun/jblas/jblas-1.2.0.jar -o jblas-1.2.0.jar

mvn install:install-file -DgroupId=org.jblas -DartifactId=jblas -Dversion=1.2.0 -Dfile=jblas-1.2.0.jar -Dpackaging=jar -DgeneratePom=true
```

Tutorial
--------

To be written.  It will contain examples of:

* Streamline imports
* Functor, ApplicativeFunctor, Monoid, Monad, ...
* DirectedGraph
* UndirectedGraph
* PowerSet
* Set and Boolean enrichments

```scala

import org.pingel.axle.graph.UndirectedGraph
import org.pingel.axle.graph.UndirectedGraphVertex
import org.pingel.axle.graph.UndirectedGraphEdge

class EliminationTreeEdge(v1: EliminationTreeNode, v2: EliminationTreeNode)
extends UndirectedGraphEdge[EliminationTreeNode]
{
	def getVertices() = (v1, v2)
}

class EliminationTreeNode(label: String)
extends UndirectedGraphVertex[EliminationTreeEdge]
{
  def getLabel(): String = label

}


class EliminationTree
extends UndirectedGraph[EliminationTreeNode, EliminationTreeEdge]
{
   ...
}

```

To Do
-----

* logistic regression
* kmeans
* pca

* matrix (context/view bounds | type projections | self type)
* graph family, use self-type
* update readmes

* linear regression: more testing, logging, tuning/vectorization
* fix cross product spec
* plotting
* Matrix: array matrix
* Matrix: muliRow, muliColumn, etc
* Matrix: dependent types for static checks of size constraints on matrix operations?
* Separate Jung and AWT references from core library

* Matrix: lt, gt, etc, should return Matrix[Boolean]
* Matrix: invert
* More tests in specs
* Write tutorial. See
   * http://volga.eng.yale.edu/sohrab/matlab_tutorial.html
   * http://en.wikibooks.org/wiki/Octave_Programming_Tutorial
   * http://www-mdp.eng.cam.ac.uk/web/CD/engapps/octave/octavetut.pdf



