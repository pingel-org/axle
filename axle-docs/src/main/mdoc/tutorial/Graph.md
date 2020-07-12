---
layout: page
title: Graph
permalink: /tutorial/graph/
---

DirectedGraph typeclass and witnesses for the Jung package

## Directed Graph

Example with `String` is the vertex value and an `Edge` type with two values (a `String` and an `Int`) to represent the edges

```scala mdoc
val (a, b, c, d) = ("a", "b", "c", "d")

class Edge(val s: String, val i: Int)
```

Invoke the `DirectedGraph` typeclass with type parameters that denote
that we will use Jung's `DirectedSparseGraph` as the graph type, with
`String` and `Edge` as vertex and edge values, respectively.

```scala mdoc
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.algebra._
import axle.jung._

val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]
```

Use the `jdg` witness's `make` method to create the directed graph

```scala mdoc
val dg = jdg.make(List(a, b, c, d),
  List(
    (a, b, new Edge("hello", 1)),
    (b, c, new Edge("world", 4)),
    (c, d, new Edge("hi", 3)),
    (d, a, new Edge("earth", 1)),
    (a, c, new Edge("!", 7)),
    (b, d, new Edge("hey", 2))))
```

```scala mdoc
import cats.implicits._
import axle.syntax.directedgraph.directedGraphOps
import axle.syntax.finite.finiteOps

dg.vertexProjection.size

dg.edgeProjection.size

dg.findVertex(_ === "a").map(v => dg.successors(v))

dg.findVertex(_ === "c").map(v => dg.successors(v))

dg.findVertex(_ === "c").map(v => dg.predecessors(v))

dg.findVertex(_ === "c").map(v => dg.neighbors(v))
```

Create a Visualization of the graph

```scala mdoc
import cats.Show
import axle.visualize._

implicit val showEdge: Show[Edge] = new Show[Edge] {
  def show(e: Edge): String = e.s + " " + e.i
}

val dVis = DirectedGraphVisualization(
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
```

Render as sn SVG file

```scala mdoc
import axle.web._
import cats.effect._

dVis.svg[IO]("SimpleDirectedGraph.svg").unsafeRunSync()
```

![directed graph](/tutorial/images/SimpleDirectedGraph.svg)

## Undirected Graph

An undirected graph using the same dataa:

```scala mdoc:reset
val (a, b, c, d) = ("a", "b", "c", "d")

class Edge(val s: String, val i: Int)
```

Invoke the `UndirectedGraph` typeclass with type parameters that denote
that we will use Jung's `UndirectedSparseGraph` as the graph type, with
`String` and `Edge` as vertex and edge values, respectively.

```scala mdoc
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import axle.algebra._
import axle.jung._

val jug = UndirectedGraph.k2[UndirectedSparseGraph, String, Edge]
```

Use the `jug` witness's `make` method to create the undirected graph

```scala mdoc
val ug = jug.make(List(a, b, c, d),
  List(
    (a, b, new Edge("hello", 10)),
    (b, c, new Edge("world", 1)),
    (c, d, new Edge("hi", 3)),
    (d, a, new Edge("earth", 7)),
    (a, c, new Edge("!", 1)),
    (b, d, new Edge("hey", 2))))
```

```scala mdoc
import cats.implicits._
import axle.syntax.undirectedgraph.undirectedGraphOps
import axle.syntax.finite.finiteOps

ug.vertexProjection.size

ug.edgeProjection.size

ug.findVertex(_ == "c").map(v => ug.neighbors(v))

ug.findVertex(_ == "a").map(v => ug.neighbors(v))
```

Create a Visualization of the graph

```scala mdoc
import axle.visualize._

val uVis = UndirectedGraphVisualization(ug, width=300, height=300, border=10, color=Color.yellow)
```

Render as an SVG file

```scala mdoc
import axle.web._
import cats.effect._

uVis.svg[IO]("SimpleUndirectedGraph.svg").unsafeRunSync()
```

![undirected graph](/tutorial/images/SimpleUndirectedGraph.svg)
