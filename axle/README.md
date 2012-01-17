
Axle (org.pingel.axle)
======================

An attempt to implement a Haskell-like language as an internal DSL in Scala.

The three main contributions are:

1. Haskell-like typeclasses
1. Data structures like (un)directed graphs and matrices
1. Enrichments to Boolean and Set

The examples in "Learn You a Haskell for Great Good" should be expressible
with a syntax as similar to Haskell as possible -- but in Scala.
Typeclasses like Functor, Applicative Functor, Monoid, and Monad are modelled explicitely.
This is similar to the goal of the Scalaz project.
One exception is that little syntactic details like the order of arguments to "fmap"
are the same as in Haskell.

Future versions may include examples of Miles Sabin's work with HList, including his Shapeless project.

Status
------

Pre-alpha.  Still under active development.  No public jar is maintained.


Tutorial
--------

To be written.  It will contain examples of:

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





