package axle.stats

case class Variable[T](name: String) {

  def charWidth: Int = name.length
}

object Variable {

  import cats.kernel.Eq
  import cats.implicits._

  implicit def eqVariable[T]: Eq[Variable[T]] =
    (x, y) => x.name === y.name
}