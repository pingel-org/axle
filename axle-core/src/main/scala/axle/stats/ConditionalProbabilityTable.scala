package axle.stats

// TODO: division by zero
import axle._

class ConditionalProbabilityTable0[A](p: Map[A, Double]) extends Distribution0[A] {

  // def randomStream(): Stream[Double] = Stream.cons(math.random, randomStream())

  // TODO Is there a version of scanLeft that is more like a reduce?
  // This would allow me to avoid having to construct the initial dummy element
  val bars = p.scanLeft((null.asInstanceOf[A], 0.0))((x, y) => (y._1, x._2 + y._2))

  def observe(): A = {
    val r = math.random
    bars.find(_._2 > r).getOrElse(throw new Exception("malformed distribution"))._1
  }

  def probabilityOf(a: A): Double = p(a)
}

class ConditionalProbabilityTable2[A, G1, G2](p: Map[(G1, G2), Map[A, Double]]) extends Distribution2[A, G1, G2] {

  def observe(): A = ???

  def observe(gv1: G1, gv2: G2): A = ???

  def probabilityOf(a: A): Double = ???

  def probabilityOf(a: A, given1: Case[G1], given2: Case[G2]): Double = ???

}

