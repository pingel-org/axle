package axle.pgm

import cats.kernel.Eq
import cats.kernel.Order
//import cats.implicits._

import spire.algebra.Field
import spire.algebra.Ring
// import spire.implicits.multiplicativeSemigroupOps
import spire.random.Dist
import spire.random.Generator

// import axle.algebra.RegionEq
import axle.algebra.Region
//import axle.algebra.RegionEmpty
//import axle.algebra.RegionAll
import axle.stats.Variable
//import axle.stats.Factor
import axle.stats.ProbabilityModel
//import axle.math.Π
//import axle.math.Σ

/**
 * The role of MonotypeBayesanNetwork is to add an additional type, C,
 * that represents the composite type of all Variables.
 * This allows a ProbabilityModel typeclass witness to be defined.
 * 
 */

class MonotypeBayesanNetwork[C, S, X, DG[_, _]](
  val bayesianNetwork: BayesianNetwork[S, X, DG[BayesianNetworkNode[S, X], Edge]],
  val select: (Variable[S], C) => S,
  val combine: (Vector[S]) => C)
  
object MonotypeBayesanNetwork {

  implicit def probabilityModelForMonotypeBayesanNetwork[I, DG[_, _]]: ProbabilityModel[({ type L[C, W] = MonotypeBayesanNetwork[C, I, W, DG] })#L] =
    new ProbabilityModel[({ type L[C, W] = MonotypeBayesanNetwork[C, I, W, DG] })#L] {

      def observe[A, V: Dist: Ring: Order](model: MonotypeBayesanNetwork[A, I, V, DG])(gen: Generator): A = {
        ???
      }

      def probabilityOf[A, V: Field](model: MonotypeBayesanNetwork[A,I,V,DG])(predicate: Region[A]): V =
        model
        .bayesianNetwork
        .jointProbabilityTable
        .probabilities
        .map({ case (vreq, p) => (model.combine(vreq.map(_.x)), p)})
        .filter({ case (a, p) => predicate.apply(a) })
        .toVector
        .map(_._2)
        .reduce(Field[V].plus)

      def filter[A, V: Field](model: MonotypeBayesanNetwork[A,I,V,DG])(predicate: Region[A]): MonotypeBayesanNetwork[A,I,V,DG] = ???

      def unit[A: Eq, V: Ring](a: A): MonotypeBayesanNetwork[A,I,V,DG] = ???

      def flatMap[A, B: Eq, V](model: MonotypeBayesanNetwork[A,I,V,DG])(f: A => MonotypeBayesanNetwork[B,I,V,DG]): MonotypeBayesanNetwork[B,I,V,DG] = ???

      def map[A, B: Eq, V](model: MonotypeBayesanNetwork[A,I,V,DG])(f: A => B): MonotypeBayesanNetwork[B,I,V,DG] = ???

      def redistribute[A: Eq, V: Ring](model: MonotypeBayesanNetwork[A,I,V,DG])(from: A, to: A, mass: V): MonotypeBayesanNetwork[A,I,V,DG] = ???

   }
}
