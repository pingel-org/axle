
object grO {

  import axle.graph.JungUndirectedGraphFactory._
  import axle.visualize._

  val g = graph[String, String]()

  val a = g += "a"
  val b = g += "b"
  val c = g += "c"

  g += ((a, b), "hello")
  g += ((b, c), "world")
  g += ((c, a), "!")

  val frame = new AxleFrame()
  val vis = new JungUndirectedGraphVisualization()
  frame.add(vis.component(g))

}