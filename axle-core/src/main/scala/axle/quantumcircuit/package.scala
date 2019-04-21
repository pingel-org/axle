package axle

import cats.kernel.Eq
import cats.syntax.all._

import spire.algebra._
import spire.math._

import axle.algebra._
import axle.algebra.Binary
import axle.algebra.B0
import axle.algebra.B1
import axle.stats.ConditionalProbabilityTable
import axle.stats.Variable

package object quantumcircuit {

  implicit val eqInt: Eq[Int] = spire.implicits.IntAlgebra

  def unindexToDistribution[T: Ring](xs: Vector[Complex[T]]): ConditionalProbabilityTable[Vector[Binary], T] = {
    val m: Map[Vector[Binary], T] = xs.zipWithIndex.map({ case (x, i) =>
       (0 until xs.size).map({ j => if(i === j) B1 else B0 }).toVector -> (x*x).real
    }).toMap
    ConditionalProbabilityTable(m, Variable("Q"))
  }

  implicit class EnrichedVector[T](vector: Vector[T]) {

    def ⊗(other: Vector[T])(implicit multT: MultiplicativeSemigroup[T]): Vector[T] = 
      for {
        x <- vector
        y <- other
      } yield multT.times(x, y)

  }

}