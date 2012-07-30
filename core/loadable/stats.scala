
object statsO {

  import collection._
  import axle.visualize._
  import axle.quanta.Information._
  import axle.InformationTheory._
  import axle.Statistics._

  val fairCoin = coin()

  val p1 = P(fairCoin eq 'HEAD)()
  val p2 = P((fairCoin eq 'HEAD) | (fairCoin eq 'HEAD))()
  val p3 = P((fairCoin eq 'HEAD) ∧ (fairCoin eq 'HEAD))()
  val p4 = P((fairCoin eq 'HEAD) ∨ (fairCoin eq 'HEAD))()

  val biasedCoin = coin(0.9)

}