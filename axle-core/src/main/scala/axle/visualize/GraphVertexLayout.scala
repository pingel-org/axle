package axle.visualize

trait GraphVertexLayout[N, VP] {
  def x(v: VP): N
  def y(v: VP): N
}
