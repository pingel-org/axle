package axle.stats

case class Variable[T](name: String) {

  def is(t: T): CaseIs[T] = CaseIs(t, this)

  def charWidth: Int = name.length
}

object Variable {

  import cats.kernel.Eq
  import cats.implicits._

  implicit def eqVariable[T]: Eq[Variable[T]] =
    (x, y) => x.name === y.name
}