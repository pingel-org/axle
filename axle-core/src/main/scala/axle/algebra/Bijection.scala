package axle.algebra

trait Bijection[A, B] extends Function1[A, B] {
  
  def apply(a: A): B
  
  def unapply(b: B): A
}
