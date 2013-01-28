package axle.algebra

trait MABCD[M[_, _, _, _], A, B, C, D] {
  
  val value: M[A, B, C, D]

  def |+|(a2: M[A, B, C, D])(implicit m: Monoid[M[A, B, C, D]]) = m.mappend(value, a2)
  
}