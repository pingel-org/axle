
Axle (org.pingel.axle)
======================

An attempt to implement a Haskell-like language as an internal DSL in Scala.

My goal is to make the examples in "Learn You a Haskell for Great Good" work
using a syntax as similar to Haskell as possible using Scala.

In particular, I would like typeclasses like Functor, Applicative Functor,
Monoid, and Monad to be modelled explicitely.

This is similar to the goal of the Scalaz project.
One notable exception is that I would like to preserve little syntactic details
like the order of arguments to "fmap".

I would/will also enjoy thinking through the Scala->Haskell mapping on my own,
since I don't fully understand Scala, Haskell, or Scalaz's choice of mapping
concepts between them.
Forcing myself to learn about all of these from scratch should substantially
deepen my understanding of all those subjects.

I would also like to include some examples of Miles Sabin's work with HList,
including his Shapeless project.

This project may also eventually include what is currently in my "util" package.
This depends on whether I think I think those data structures can neatly
fit into whatever emerges at the Axle design philosophy.
I don't want to encumber "util" with too much arbitrary opinion.

Tutorial
--------

To be written.  It will contain examples of:

* DirectedGraph
* UndirectedGraph
* PowerSet
* etc...

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
* Separate Jung and AWT references from core library
* Write tutorial





