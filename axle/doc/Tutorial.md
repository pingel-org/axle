
Tutorial
========

Functor
-------

The examples in
["Learn You a Haskell for Great Good"](http://learnyouahaskell.com/)
should be expressible with a syntax as similar to Haskell as possible --
but in Scala.
Typeclasses like Functor, Applicative Functor, Monoid, and Monad are modelled explicitely.
(This is similar to the goal of the [Scalaz](https://github.com/scalaz/scalaz) project.)
One exception is that little syntactic details like the order of arguments to "fmap"
are the same as in Haskell.

```scala
TODO
```


Applicative Functor
-------------------

```scala
TODO
```

Monoid
------

```scala
TODO
```

Monad
-----

```scala
TODO
```

Directed Graph
--------------

```scala
TODO
```

Undirected Graph
----------------

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

PowerSet
--------

```scala
TODO
```


Permuter
--------

```scala
TODO
```


Boolean Enrichments
-------------------

```scala
TODO
```


Set Enrichments
---------------

```scala
TODO
```

List Enrichments
----------------

```scala
TODO
```

Scala
-----

* Daniel Sobral's [Scala on the Web](http://dcsobral.blogspot.com/2011/12/scala-on-web.html)
* [StackOverflow Scala Tutorial](http://stackoverflow.com/tags/scala/info) Awesome guide to the most useful and helpful questions.

And a few StackOverflow topics that I found particularly useful in Axle:

* [Context bounds](http://stackoverflow.com/questions/2982276/what-is-a-context-bound-in-scala)
* [Type projections](http://stackoverflow.com/questions/7045967/what-are-type-projections-useful-for)
* Family Polymorphism
* Self Types
* [HLists and foldCurry](http://stackoverflow.com/questions/7606587/applying-an-argument-list-to-curried-function-using-foldleft-in-scala) (my question answered by Miles Sabin)
