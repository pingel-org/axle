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

And a stateless `Edge` type as the edge value

```tut:book
class Edge
```

Invokes the `DirectedGraph` typeclass with type parameters that denote
that we will use Jung's `DirectedSparseGraph` as the graph type, with
`String` and `Edge` as vertex and edge values, respectively.

```tut:book
val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]
```

Use the `jdg` witness's `make` method to create the directed graph

```tut:book
val dg = jdg.make(List(a, b, c, d),
  List(
    (a, b, new Edge),
    (b, c, new Edge),
    (c, d, new Edge),
    (d, a, new Edge),
    (a, c, new Edge),
    (b, d, new Edge)))
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

implicit val showEdge: Show[Edge] = new Show[Edge] { def show(e: Edge): String = "" }

val vis = DirectedGraphVisualization(
  dg,
  width = 500,
  height = 500,
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

Imports and implicits

```tut:book
import edu.uci.ics.jung.graph.UndirectedSparseGraph
```

Example using the `Edge` edge value defined above.

```tut:book
val jug = UndirectedGraph.k2[UndirectedSparseGraph, String, Edge]

val (a, b, c, d) = ("a", "b", "c", "d")

val ug = jug.make(List(a, b, c, d),
  List(
    (a, b, new Edge),
    (b, c, new Edge),
    (c, d, new Edge),
    (d, a, new Edge),
    (a, c, new Edge),
    (b, d, new Edge)))
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

val vis = UndirectedGraphVisualization(ug, width=500, height=500, border=10, color=Color.yellow)

svg(vis, "SimpleUndirectedGraph.svg")
```

![undirected graph](/tutorial/images/SimpleUndirectedGraph.svg)
