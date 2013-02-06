package axle.algebra

trait Zero[A] {

  def zero(): A

}

object Zero {

  implicit def IntZero = new Zero[Int] { def zero() = 0 }

  implicit def DoubleZero = new Zero[Double] { def zero() = 0.0 }

  implicit def StringZero = new Zero[String] { def zero() = "" }

  implicit def ListZero[A] = new Zero[List[A]] { def zero() = List[A]() }

  implicit def Tuple2Zero[R: Zero, S: Zero] = (∅[R], ∅[S])

  implicit def Tuple3Zero[R: Zero, S: Zero, T: Zero] = (∅[R], ∅[S], ∅[T])

  implicit def Tuple4Zero[R: Zero, S: Zero, T: Zero, U: Zero] = (∅[R], ∅[S], ∅[T], ∅[U])

}