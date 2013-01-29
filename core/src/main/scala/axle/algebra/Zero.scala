package axle.algebra

trait Zero[A] {

  def mzero(): A

}

object Zero {
  
  implicit def IntZero = new Zero[Int] { def mzero() = 0 }

  implicit def DoubleZero = new Zero[Double] { def mzero() = 0.0 }

  implicit def StringZero = new Zero[String] { def mzero() = "" }

  implicit def ListZero[A] = new Zero[List[A]] { def mzero() = List[A]() }

  implicit def Tuple2Zero[R: Zero, S: Zero] =
    (implicitly[Zero[R]].mzero, implicitly[Zero[S]].mzero)

  implicit def Tuple3Zero[R: Zero, S: Zero, T: Zero] =
    (implicitly[Zero[R]].mzero, implicitly[Zero[S]].mzero, implicitly[Zero[T]].mzero)

  implicit def Tuple4Zero[R: Zero, S: Zero, T: Zero, U: Zero] =
    (implicitly[Zero[R]].mzero, implicitly[Zero[S]].mzero, implicitly[Zero[T]].mzero, implicitly[Zero[U]].mzero)

}