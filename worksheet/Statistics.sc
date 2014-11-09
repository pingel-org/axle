
import axle.stats._

object Statistics {

  val fairCoin = coin()                           //> fairCoin  : axle.stats.RandomVariable0[Symbol] = RandomVariable0(coin,Some(Ve
                                                  //| ctor('HEAD, 'TAIL)),Some(axle.stats.ConditionalProbabilityTable0@3782da3d))
  val biasedCoin = coin(0.9)                      //> biasedCoin  : axle.stats.RandomVariable0[Symbol] = RandomVariable0(coin,Some(
                                                  //| Vector('HEAD, 'TAIL)),Some(axle.stats.ConditionalProbabilityTable0@266bade9))
                                                  //| 
  fairCoin.observe                                //> res0: Symbol = 'TAIL

  import collection._
  import axle.visualize._

  val d6a = die(6)                                //> d6a  : axle.stats.RandomVariable0[Int] = RandomVariable0(d6,Some(Range(1, 2,
                                                  //|  3, 4, 5, 6)),Some(axle.stats.ConditionalProbabilityTable0@781f6226))
  val d6b = die(6)                                //> d6b  : axle.stats.RandomVariable0[Int] = RandomVariable0(d6,Some(Range(1, 2,
                                                  //|  3, 4, 5, 6)),Some(axle.stats.ConditionalProbabilityTable0@5464ea66))

  val hist = new immutable.TreeMap[Int, Int]() ++ (0 until 10000).map(i => d6a.observe + d6b.observe).map(v => (v, 1)).groupBy(_._1).map({ case (k, v) => (k, v.map(_._2).sum) })
                                                  //> hist  : scala.collection.immutable.TreeMap[Int,Int] = Map(2 -> 284, 3 -> 565
                                                  //| , 4 -> 795, 5 -> 1082, 6 -> 1386, 7 -> 1726, 8 -> 1376, 9 -> 1077, 10 -> 877
                                                  //| , 11 -> 546, 12 -> 286)

  val plot = Plot(List(("count", hist)), connect = true, drawKey = false, xAxis = 0, yAxis = 0, title = Some("d6 + d6"))
                                                  //> plot  : axle.visualize.Plot[Int,Nothing,Int,Nothing] = Plot(List((count,Map(
                                                  //| 2 -> 284, 3 -> 565, 4 -> 795, 5 -> 1082, 6 -> 1386, 7 -> 1726, 8 -> 1376, 9 
                                                  //| -> 1077, 10 -> 877, 11 -> 546, 12 -> 286))),true,false,700,600,50,4,Some(d6 
                                                  //| + d6),0,None,0,None)

  // show(plot)

  utfD6().observe                                 //> res1: Symbol = '?

  // Probability

  val fairFlip1 = coin()                          //> fairFlip1  : axle.stats.RandomVariable0[Symbol] = RandomVariable0(coin,Some(
                                                  //| Vector('HEAD, 'TAIL)),Some(axle.stats.ConditionalProbabilityTable0@4cc39a20)
                                                  //| )
  val fairFlip2 = coin()                          //> fairFlip2  : axle.stats.RandomVariable0[Symbol] = RandomVariable0(coin,Some(
                                                  //| Vector('HEAD, 'TAIL)),Some(axle.stats.ConditionalProbabilityTable0@3dbbd23f)
                                                  //| )

  P(fairFlip1 eq 'HEAD)()                         //> res2: Double = 0.5

  P((fairFlip1 eq 'HEAD) and (fairFlip2 eq 'HEAD))() // ∧
                                                  //> res3: Double = 0.25

  P((fairFlip1 eq 'HEAD) or (fairFlip2 eq 'HEAD))() // ∨
                                                  //> res4: Double = 0.75

  P((fairFlip1 eq 'HEAD) | (fairFlip2 eq 'TAIL))()//> res5: Double = 0.5

  val utfD6a = utfD6()                            //> utfD6a  : axle.stats.RandomVariable0[Symbol] = RandomVariable0(UTF d6,Some(V
                                                  //| ector('?, '?, '?, '?, '?, '?)),Some(axle.stats.ConditionalProbabilityTable0@
                                                  //| 3598cb3d))
  val utfD6b = utfD6()                            //> utfD6b  : axle.stats.RandomVariable0[Symbol] = RandomVariable0(UTF d6,Some(V
                                                  //| ector('?, '?, '?, '?, '?, '?)),Some(axle.stats.ConditionalProbabilityTable0@
                                                  //| 42472d48))

  // P((utfD6a ne '⚃))()

  // P((utfD6a eq '⚃) and (utfD6b eq '⚃))()
 
}