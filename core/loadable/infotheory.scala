
object infotheoryO {

  import axle.stats._

  val biasedCoin = coin(0.9)
  println((0 until 100).map(i => biasedCoin.choose()).mkString(" "))

  val fairCoin = coin()
  println((0 until 100).map(i => fairCoin.choose()).mkString(" "))

  println("entropy of 90/10 coin: " + entropy(biasedCoin))
  println("entropy of 50/50 coin: " + entropy(fairCoin))

}
