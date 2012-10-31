
object grO {

  import axle.graph._
  import axle.visualize._

  val g = JungDirectedGraph(
    List("a", "b", "c"),
    (vs: Seq[JungDirectedGraphVertex[String]]) => vs match {
      case a :: b :: c :: Nil => List((a, b, "hello"), (b, c, "world"), (c, a, "!"))
    }
  )

  show(g)

}
