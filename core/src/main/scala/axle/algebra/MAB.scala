package axle.algebra

trait MAB[M[_, _], A, B] {
  
  val value: M[A, B]

}