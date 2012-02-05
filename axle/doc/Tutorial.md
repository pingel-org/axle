
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

The examples in
["Learn You a Haskell for Great Good"](http://learnyouahaskell.com/)
should be expressible with a syntax as similar to Haskell as possible --
but in Scala.
Typeclasses like Functor, Applicative Functor, Monoid, and Monad are modelled explicitely.
(This is similar to the goal of the [Scalaz](https://github.com/scalaz/scalaz) project.)
One exception is that little syntactic details like the order of arguments to "fmap"
are the same as in Haskell.

