package axle.algebra

//import spire.algebra.Group
import spire.algebra.AbGroup
import spire.implicits._

case class Transform[N](t: N)(implicit group: AbGroup[N]) extends Bijection[N, N] {

  def apply(n: N): N = group.op(n, t)

  def unapply(n: N): N = group.opInverse(n, t)

}
