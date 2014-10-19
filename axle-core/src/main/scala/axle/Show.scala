package axle

trait Show[T] {

  def text(t: T): String
}
