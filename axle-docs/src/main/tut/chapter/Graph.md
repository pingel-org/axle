---
layout: page
title: Graph
permalink: /chapter/graph/
---

DirectedGraph typeclass and witnesses for the Jung package

Directed Graph
--------------

Imports and implicits

```tut:book:silent
import axle._
import axle.algebra._
import axle.jung._
import axle.syntax.directedgraph.directedGraphOps
import axle.syntax.undirectedgraph.undirectedGraphOps
import spire.implicits.StringOrder
import spire.implicits.eqOps
import axle.syntax.finite.finiteOps
import edu.uci.ics.jung.graph.DirectedSparseGraph

class Edge
implicit val showEdge: Show[Edge] = new Show[Edge] { def text(e: Edge): String = "" }
```

Example

```tut:book
val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]

val a = "a"
val b = "b"
val c = "c"
val d = "d"

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
import axle.web._

svg(dg, "SimpleDirectedGraph.svg")
```

![directed graph](/chapter/images/SimpleDirectedGraph.svg)

Undirected Graph
----------------

Imports and implicits

```tut:book:silent
import edu.uci.ics.jung.graph.UndirectedSparseGraph

class Edge
implicit val showEdge: Show[Edge] = new Show[Edge] { def text(e: Edge): String = "" }
```

Example

```tut:book
val jug = UndirectedGraph.k2[UndirectedSparseGraph, String, Edge]

val a = "a"
val b = "b"
val c = "c"
val d = "d"

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
import axle.web._

svg(ug, "SimpleUndirectedGraph.svg")
```

![undirected graph](/chapter/images/SimpleUndirectedGraph.svg)
