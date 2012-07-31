package axle.stats

trait Distribution[A] {
  def probabilityOf(a: A): Double
}

trait Distribution0[A] extends Distribution[A] {
  def choose(): A
}

trait Distribution1[A, G1] {
  def choose(gv: G1): A
  def probabilityOf(a: A, given: Case[G1]): Double
}

trait Distribution2[A, G1, G2] {
  def choose(gv1: G1, gv2: G2): A
  def probabilityOf(a: A, given1: Case[G1], given2: Case[G2]): Double
}
