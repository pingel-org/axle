
object gaO {

  import axle.ml.{ Species, GeneticAlgorithm }
  import util.Random

  type RG = (Int, Double, Double, Double, Double, Double, Double, Double)

  def randomInt(from: Int, to: Int) = math.floor((to - from + 1) * Random.nextDouble).toInt

  object RabbitSpecies extends Species[RG] {

    def random(): RG = (
      randomInt(0, 2), // num eyes
      5 + 20 * Random.nextDouble(), // speed
      1 + 4 * Random.nextDouble(),  // iron
      3 + 10 * Random.nextDouble(), // calcium
      10 + 5 * Random.nextDouble(), // phosphorus
      2 + 2 * Random.nextDouble(), // x
      3 + 5 * Random.nextDouble(), // y
      2 + 10 * Random.nextDouble() // z
    )

    def fitness(g: RG) = 100.0 * g._1 +
      2.2 * (1.1 * g._2 + 0.3 * g._3) +
      1.3 * (1.4 * g._5 - 3.1 * g._6 + 1.3 * g._7) +
      -1.4 * g._8

    def mate(left: RG, right: RG) = {
      val i = randomInt(0, 8)
      (
        if (i < 1) { left._1 } else { right._1 },
        if (i < 2) { left._2 } else { right._2 },
        if (i < 3) { left._3 } else { right._3 },
        if (i < 4) { left._4 } else { right._4 },
        if (i < 5) { left._5 } else { right._5 },
        if (i < 6) { left._6 } else { right._6 },
        if (i < 7) { left._7 } else { right._7 },
        if (i < 8) { left._8 } else { right._8 }
      )
    }
  }

  val ga = new GeneticAlgorithm(populationSize = 1000, numGenerations = 100, pCrossover = 0.3, pMutation = 0.01)

  ga.run()

}
