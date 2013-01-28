package axle.algebra

trait MAB[M[_, _], A, B] {
  
  val value: M[A, B]

  def |+|(a2: M[A, B])(implicit m: Monoid[M[A, B]]) = m.mappend(value, a2)
  
}