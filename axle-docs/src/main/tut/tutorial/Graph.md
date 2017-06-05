---
layout: page
title: Graph
permalink: /tutorial/graph/
---

DirectedGraph typeclass and witnesses for the Jung package

Directed Graph
--------------

Imports and implicits

```tut:silent
import cats.implicits._
import cats.Show
import axle._

import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.jung._

import axle.algebra._
import axle.syntax.directedgraph.directedGraphOps
import axle.syntax.undirectedgraph.undirectedGraphOps
import axle.syntax.finite.finiteOps
```

Example with `String` is the vertex value

```tut:book
val (a, b, c, d) = ("a", "b", "c", "d")
```

And an `Edge` type with two values (a `String` and an `Int`) to represent the edges

```tut:book
class Edge(val s: String, val i: Int)
```

Invoke the `DirectedGraph` typeclass with type parameters that denote
that we will use Jung's `DirectedSparseGraph` as the graph type, with
`String` and `Edge` as vertex and edge values, respectively.

```tut:book
val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]
```

Use the `jdg` witness's `make` method to create the directed graph

```tut:book
val dg = jdg.make(List(a, b, c, d),
  List(
    (a, b, new Edge("hello", 1)),
    (b, c, new Edge("world", 4)),
    (c, d, new Edge("hi", 3)),
    (d, a, new Edge("earth", 1)),
    (a, c, new Edge("!", 7)),
    (b, d, new Edge("hey", 2))))
```

```tut:book
dg.size

dg.findVertex(_ === "a").map(v => dg.successors(v))

dg.findVertex(_ === "c").map(v => dg.successors(v))

dg.findVertex(_ === "c").map(v => dg.predecessors(v))

dg.findVertex(_ === "c").map(v => dg.neighbors(v))
```

Visualize the graph

```tut:book
import axle.visualize._
import axle.web._

implicit val showEdge: Show[Edge] = new Show[Edge] {
  def show(e: Edge): String = e.s + " " + e.i
}

val vis = DirectedGraphVisualization(
  dg,
  width = 300,
  height = 300,
  border = 10,
  radius = 10,
  arrowLength = 10,
  color = Color.green,
  borderColor = Color.black,
  fontSize = 12
)

svg(vis, "SimpleDirectedGraph.svg")
```

![directed graph](/tutorial/images/SimpleDirectedGraph.svg)

Undirected Graph
----------------

Imports

```tut:book
import edu.uci.ics.jung.graph.UndirectedSparseGraph
```

Example using the `Edge` edge value defined above and the same vertex values defined above.

Invoke the `UndirectedGraph` typeclass with type parameters that denote
that we will use Jung's `UndirectedSparseGraph` as the graph type, with
`String` and `Edge` as vertex and edge values, respectively.

```tut:book
val jug = UndirectedGraph.k2[UndirectedSparseGraph, String, Edge]
```

Use the `jug` witness's `make` method to create the undirected graph

```tut:book
val ug = jug.make(List(a, b, c, d),
  List(
    (a, b, new Edge("hello", 10)),
    (b, c, new Edge("world", 1)),
    (c, d, new Edge("hi", 3)),
    (d, a, new Edge("earth", 7)),
    (a, c, new Edge("!", 1)),
    (b, d, new Edge("hey", 2))))
```

```tut:book
ug.size

ug.findVertex(_ == "c").map(v => ug.neighbors(v))

ug.findVertex(_ == "a").map(v => ug.neighbors(v))
```

Visualize the graph

```tut:book
import axle.visualize._
import axle.web._

val vis = UndirectedGraphVisualization(ug, width=300, height=300, border=10, color=Color.yellow)

svg(vis, "SimpleUndirectedGraph.svg")
```

![undirected graph](/tutorial/images/SimpleUndirectedGraph.svg)
