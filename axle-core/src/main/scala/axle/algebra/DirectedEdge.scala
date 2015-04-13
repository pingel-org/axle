package axle.algebra

trait DirectedEdge[VP, EP] {

  def from: Vertex[VP]

  def to: Vertex[VP]

  def payload: EP
}
