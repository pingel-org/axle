
package axle

// http://en.wikipedia.org/wiki/Algebraic_structure

package object algebra {

  implicit def toIdent[A](a: A): Identity[A] = new Identity[A] {
    val value = a
  }

  implicit def toMA[M[_], A](ma: M[A]): MA[M, A] = new MA[M, A] {
    val value = ma
  }

  implicit def toMAB[M[_, _], A, B](mab: M[A, B]): MAB[M, A, B] = new MAB[M, A, B] {
    val value = mab
  }

}
