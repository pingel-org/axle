package axle.algebra

trait Identity[A] {
  
  val value: A
  
  def |+|(a2: A)(implicit m: Monoid[A]) = m.mappend(value, a2)

  def mzero[A : Monoid](): A = {
    val monoid = implicitly[Monoid[A]]
    monoid.mzero()
  }

  def ===(other: A): Boolean = this equals other // this.## == other.##  // TODO
  
}
