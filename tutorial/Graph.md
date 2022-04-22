---
layout: page
title: Graph
permalink: /tutorial/graph/
---

DirectedGraph typeclass and witnesses for the Jung package

## Directed Graph

Example with `String` is the vertex value and an `Edge` type with two values (a `String` and an `Int`) to represent the edges

```scala
val (a, b, c, d) = ("a", "b", "c", "d")
// a: String = "a"
// b: String = "b"
// c: String = "c"
// d: String = "d"

class Edge(val s: String, val i: Int)
```

Invoke the `DirectedGraph` typeclass with type parameters that denote
that we will use Jung's `DirectedSparseGraph` as the graph type, with
`String` and `Edge` as vertex and edge values, respectively.

```scala
import edu.uci.ics.jung.graph.DirectedSparseGraph
import axle.algebra._
import axle.jung._

val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]
// jdg: DirectedGraph[DirectedSparseGraph[String, Edge], String, Edge] = axle.jung.package$$anon$7@4cc1ca7e
```

Use the `jdg` witness's `make` method to create the directed graph

```scala
val dg = jdg.make(List(a, b, c, d),
  List(
    (a, b, new Edge("hello", 1)),
    (b, c, new Edge("world", 4)),
    (c, d, new Edge("hi", 3)),
    (d, a, new Edge("earth", 1)),
    (a, c, new Edge("!", 7)),
    (b, d, new Edge("hey", 2))))
// dg: DirectedSparseGraph[String, Edge] = Vertices:a,b,c,d
// Edges:repl.MdocSession$App$Edge@703ec59d[c,d] repl.MdocSession$App$Edge@8e5c260[a,c] repl.MdocSession$App$Edge@3e5f8288[a,b] repl.MdocSession$App$Edge@7b5d0b27[b,c] repl.MdocSession$App$Edge@1b72f468[b,d] repl.MdocSession$App$Edge@4a6c5267[d,a]
```

```scala
import cats.implicits._
import axle.syntax.directedgraph.directedGraphOps
import axle.syntax.finite.finiteOps

dg.vertexProjection.size
// res0: Int = 4

dg.edgeProjection.size
// res1: Int = 6

dg.findVertex(_ === "a").map(v => dg.successors(v))
// res2: Option[Set[String]] = Some(value = Set("b", "c"))

dg.findVertex(_ === "c").map(v => dg.successors(v))
// res3: Option[Set[String]] = Some(value = Set("d"))

dg.findVertex(_ === "c").map(v => dg.predecessors(v))
// res4: Option[Set[String]] = Some(value = Set("a", "b"))

dg.findVertex(_ === "c").map(v => dg.neighbors(v))
// res5: Option[Set[String]] = Some(value = Set("a", "b", "d"))
```

Create a Visualization of the graph

```scala
import cats.Show

implicit val showEdge: Show[Edge] = new Show[Edge] {
  def show(e: Edge): String = e.s + " " + e.i
}
// showEdge: Show[Edge] = repl.MdocSession$App$$anon$1@eef64a2

import axle.visualize._

val dVis = DirectedGraphVisualization[DirectedSparseGraph[String, Edge], String, Edge](
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
// dVis: DirectedGraphVisualization[DirectedSparseGraph[String, Edge], String, Edge] = DirectedGraphVisualization(
//   dg = Vertices:a,b,c,d
// Edges:repl.MdocSession$App$Edge@703ec59d[c,d] repl.MdocSession$App$Edge@8e5c260[a,c] repl.MdocSession$App$Edge@3e5f8288[a,b] repl.MdocSession$App$Edge@7b5d0b27[b,c] repl.MdocSession$App$Edge@1b72f468[b,d] repl.MdocSession$App$Edge@4a6c5267[d,a] ,
//   width = 300,
//   height = 300,
//   border = 10,
//   radius = 10,
//   arrowLength = 10,
//   color = Color(r = 0, g = 255, b = 0),
//   borderColor = Color(r = 0, g = 0, b = 0),
//   fontSize = 12,
//   layoutOpt = None
// )
```

Render as sn SVG file

