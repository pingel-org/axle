
object statsO {

  import collection._
  import axle.visualize._
  import axle.quanta.Information._
  import axle.InformationTheory._
  import axle.stats._

  val fairFlip1 = coin()
  val fairFlip2 = coin()

  val p1 = P(fairFlip1 eq 'HEAD)()
  val p2 = P((fairFlip1 eq 'HEAD) | (fairFlip2 eq 'HEAD))()
  val p3 = P((fairFlip1 eq 'HEAD) ∧ (fairFlip2 eq 'HEAD))()
  val p4 = P((fairFlip1 eq 'HEAD) ∨ (fairFlip2 eq 'HEAD))()

  val biasedCoin = coin(0.9)

}
