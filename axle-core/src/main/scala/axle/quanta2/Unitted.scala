package axle.quanta2

import spire.algebra.Field
import spire.algebra.Eq

trait Unitted[Q <: Quantum, N] { self =>

  implicit def fieldN: Field[N]

  implicit def eqN: Eq[N]

}
