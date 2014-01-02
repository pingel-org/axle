package axle.ml

import collection.immutable.TreeMap
import util.Random.{ nextDouble, nextInt, nextBoolean }
import shapeless._
import poly._
import shapeless.ops.hlist._
import syntax.std.tuple._
import Zipper._

trait Species[G] {

  def random(): G

  def fitness(genotype: G): Double

}

case class GeneticAlgorithmLog[G](
  popLog: IndexedSeq[(G, Double)],
  mins: TreeMap[Int, Double],
  maxs: TreeMap[Int, Double],
  aves: TreeMap[Int, Double])

object GeneticAlgorithm {

  def apply[G <: HList, Z <: HList](populationSize: Int = 1000, numGenerations: Int = 100)(
    implicit species: Species[G],
    zipper: Zip.Aux[G :: G :: HNil, Z],
    mapper: Mapper[mixer.type, Z],
    mapperMutate: Mapper[mutator.type, Z]) =
    new GeneticAlgorithmC(populationSize, numGenerations)

  object mixer extends Poly1 {
    implicit def caseTuple[T] = at[(T, T)](t =>
      if (nextBoolean) t._2 else t._1)
  }

  object mater extends Poly1 {
    implicit def caseTuple[T] = at[(T, T, T)](t =>
      if (nextDouble < 0.03) t._3
      else if (nextBoolean) t._2
      else t._1)
  }

  object mutator extends Poly1 {
    implicit def caseTuple[T] = at[(T, T)](t => if (nextDouble < 0.03) t._2 else t._1)
  }

  class GeneticAlgorithmC[G <: HList, Z <: HList](
    populationSize: Int = 1000, numGenerations: Int = 100)(
      implicit species: Species[G],
      zipper: Zip.Aux[G :: G :: HNil, Z],
      mapperMix: Mapper[mixer.type, Z],
      mapperMutate: Mapper[mutator.type, Z]) {

    def initialPopulation(): IndexedSeq[(G, Double)] =
      (0 until populationSize).map(i => {
        val r = species.random()
        (r, species.fitness(r))
      })

    /**
     * There are many variations of produceChild.
     * The important components are:
     *
     * 1. Fitness-based selection
     * 2. Crossover / gene-swapping
     * 3. Mutation
     *
     */

    def crossover[Z <: HList](h1: G, h2: G)(
      implicit zipper: Zip.Aux[G :: G :: HNil, Z],
      mapper: Mapper[mixer.type, Z]) = (h1 zip h2) map mixer

    def mutate[Z <: HList](x: G, r: G)(
      implicit zipper: Zip.Aux[G :: G :: HNil, Z],
      mapper: Mapper[mutator.type, Z]) = (x zip r) map mutator

    def live(population: IndexedSeq[(G, Double)], fitnessLog: List[(Double, Double, Double)]): (IndexedSeq[(G, Double)], List[(Double, Double, Double)]) = {
      val nextGen = (0 until populationSize).map(i => {
        val (m1, m1f) = population(nextInt(population.size))
        val (m2, m2f) = population(nextInt(population.size))
        val (f, _) = population(nextInt(population.size))
        val m = if (m1f > m2f) { m1 } else { m2 }
        val crossed = crossover(m, f).asInstanceOf[G]
        val kid = mutate(crossed, species.random()).asInstanceOf[G] // TODO
        (kid, species.fitness(kid))
      })
      (nextGen, minMaxAve(nextGen) :: fitnessLog)
    }

    def minMaxAve(population: IndexedSeq[(G, Double)]): (Double, Double, Double) =
      (population.minBy(_._2)._2, population.maxBy(_._2)._2, population.map(_._2).sum / population.size)

    def run(): GeneticAlgorithmLog[G] = {
      val popLog = (0 until numGenerations)
        .foldLeft((initialPopulation(), List[(Double, Double, Double)]()))(
          (pl: (IndexedSeq[(G, Double)], List[(Double, Double, Double)]), i: Int) => live(pl._1, pl._2))
      val logs = popLog._2.reverse
      val mins = new TreeMap[Int, Double]() ++ (0 until logs.size).map(i => (i, logs(i)._1))
      val maxs = new TreeMap[Int, Double]() ++ (0 until logs.size).map(i => (i, logs(i)._2))
      val aves = new TreeMap[Int, Double]() ++ (0 until logs.size).map(i => (i, logs(i)._3))
      GeneticAlgorithmLog[G](popLog._1, mins, maxs, aves)
    }

  }

}
