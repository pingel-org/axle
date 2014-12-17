package axle.algebra

trait Endofunctor[E, T] {

  def map(e: E)(f: T => T): E
}