```scala
import axle.web._
import cats.effect._

dVis.svg[IO]("SimpleDirectedGraph.svg").unsafeRunSync()
```

![directed graph](/tutorial/images/SimpleDirectedGraph.svg)

## Undirected Graph

An undirected graph using the same dataa:

```scala
val (a, b, c, d) = ("a", "b", "c", "d")
// a: String = "a"
// b: String = "b"
// c: String = "c"
// d: String = "d"

class Edge(val s: String, val i: Int)
```

Invoke the `UndirectedGraph` typeclass with type parameters that denote
that we will use Jung's `UndirectedSparseGraph` as the graph type, with
`String` and `Edge` as vertex and edge values, respectively.

```scala
import edu.uci.ics.jung.graph.UndirectedSparseGraph
import axle.algebra._
import axle.jung._

val jug = UndirectedGraph.k2[UndirectedSparseGraph, String, Edge]
// jug: UndirectedGraph[UndirectedSparseGraph[String, Edge], String, Edge] = axle.jung.package$$anon$11@236862b9
```

Use the `jug` witness's `make` method to create the undirected graph

```scala
val ug = jug.make(List(a, b, c, d),
  List(
    (a, b, new Edge("hello", 10)),
    (b, c, new Edge("world", 1)),
    (c, d, new Edge("hi", 3)),
    (d, a, new Edge("earth", 7)),
    (a, c, new Edge("!", 1)),
    (b, d, new Edge("hey", 2))))
// ug: UndirectedSparseGraph[String, Edge] = Vertices:a,b,c,d
// Edges:repl.MdocSession$App7$Edge@e08c63[b,c] repl.MdocSession$App7$Edge@25cc4879[c,d] repl.MdocSession$App7$Edge@5c200ca7[d,a] repl.MdocSession$App7$Edge@4efdcd8[b,d] repl.MdocSession$App7$Edge@68db22[a,c] repl.MdocSession$App7$Edge@6c960049[a,b]
```

```scala
import cats.implicits._
import axle.syntax.undirectedgraph.undirectedGraphOps
import axle.syntax.finite.finiteOps

ug.vertexProjection.size
// res8: Int = 4

ug.edgeProjection.size
// res9: Int = 6

ug.findVertex(_ == "c").map(v => ug.neighbors(v))
// res10: Option[Iterable[String]] = Some(value = Iterable("a", "b", "d"))

ug.findVertex(_ == "a").map(v => ug.neighbors(v))
// res11: Option[Iterable[String]] = Some(value = Iterable("b", "c", "d"))
```

Create a Visualization of the graph

```scala
import cats.Show

implicit val showEdge: Show[Edge] = new Show[Edge] {
  def show(e: Edge): String = e.s + " " + e.i
}
// showEdge: Show[Edge] = repl.MdocSession$App7$$anon$2@229aab88

import axle.visualize._

val uVis = UndirectedGraphVisualization[UndirectedSparseGraph[String, Edge], String, Edge](
  ug,
  width = 300,
  height = 300,
  border = 10,
  color = Color.yellow)
// uVis: UndirectedGraphVisualization[UndirectedSparseGraph[String, Edge], String, Edge] = UndirectedGraphVisualization(
//   ug = Vertices:a,b,c,d
// Edges:repl.MdocSession$App7$Edge@e08c63[b,c] repl.MdocSession$App7$Edge@25cc4879[c,d] repl.MdocSession$App7$Edge@5c200ca7[d,a] repl.MdocSession$App7$Edge@4efdcd8[b,d] repl.MdocSession$App7$Edge@68db22[a,c] repl.MdocSession$App7$Edge@6c960049[a,b] ,
//   width = 300,
//   height = 300,
//   border = 10,
//   radius = 10,
//   color = Color(r = 255, g = 255, b = 0),
//   borderColor = Color(r = 0, g = 0, b = 0),
//   fontSize = 12
// )
```

Render as an SVG file

```scala
import axle.web._
import cats.effect._

uVis.svg[IO]("SimpleUndirectedGraph.svg").unsafeRunSync()
```

![undirected graph](/tutorial/images/SimpleUndirectedGraph.svg)
