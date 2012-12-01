
object BayesianNetworkDemo {

  println("Bayesian Network Demo")                //> Bayesian Network Demo

  import collection._
  import axle.stats._
  import axle.visualize._
  import BayesianNetwork.{BayesianNetworkNode, JungDirectedGraphVertex}

  val bools = Some(Vector(true, false))           //> bools  : Some[scala.collection.immutable.Vector[Boolean]] = Some(Vector(true
                                                  //| , false))

  val B = new RandomVariable0("Burglary", bools, None)
                                                  //> B  : axle.stats.RandomVariable0[Boolean] = RandomVariable0(Burglary,Some(Vec
                                                  //| tor(true, false)),None)
  val E = new RandomVariable0("Earthquake", bools, None)
                                                  //> E  : axle.stats.RandomVariable0[Boolean] = RandomVariable0(Earthquake,Some(V
                                                  //| ector(true, false)),None)
  val A = new RandomVariable0("Alarm", bools, None)
                                                  //> A  : axle.stats.RandomVariable0[Boolean] = RandomVariable0(Alarm,Some(Vector
                                                  //| (true, false)),None)
  val J = new RandomVariable0("John Calls", bools, None)
                                                  //> J  : axle.stats.RandomVariable0[Boolean] = RandomVariable0(John Calls,Some(V
                                                  //| ector(true, false)),None)
  val M = new RandomVariable0("Mary Calls", bools, None)
                                                  //> M  : axle.stats.RandomVariable0[Boolean] = RandomVariable0(Mary Calls,Some(V
                                                  //| ector(true, false)),None)

  val bn = BayesianNetwork(
    "A sounds (due to Burglary or Earthquake) and John or Mary Call",
    List(BayesianNetworkNode(B,
      Factor(Vector(B), Map(
        List(B eq true) -> 0.001,
        List(B eq false) -> 0.999
      ))),
      BayesianNetworkNode(E,
        Factor(Vector(E), Map(
          List(E eq true) -> 0.002,
          List(E eq false) -> 0.998
        ))),
      BayesianNetworkNode(A,
        Factor(Vector(B, E, A), Map(
          List(B eq false, E eq false, A eq true) -> 0.001,
          List(B eq false, E eq false, A eq false) -> 0.999,
          List(B eq true, E eq false, A eq true) -> 0.94,
          List(B eq true, E eq false, A eq false) -> 0.06,
          List(B eq false, E eq true, A eq true) -> 0.29,
          List(B eq false, E eq true, A eq false) -> 0.71,
          List(B eq true, E eq true, A eq true) -> 0.95,
          List(B eq true, E eq true, A eq false) -> 0.05))),
      BayesianNetworkNode(J,
        Factor(Vector(A, J), Map(
          List(A eq true, J eq true) -> 0.9,
          List(A eq true, J eq false) -> 0.1,
          List(A eq false, J eq true) -> 0.05,
          List(A eq false, J eq false) -> 0.95
        ))),
      BayesianNetworkNode(M,
        Factor(Vector(A, M), Map(
          List(A eq true, M eq true) -> 0.7,
          List(A eq true, M eq false) -> 0.3,
          List(A eq false, M eq true) -> 0.01,
          List(A eq false, M eq false) -> 0.99
        )))),
    (vs: Seq[JungDirectedGraphVertex[BayesianNetworkNode]]) => vs match {
      case b :: e :: a :: j :: m :: Nil => List((b, a, ""), (e, a, ""), (a, j, ""), (a, m, ""))
    })                                            //> bn  : axle.stats.BayesianNetwork.BayesianNetwork = JungDirectedGraph(List(B
                                                  //| urglary
                                                  //| 
                                                  //| Burglary
                                                  //| true     0.001000
                                                  //| false    0.999000, Earthquake
                                                  //| 
                                                  //| Earthquake
                                                  //| true       0.002000
                                                  //| false      0.998000, Alarm
                                                  //| 
                                                  //| Burglary Earthquake Alarm
                                                  //| true     true       true  0.950000
                                                  //| true     true       false 0.050000
                                                  //| true     false      true  0.940000
                                                  //| true     false      false 0.060000
                                                  //| false    true       true  0.290000
                                                  //| false    true       false 0.710000
                                                  //| false    false      true  0.001000
                                                  //| false    false      false 0.999000, John Calls
                                                  //| 
                                                  //| Alarm John Calls
                                                  //| true  true       0.900000
                                                  //| true  false      0.100000
                                                  //| false true       0.050000
                                                  //| false false      0.950000, Mary Calls
                                                  //| 
                                                  //| Alarm Mary Calls
                                                  //| true  true       0.700000
                                                  //| true  false      0.300000
                                                  //| false true       0.010000
                                                  //| false false      0.990000),<function1>)
    
  // show(enComponentJungDirectedGraph(bn))

  val jpt = bn.jointProbabilityTable()            //> jpt  : axle.stats.Factor = Earthquake Burglary John Calls Mary Calls Alarm
                                                  //| true       true     true       true       true  0.000001
                                                  //| true       true     true       true       false 0.000000
                                                  //| true       true     true       false      true  0.000001
                                                  //| true       true     true       false      false 0.000000
                                                  //| true       true     false      true       true  0.000000
                                                  //| true       true     false      true       false 0.000000
                                                  //| true       true     false      false      true  0.000000
                                                  //| true       true     false      false      false 0.000000
                                                  //| true       false    true       true       true  0.000365
                                                  //| true       false    true       true       false 0.000001
                                                  //| true       false    true       false      true  0.000156
                                                  //| true       false    true       false      false 0.000070
                                                  //| true       false    false      true       true  0.000041
                                                  //| true       false    false      true       false 0.000013
                                                  //| true       false    false      false      true  0.000017
                                                  //| true       false    false      false      false 0.001334
                                                  //| false      true     true       true       true  0.000591
                                                  //| false      true     true       true       false 0.000000
                                                  //| false      true     true       false      true  0.000253
                                                  //| false      true     true       false      false 0.000003
                                                  //| false      true     false      true       true  0.000066
                                                  //| false      true     false      true       false 0.000001
                                                  //| false      true     false      false      true  0.000028
                                                  //| false      true     false      false      false 0.000056
                                                  //| false      false    true       true       true  0.000628
                                                  //| false      false    true       true       false 0.000498
                                                  //| false      false    true       false      true  0.000269
                                                  //| false      false    true       false      false 0.049302
                                                  //| false      false    false      true       true  0.000070
                                                  //| false      false    false      true       false 0.009462
                                                  //| false      false    false      false      true  0.000030
                                                  //| false      false    false      false      false 0.936743
  
  // jpt.Σ(M).Σ(J).Σ(A).Σ(B).Σ(E)

  jpt.sumOut(M).sumOut(J).sumOut(A).sumOut(B).sumOut(E)
                                                  //> res0: axle.stats.Factor = 
                                                  //|  1.000000

  (bn.cpt(A) * bn.cpt(B)) * bn.cpt(E)             //> res1: axle.stats.Factor = Burglary Earthquake Alarm
                                                  //| true     true       true  0.000002
                                                  //| true     true       false 0.000000
                                                  //| true     false      true  0.000938
                                                  //| true     false      false 0.000060
                                                  //| false    true       true  0.000579
                                                  //| false    true       false 0.001419
                                                  //| false    false      true  0.000997
                                                  //| false    false      false 0.996005

  bn.markovAssumptionsFor(M)                      //> res2: axle.stats.Independence = I({Mary Calls}, {Alarm}, {Earthquake, Burgl
                                                  //| ary, John Calls})
  
}