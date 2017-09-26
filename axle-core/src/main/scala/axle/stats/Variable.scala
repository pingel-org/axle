package axle.stats

case class Variable[T](
    name: String,
    values: IndexedSeq[T]) {

  def is(t: T): CaseIs[T] = CaseIs(t, this)
}

object Variable {

  import cats.kernel.Eq
  import cats.implicits._

  implicit def eqVariable[T]: Eq[Variable[T]] =
    new Eq[Variable[T]] {
      def eqv(x: Variable[T], y: Variable[T]): Boolean = x.name === y.name
    }
}