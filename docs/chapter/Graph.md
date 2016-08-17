
Graph
=====

DirectedGraph typeclass and witnesses for the Jung package

Directed Graph
--------------

Imports and implicits

```scala
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

```scala
scala> val jdg = DirectedGraph.k2[DirectedSparseGraph, String, Edge]
jdg: axle.algebra.DirectedGraph[edu.uci.ics.jung.graph.DirectedSparseGraph[String,Edge],String,Edge] = axle.jung.package$$anon$4@b87b353

scala> val a = "a"
a: String = a

scala> val b = "b"
b: String = b

scala> val c = "c"
c: String = c

scala> val d = "d"
d: String = d

scala> val dg = jdg.make(List(a, b, c, d),
     |   List(
     |     (a, b, new Edge),
     |     (b, c, new Edge),
     |     (c, d, new Edge),
     |     (d, a, new Edge),
     |     (a, c, new Edge),
     |     (b, d, new Edge)))
dg: edu.uci.ics.jung.graph.DirectedSparseGraph[String,Edge] =
Vertices:a,b,c,d
Edges:Edge@637904c9[a,b] Edge@64f209b[d,a] Edge@6ca7f2a3[b,d] Edge@2096d82e[c,d] Edge@2d3b33d7[a,c] Edge@7f17c94a[b,c]
```

```scala
scala> dg.size
res1: Int = 4

scala> dg.findVertex(_ === "a").map(v => dg.successors(v))
res2: Option[Set[String]] = Some(Set(b, c))

scala> dg.findVertex(_ === "c").map(v => dg.successors(v))
res3: Option[Set[String]] = Some(Set(d))

scala> dg.findVertex(_ === "c").map(v => dg.predecessors(v))
res4: Option[Set[String]] = Some(Set(a, b))

scala> dg.findVertex(_ === "c").map(v => dg.neighbors(v))
res5: Option[Set[String]] = Some(Set(a, b, d))
```

Visualize the graph

```scala
scala> import axle.web._
import axle.web._

scala> svg(dg, "SimpleDirectedGraph.svg")
java.lang.NoClassDefFoundError: edu/uci/ics/jung/visualization/DefaultVisualizationModel
  at axle.web.SVG$$anon$18.svg(SVG.scala:495)
  at axle.web.SVG$$anon$18.svg(SVG.scala:479)
  at axle.web.package$.svg(package.scala:26)
  ... 418 elided
Caused by: java.lang.ClassNotFoundException: edu.uci.ics.jung.visualization.DefaultVisualizationModel
  at java.net.URLClassLoader.findClass(URLClassLoader.java:381)
  at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
  at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
  ... 421 more
```

![directed graph](../images/SimpleDirectedGraph.svg)

Undirected Graph
----------------

Imports and implicits

```scala
import edu.uci.ics.jung.graph.UndirectedSparseGraph

class Edge
implicit val showEdge: Show[Edge] = new Show[Edge] { def text(e: Edge): String = "" }
```

Example

```scala
scala> val jug = UndirectedGraph.k2[UndirectedSparseGraph, String, Edge]
jug: axle.algebra.UndirectedGraph[edu.uci.ics.jung.graph.UndirectedSparseGraph[String,Edge],String,Edge] = axle.jung.package$$anon$8@29c0cb0d

scala> val a = "a"
a: String = a

scala> val b = "b"
b: String = b

scala> val c = "c"
c: String = c

scala> val d = "d"
d: String = d

scala> val ug = jug.make(List(a, b, c, d),
     |   List(
     |     (a, b, new Edge),
     |     (b, c, new Edge),
     |     (c, d, new Edge),
     |     (d, a, new Edge),
     |     (a, c, new Edge),
     |     (b, d, new Edge)))
ug: edu.uci.ics.jung.graph.UndirectedSparseGraph[String,Edge] =
Vertices:a,b,c,d
Edges:Edge@390ba498[c,d] Edge@1c44d4e7[b,d] Edge@30466213[a,c] Edge@305ab3e[d,a] Edge@6bbb0964[a,b] Edge@405d8e62[b,c]
```

```scala
scala> ug.size
res8: Int = 4

scala> ug.findVertex(_ == "c").map(v => ug.neighbors(v))
res9: Option[Set[String]] = Some(Set(a, b, d))

scala> ug.findVertex(_ == "a").map(v => ug.neighbors(v))
res10: Option[Set[String]] = Some(Set(b, c, d))
```

Visualize the graph

```scala
scala> import axle.web._
import axle.web._

scala> svg(ug, "SimpleUndirectedGraph.svg")
java.lang.NoClassDefFoundError: edu/uci/ics/jung/visualization/DefaultVisualizationModel
  at axle.web.SVG$$anon$19.svg(SVG.scala:562)
  at axle.web.SVG$$anon$19.svg(SVG.scala:547)
  at axle.web.package$.svg(package.scala:26)
  ... 618 elided
Caused by: java.lang.ClassNotFoundException: edu.uci.ics.jung.visualization.DefaultVisualizationModel
  at java.net.URLClassLoader.findClass(URLClassLoader.java:381)
  at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
  at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
  ... 621 more
```

![undirected graph](../images/SimpleUndirectedGraph.svg)
