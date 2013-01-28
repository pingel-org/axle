package axle.algebra

trait MABC[M[_, _, _], A, B, C] {
  
  val value: M[A, B, C]

  def |+|(a2: M[A, B, C])(implicit m: Monoid[M[A, B, C]]) = m.mappend(value, a2)
  
}