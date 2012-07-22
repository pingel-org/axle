
object gaO {

  import axle.ml.{ Species, GeneticAlgorithm }
  import axle.visualize._
  import util.Random

  type RG = (Int, Double, Double, Double, Double, Double, Double, Double)

  def randomInt(from: Int, to: Int) = math.floor((to - from + 1) * Random.nextDouble).toInt

  object RabbitSpecies extends Species[RG] {

    val pMutation = 0.003

    def random0() = randomInt(0, 2) // number eyes
    def random1() = 5 + 20 * Random.nextDouble() // speed
    def random2() = 1 + 4 * Random.nextDouble()
    def random3() = 3 + 10 * Random.nextDouble()
    def random4() = 10 + 5 * Random.nextDouble()
    def random5() = 2 + 2 * Random.nextDouble()
    def random6() = 3 + 5 * Random.nextDouble()
    def random7() = 2 + 10 * Random.nextDouble()

    def random(): RG = (random0(), random1(), random2(), random3(), random4(), random5(), random6(), random7())

    def fitness(g: RG) = 100.0 * g._1 + 2.2 * (1.1 * g._2 + 0.3 * g._3) +
      1.3 * (1.4 * g._5 - 3.1 * g._6 + 1.3 * g._7) - 1.4 * g._8

    def shouldMutate() = util.Random.nextDouble < pMutation

    def mate(left: RG, right: RG) = {
      val i = randomInt(0, 8)
      (
        if (shouldMutate()) { random0() } else { if (i < 1) { left._1 } else { right._1 } },
        if (shouldMutate()) { random1() } else { if (i < 2) { left._2 } else { right._2 } },
        if (shouldMutate()) { random2() } else { if (i < 3) { left._3 } else { right._3 } },
        if (shouldMutate()) { random3() } else { if (i < 4) { left._4 } else { right._4 } },
        if (shouldMutate()) { random4() } else { if (i < 5) { left._5 } else { right._5 } },
        if (shouldMutate()) { random5() } else { if (i < 6) { left._6 } else { right._6 } },
        if (shouldMutate()) { random6() } else { if (i < 7) { left._7 } else { right._7 } },
        if (shouldMutate()) { random7() } else { if (i < 8) { left._8 } else { right._8 } }
      )
    }

  }

  val ga = new GeneticAlgorithm(RabbitSpecies, populationSize = 1000, numGenerations = 100)

  val (population, (mins, maxs, aves)) = ga.run()

  new AxleFrame().add(new Plot(List("min" -> mins, "ave" -> aves, "max" -> maxs),
    connect = true, title = Some("GA Demo"), xAxis = 0.0, xAxisLabel = Some("generation"), yAxis = 0, yAxisLabel = Some("fitness")))

}
