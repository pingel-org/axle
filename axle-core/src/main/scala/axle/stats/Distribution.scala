package axle.stats

//import cats.Show
//import axle.string
//import cats.kernel.Eq

//object Distribution {
//
//  implicit def rvEq[A: Eq, N]: Eq[Distribution[A, N]] =
//    new Eq[Distribution[A, N]] {
//      def eqv(x: Distribution[A, N], y: Distribution[A, N]): Boolean = x equals y // TODO
//    }
//}
//
//trait Distribution[A, N] {
//
//  def name: String
//
//  def observe(): A
//
//  def probabilityOf(a: A): N
//
//  def charWidth(implicit sa: Show[A]): Int = (name.length :: values.map(a => string(a).length).toList).reduce(math.max)
//}
//
//trait Distribution0[A, N] extends Distribution[A, N] {
//
//  def map[B](f: A => B): Distribution0[B, N]
//
//  def flatMap[B](f: A => Distribution0[B, N]): Distribution0[B, N]
//
//  def toMap: Map[A, N] = values map { v => (v, probabilityOf(v)) } toMap
//
//  //  def σ: Real = {
//  //    val μ = Σ(values.map(xi => probabilityOf(xi) * xi))(identity)
//  //    (Σ(Xs.map(xi => p(xi) * ((xi - μ) ** 2)))(identity)).sqrt
//  //  }
//
//}
//
//object Distribution0 {
//
//  import cats.Eq
//  import cats.implicits._
//
//  implicit def eqDistribution0[A: Eq, N: Eq]: Eq[Distribution0[A, N]] = new Eq[Distribution0[A, N]] {
//
//    def eqv(x: Distribution0[A, N], y: Distribution0[A, N]): Boolean =
//      x.toMap === y.toMap
//
//  }
//}
//
//trait Distribution1[A, G1, N] extends Distribution[A, N] {
//
//  def observe(gv: G1): A
//
//  def probabilityOf(a: A, given: Case[G1, N]): N
//}
//
//trait Distribution2[A, G1, G2, N] extends Distribution[A, N] {
//
//  def observe(gv1: G1, gv2: G2): A
//
//  def probabilityOf(a: A, given1: Case[G1, N], given2: Case[G2, N]): N
//}
