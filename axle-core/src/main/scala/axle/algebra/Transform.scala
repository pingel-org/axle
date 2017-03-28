package axle.algebra

import spire.algebra.AbGroup

case class Transform[N](u: N)(implicit group: AbGroup[N]) extends Bijection[N, N] {

  def apply(v: N): N = group.combine(u, v)

  def unapply(v: N): N = group.combine(v, group.inverse(u))

}
