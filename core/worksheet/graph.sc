
import axle.graph._
import JungDirectedGraph.JungDirectedGraphVertex
import JungUndirectedGraph.JungUndirectedGraphVertex
import NativeDirectedGraph.NativeDirectedGraphVertex
import NativeUndirectedGraph.NativeUndirectedGraphVertex

object graph {

  val dg = JungDirectedGraph(List("a", "b", "c", "d"),
    (vs: Seq[JungDirectedGraphVertex[String]]) => vs match {
      case a :: b :: c :: d :: Nil => List((a, b, ""), (b, c, ""), (c, d, ""), (d, a, ""), (a, c, ""), (b, d, ""))
    })                                            //> dg  : axle.graph.JungDirectedGraph.G[String,java.lang.String] = JungDirected
                                                  //| Graph(List(a, b, c, d),<function1>)

  dg.size                                         //> res0: Int = 4

  dg.findVertex(_.payload == "a").map(v => dg.successors(v).map(_.payload))
                                                  //> res1: Option[scala.collection.Set[String]] = Some(Set(c, b))

  dg.findVertex(_.payload == "c").map(v => dg.successors(v).map(_.payload))
                                                  //> res2: Option[scala.collection.Set[String]] = Some(Set(d))

  dg.findVertex(_.payload == "c").map(v => dg.predecessors(v).map(_.payload))
                                                  //> res3: Option[scala.collection.Set[String]] = Some(Set(a, b))

  dg.findVertex(_.payload == "c").map(v => dg.neighbors(v).map(_.payload))
                                                  //> res4: Option[scala.collection.Set[String]] = Some(Set(d, a, b))
  // TODO g.shortestPath(a, d).map( _.map( edge => (edge.dest.payload, edge.payload) ) )

  //----------------------------------------------------

  val ug = JungUndirectedGraph(List("a", "b", "c", "d"),
    (vs: Seq[JungUndirectedGraphVertex[String]]) => vs match {
      case a :: b :: c :: d :: Nil => List((a, b, ""), (b, c, ""), (c, d, ""), (d, a, ""), (a, c, ""), (b, d, ""))
    })                                            //> ug  : axle.graph.JungUndirectedGraph.G[String,java.lang.String] = JungUndir
                                                  //| ectedGraph(List(a, b, c, d),<function1>)

  ug.size                                         //> res5: Int = 4

  ug.findVertex(_.payload == "c").map(v => ug.neighbors(v).map(_.payload))
                                                  //> res6: Option[scala.collection.Set[String]] = Some(Set(b, a, d))

  ug.findVertex(_.payload == "a").map(v => ug.neighbors(v).map(_.payload))
                                                  //> res7: Option[scala.collection.Set[String]] = Some(Set(c, b, d))

  //----------------------------------------------------

  val ndg = NativeDirectedGraph(List("a", "b", "c", "d"),
    (vs: Seq[NativeDirectedGraphVertex[String]]) => vs match {
      case a :: b :: c :: d :: Nil => List((a, b, ""), (b, c, ""), (c, d, ""), (d, a, ""), (a, c, ""), (b, d, ""))
    })                                            //> ndg  : axle.graph.NativeDirectedGraph.NativeDirectedGraph[String,java.lang.
                                                  //| String] = NativeDirectedGraph(List(a, b, c, d),<function1>)

  ndg.size                                        //> res8: Int = 4

  ndg.findVertex(_.payload == "a").map(v => ndg.successors(v).map(_.payload))
                                                  //> res9: Option[scala.collection.Set[String]] = Some(Set(b, c))

  ndg.findVertex(_.payload == "c").map(v => ndg.successors(v).map(_.payload))
                                                  //> res10: Option[scala.collection.Set[String]] = Some(Set(d))

  ndg.findVertex(_.payload == "c").map(v => ndg.predecessors(v).map(_.payload))
                                                  //> res11: Option[scala.collection.Set[String]] = Some(Set(b, a))

  ndg.findVertex(_.payload == "c").map(v => ndg.neighbors(v).map(_.payload))
                                                  //> res12: Option[scala.collection.Set[String]] = Some(Set(b, a, d))

  //----------------------------------------------------

  val nug = NativeUndirectedGraph(List("a", "b", "c", "d"),
    (vs: Seq[NativeUndirectedGraphVertex[String]]) => vs match {
      case a :: b :: c :: d :: Nil => List((a, b, ""), (b, c, ""), (c, d, ""), (d, a, ""), (a, c, ""), (b, d, ""))
    })                                            //> nug  : axle.graph.NativeUndirectedGraph.G[String,java.lang.String] = Native
                                                  //| UndirectedGraph(List(a, b, c, d),<function1>)

  nug.size                                        //> res13: Int = 4

  nug.findVertex(_.payload == "c").map(v => nug.neighbors(v).map(_.payload))
                                                  //> res14: Option[scala.collection.Set[String]] = Some(Set())

  nug.findVertex(_.payload == "a").map(v => nug.neighbors(v).map(_.payload))
                                                  //> res15: Option[scala.collection.Set[String]] = Some(Set())

}