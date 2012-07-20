package axle.ml

trait Species[G] {

  def random(): G

  def fitness(genotype: G): Double

  def mate(left: G, right: G): G

}

// TODO: each generation should report min/max/ave fitness

class GeneticAlgorithm[G](species: Species[G], pCrossover: Double, pMutation: Double, populationSize: Int = 1000, numGenerations: Int = 100) {

  def initialPopulation(): Set[G] = (0 until populationSize).map(i => species.random()).toSet

  def live(population: Set[G]): Set[G] = {
    // TODO:
    population
  }

  def run() = (0 until numGenerations).foldLeft(initialPopulation())((pop: Set[G], i: Int) => live(pop))

}