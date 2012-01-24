
Axle (org.pingel.axle)
======================

An attempt to implement a Haskell-like language as an internal DSL in Scala.

The contributions are:

1. Haskell-like [typeclasses](http://www.haskell.org/haskellwiki/Typeclassopedia)
1. Directed and undirected graphs.  [JUNG](http://jung.sourceforge.net/) is used for visualization.
1. Iterators like PowerSet and Permuter
1. Matrices.  Type-safe, expressive wrapper for [JBLAS](http://jblas.org/) which itself is a wrapper for [LAPAK](http://www.netlib.org/lapack/) -- a very fast matrix library.
1. Enrichments to Boolean and Set

The examples in ["Learn You a Haskell for Great Good"](http://learnyouahaskell.com/) should be expressible
with a syntax as similar to Haskell as possible -- but in Scala.
Typeclasses like Functor, Applicative Functor, Monoid, and Monad are modelled explicitely.
This is similar to the goal of the [Scalaz](https://github.com/scalaz/scalaz) project.
One exception is that little syntactic details like the order of arguments to "fmap"
are the same as in Haskell.

Future versions may include examples of Miles Sabin's work with HList, including his [Shapeless](https://github.com/milessabin/shapeless) project.

Status
------

Pre-alpha.  Still under active development.  No public jar is maintained.

Installation
------------

The Matrix class depends on JBLAS, which is not hosted on any public repository.
It can be downloaded and installed locally with two steps:

```bash
curl http://cloud.github.com/downloads/mikiobraun/jblas/jblas-1.2.0.jar -o lib/jblas-1.2.0.jar

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

* Specs
* Family polymorphism (with self types) for DG and UG families
* Separate Jung and AWT references from core library
* Write tutorial





