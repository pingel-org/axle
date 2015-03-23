package axle.algebra

import spire.algebra.Group
import spire.implicits._

case class Transform[N](t: N)(implicit group: Group[N]) extends Bijection[N, N] {

  def apply(n: N): N = group.op(t, n)

  def unapply(n: N): N = group.opInverse(t, n)

}
