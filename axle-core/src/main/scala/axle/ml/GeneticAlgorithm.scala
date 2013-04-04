package axle.ml

import collection._
import collection.immutable.TreeMap

trait Species[G] {

  def random(): G

  def fitness(genotype: G): Double

  def mate(left: G, right: G): G

}

case class GeneticAlgorithmLog[G](
  popLog: IndexedSeq[(G, Double)],
  mins: TreeMap[Int, Double],
  maxs: TreeMap[Int, Double],
  aves: TreeMap[Int, Double])

object GeneticAlgorithm {

  def apply[G](species: Species[G], populationSize: Int = 1000, numGenerations: Int = 100) = new GeneticAlgorithm(species, populationSize, numGenerations)
  
}
  
class GeneticAlgorithm[G](species: Species[G], populationSize: Int = 1000, numGenerations: Int = 100) {

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

  def produceChild(population: IndexedSeq[(G, Double)]): G = {
    val m1 = population(util.Random.nextInt(population.size))
    val m2 = population(util.Random.nextInt(population.size))
    val f = population(util.Random.nextInt(population.size))
    val m = if (m1._2 > m2._2) { m1 } else { m2 }
    species.mate(m._1, f._1)
  }

  def live(population: IndexedSeq[(G, Double)], fitnessLog: List[(Double, Double, Double)]): (IndexedSeq[(G, Double)], List[(Double, Double, Double)]) = {
    val nextGen = (0 until populationSize).map(i => {
      val child = produceChild(population)
      (child, species.fitness(child))
    })
    (nextGen, minMaxAve(nextGen) :: fitnessLog)
  }

  def minMaxAve(population: IndexedSeq[(G, Double)]): (Double, Double, Double) =
    (population.minBy(_._2)._2, population.maxBy(_._2)._2, population.map(_._2).sum / population.size)

  def run(): GeneticAlgorithmLog[G] = {
    val popLog = (0 until numGenerations)
      .foldLeft((initialPopulation(), List[(Double, Double, Double)]()))(
        (pl: (IndexedSeq[(G, Double)], List[(Double, Double, Double)]), i: Int) => live(pl._1, pl._2)
      )
    val logs = popLog._2.reverse
    val mins = new immutable.TreeMap[Int, Double]() ++ (0 until logs.size).map(i => (i, logs(i)._1))
    val maxs = new immutable.TreeMap[Int, Double]() ++ (0 until logs.size).map(i => (i, logs(i)._2))
    val aves = new immutable.TreeMap[Int, Double]() ++ (0 until logs.size).map(i => (i, logs(i)._3))
    GeneticAlgorithmLog[G](popLog._1, mins, maxs, aves)
  }

}
