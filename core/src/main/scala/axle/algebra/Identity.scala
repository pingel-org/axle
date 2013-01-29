package axle.algebra

trait Identity[A] {

  val value: A

  def |+|(a2: A)(implicit s: Semigroup[A]) = s.mappend(value, a2)

  def ===(other: A): Boolean = (value equals other)

}
