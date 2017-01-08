package axle.algebra

import spire.algebra.AbGroup

case class Transform[N](t: N)(implicit group: AbGroup[N]) extends Bijection[N, N] {

  def apply(n: N): N = group.combine(n, t)

  def unapply(n: N): N = group.combine(t, group.inverse(n))

}
