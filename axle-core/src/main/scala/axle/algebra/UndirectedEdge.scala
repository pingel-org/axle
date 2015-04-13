package axle.algebra

trait UndirectedEdge[VP, EP] {

  def vertices: (Vertex[VP], Vertex[VP])

  def payload: EP
}
