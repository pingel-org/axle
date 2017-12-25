package axle.algebra.distance

import spire.algebra.AdditiveAbGroup
import spire.algebra.InnerProductSpace
import spire.algebra.MetricSpace
import spire.algebra.NRoot
import spire.implicits.additiveGroupOps

/**
 * Euclidean space
 *
 * http://en.wikipedia.org/wiki/Euclidean_space
 *
 */

class Euclidean[M: AdditiveAbGroup, D: NRoot](
  implicit
  ips: InnerProductSpace[M, D])
  extends MetricSpace[M, D] {

  val normed = ips.normed

  def distance(x: M, y: M): D = normed.norm(x - y)

}
