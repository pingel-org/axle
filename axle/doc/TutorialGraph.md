
Graph
=====

Here are a couple of simple examples.
Eventually this page will include repl sessions showing in-depth interaction with these graphs.
The interfaces are likely to change substantially, as I have learned a lot about the 
type system and family polymorphism since I wrote this.

I also have some code that will use JUNG to create AWT visualizations.
More on that soon.

Undirected Graph
----------------

```scala
import org.pingel.axle.graph._

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

Directed Graph
--------------

```scala
import org.pingel.axle.graph._

class DE(v1: DN, v2: DN) extends DirectedGraphEdge[DN] {
  def getVertices() = (v1, v2)
  def getSource() = v1
  def getDest() = v2
}

class DN(label: String) extends DirectedGraphVertex[DE] {
  def getLabel(): String = label
}

class DG extends DirectedGraph[DN, DE] {}

val g = new DG()
val a = g.addVertex(new DN("a"))
val b = g.addVertex(new DN("b"))
val c = g.addVertex(new DN("c"))

g.size // should be 3
```
