
package axle.stats

import axle.stats.docalculus._
import org.specs2.mutable._

class CausalitySpec extends Specification {

  val bools = Some(Vector(true, false))

  //    def getStandardQuantity(m: CausalModel) = {
  //      val namer = new VariableNamer()
  //      val question = Set(m.getVariable("Y").nextVariable(namer))
  //      val given = Set[Variable]()
  //      val actions = Set(m.getVariable("X").nextVariable(namer))
  //      new CausalityProbability(question, given, actions)
  //    }
  //
  //    for (model <- getModels()) {
  //      println("is Markovian? " + model.isMarkovian())
  //      val yGivenDoX: Probability = getStandardQuantity(model)
  //      println(model.getName() + " identifies " + yGivenDoX.toString() + "? " + model.identifies(yGivenDoX))
  //    }

  class XorOrFunction(variable: RandomVariable[_], in1: RandomVariable[_],
    in2: RandomVariable[_], in3: RandomVariable[_])
    extends PFunction(variable, List(in1, in2, in3)) {

    // TODO
    //    def apply(m: CausalModel, memo: Case) = {
    //      val val1 = new java.lang.Boolean(memo.valueOf(in1).toString()).booleanValue()
    //      val val2 = new java.lang.Boolean(memo.valueOf(in2).toString()).booleanValue()
    //      val val3 = new java.lang.Boolean(memo.valueOf(in3).toString()).booleanValue()
    //      (val2 || val3) ^ val1 // ???
    //    }

  }

  "Homework Model 4" should {

    "work" in {

      def homework4Model(k: Int, p: Double) = {

        val result = new CausalModel("Homewor 4 model with k " + k + ", p = " + p)

        val zero = Option(null.asInstanceOf[(RandomVariable[_], RandomVariable[_], RandomVariable[_], RandomVariable[_])])
        
        (0 to k).foldLeft(zero)((previous, i) => {
          val ei = RandomVariable0("E" + i, bools)
          val epi = RandomVariable0("E'" + i, bools)
          val xi = RandomVariable0("X" + i, bools)
          val yi = RandomVariable0("Y" + i, bools)
          result += CausalModelNode(ei, false)
          result += CausalModelNode(epi, false)
          result += CausalModelNode(xi)
          result += CausalModelNode(yi)
          // TODO
          //          result.addFunction(new PFunction(ei, p))
          //          result.addFunction(new PFunction(epi, p))
          //          if (i == 0) {
          //            result.addFunction(new RandomBooleanFunction(xi, 0.25))
          //          } else {
          //            result.addFunction(new XorOrFunction(xi, oldE.get, oldX.get, oldY.get))
          //          }
          //          if (i == 0) {
          //            result.addFunction(new RandomBooleanFunction(yi, 0.25))
          //          } else {
          //            result.addFunction(new XorOrFunction(yi, oldEp.get, oldX.get, oldY.get))
          //          }
          Some((ei, epi, xi, yi))
        })
        result
      }

      val m = homework4Model(3, 0.4)
      // val m = homework4Model(5, 0.2)

      // TODO
      //      val table = m.sampleDistribution(4)
      //
      //      val x0 = m.getVariable("X0")
      //      val x5 = m.getVariable("X5")
      //      val S = table.separate(x0, x5)
      //
      //      val search = new InductiveCausation(table)
      //      if (true) {
      //        println(search.ic())
      //      } else {
      //        println(search.icstar())
      //      }

      1 must be equalTo (1)
    }
  }

  "Midterm Model 1" should {

    "work" in {

      val model = new CausalModel("Midterm Model 1")

      val U1 = RandomVariable0("U1", bools)
      val U2 = RandomVariable0("U2", bools)
      val U3 = RandomVariable0("U3", bools)
      val X1 = RandomVariable0("X1", bools)
      val X2 = RandomVariable0("X2", bools)
      val X3 = RandomVariable0("X3", bools)
      val X4 = RandomVariable0("X4", bools)
      val Y = RandomVariable0("Y", bools)

      model += CausalModelNode(U1, false)
      model += CausalModelNode(U2, false)
      model += CausalModelNode(U3, false)
      model += CausalModelNode(X1)
      model += CausalModelNode(X2)
      model += CausalModelNode(X3)
      model += CausalModelNode(X4)
      model += CausalModelNode(Y)

      model.addFunction(new PFunction(X1, List(U1)))
      model.addFunction(new PFunction(X2, List(X1, U2)))
      model.addFunction(new PFunction(X3, List(X2, U1, U3)))
      model.addFunction(new PFunction(X4, List(X3, U2)))
      model.addFunction(new PFunction(Y, List(X4, U3)))

      // TODO
      //      def getQuantity(namer: VariableNamer) = {
      //        // this returns the quantity which is involved in
      //        // the question: P(y|do{x1},do{x2},do{x3},do{x4})
      //        val question = Set(model.getVariable("Y").nextVariable(namer))
      //        val given = Set[Variable]()
      //        val actions = Set(
      //          getVariable("X1").nextVariable(namer),
      //          getVariable("X2").nextVariable(namer),
      //          getVariable("X3").nextVariable(namer),
      //          getVariable("X4").nextVariable(namer)
      //        )
      //        new Probability(question, given, actions)
      //      }
      //
      //      def getClose(namer: VariableNamer) = {
      //        val question = Set(getVariable("Y").nextVariable(namer))
      //        val given = Set[Variable]()
      //        val actions = Set(
      //          getVariable("X3").nextVariable(namer),
      //          getVariable("X4").nextVariable(namer)
      //        )
      //        new Probability(question, given, actions)
      //      }

      1 must be equalTo (1)
    }
  }

  "Midterm Model 2" should {
    "work" in {

      val model = new CausalModel("Midterm Model 2")

      val a = RandomVariable0("A", bools)
      val b = RandomVariable0("B", bools)
      val c = RandomVariable0("C", bools)
      val f = RandomVariable0("F", bools)
      val d = RandomVariable0("D", bools)
      val e = RandomVariable0("E", bools)

      model += CausalModelNode(a)
      model += CausalModelNode(b)
      model += CausalModelNode(c)
      model += CausalModelNode(d)
      model += CausalModelNode(e)
      model += CausalModelNode(f, false)

      model.addFunction(new PFunction(c, List(a, b)))
      model.addFunction(new PFunction(d, List(c, f)))
      model.addFunction(new PFunction(e, List(d, f)))

      // TODO
      //      val distribution = new PerfectDistribution(this)
      //      val search = new InductiveCausation(distribution)
      //      val pdg = search.ic()

      1 must be equalTo (1)
    }
  }

  "3.8a" should {
    "work" in {
      val model = new CausalModel("3.8a")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model.addFunction(new PFunction(Y, List(X)))
    }
    1 must be equalTo (1)
  }

  "3.8b" should {
    "work" in {
      val model = new CausalModel("3.8b")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z = RandomVariable0("Z")
      val U = RandomVariable0("U")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z)
      model += CausalModelNode(U)
      model.addFunction(new PFunction(Y, List(X, Z, U)))
      model.addFunction(new PFunction(Z, List(X, U)))
      1 must be equalTo (1)
    }
  }

  "3.8c" should {
    "work" in {
      val model = new CausalModel("3.8c")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z = RandomVariable0("Z")
      val U = RandomVariable0("U")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z)
      model += CausalModelNode(U, false)
      model.addFunction(new PFunction(X, List(Z)))
      model.addFunction(new PFunction(Y, List(X, Z, U)))
      model.addFunction(new PFunction(Z, List(U)))
      1 must be equalTo (1)
    }
  }

  "3.8d" should {
    "work" in {

      val model = new CausalModel("3.8d")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z = RandomVariable0("Z")
      val U = RandomVariable0("U")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z)
      model += CausalModelNode(U, false)
      model.addFunction(new PFunction(X, List(Z, U)))
      model.addFunction(new PFunction(Y, List(X, Z)))
      model.addFunction(new PFunction(Z, List(U)))

      1 must be equalTo (1)
    }
  }

  "3.8e" should {
    "work" in {
      val model = new CausalModel("3.8e")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z = RandomVariable0("Z")
      val U = RandomVariable0("U")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z)
      model += CausalModelNode(U, false)
      model.addFunction(new PFunction(X, List(U)))
      model.addFunction(new PFunction(Y, List(Z, U)))
      model.addFunction(new PFunction(Z, List(X)))
      1 must be equalTo (1)
    }
  }

  "3.8f" should {
    "work" in {
      val model = new CausalModel("3.8f")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z1 = RandomVariable0("Z1")
      val Z2 = RandomVariable0("Z2")
      val U1 = RandomVariable0("U1")
      val U2 = RandomVariable0("U2")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z1)
      model += CausalModelNode(Z2)
      model += CausalModelNode(U1, false)
      model += CausalModelNode(U2, false)
      model.addFunction(new PFunction(X, List(U1)))
      model.addFunction(new PFunction(Y, List(X, Z1, Z2, U2)))
      model.addFunction(new PFunction(Z1, List(X, U2)))
      model.addFunction(new PFunction(Z2, List(Z1, U1)))

      1 must be equalTo (1)
    }
  }

  "3.8g" should {
    "work" in {
      val model = new CausalModel("3.8g")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z1 = RandomVariable0("Z1")
      val Z2 = RandomVariable0("Z2")
      val Z3 = RandomVariable0("Z3")
      val U1 = RandomVariable0("U1")
      val U2 = RandomVariable0("U2")
      val U3 = RandomVariable0("U3")
      val U4 = RandomVariable0("U4")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z1)
      model += CausalModelNode(Z2)
      model += CausalModelNode(Z3)
      model += CausalModelNode(U1, false)
      model += CausalModelNode(U2, false)
      model += CausalModelNode(U3, false)
      model += CausalModelNode(U4, false)
      model.addFunction(new PFunction(X, List(Z2, U1, U2, U3)))
      model.addFunction(new PFunction(Y, List(Z1, Z3, U1, U4)))
      model.addFunction(new PFunction(Z1, List(X, Z2)))
      model.addFunction(new PFunction(Z2, List(U3, U4)))
      model.addFunction(new PFunction(Z3, List(Z2, U2)))
      1 must be equalTo (1)
    }
  }

  "3.9a" should {
    "work" in {
      val model = new CausalModel("3.9a")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val U1 = RandomVariable0("U1")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(U1, false)
      model.addFunction(new PFunction(X, List(U1)))
      model.addFunction(new PFunction(Y, List(X, U1)))
      1 must be equalTo (1)
    }
  }

  "3.9b" should {
    "work" in {
      val model = new CausalModel("3.9b")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z = RandomVariable0("Z")
      val U1 = RandomVariable0("U1")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z)
      model += CausalModelNode(U1, false)
      model.addFunction(new PFunction(X, List(U1)))
      model.addFunction(new PFunction(Z, List(X, U1)))
      model.addFunction(new PFunction(Y, List(Z)))
      1 must be equalTo (1)
    }
  }

  "3.9c" should {
    "work" in {
      val model = new CausalModel("3.9c")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z = RandomVariable0("Z")
      val U1 = RandomVariable0("U1")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z)
      model += CausalModelNode(U1, false)
      model.addFunction(new PFunction(X, List(U1)))
      model.addFunction(new PFunction(Y, List(X, Z)))
      model.addFunction(new PFunction(Z, List(X, U1)))
      1 must be equalTo (1)
    }
  }

  "3.9d" should {
    "work" in {
      val model = new CausalModel("3.9d")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z = RandomVariable0("Z")
      val U1 = RandomVariable0("U1")
      val U2 = RandomVariable0("U2")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z)
      model += CausalModelNode(U1, false)
      model += CausalModelNode(U2, false)
      model.addFunction(new PFunction(X, List(U1)))
      model.addFunction(new PFunction(Y, List(X, Z, U2)))
      model.addFunction(new PFunction(Z, List(U1, U2)))
      1 must be equalTo (1)
    }
  }

  "3.9e" should {
    "work" in {
      val model = new CausalModel("3.9e")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z = RandomVariable0("Z")
      val U1 = RandomVariable0("U1")
      val U2 = RandomVariable0("U2")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z)
      model += CausalModelNode(U1, false)
      model += CausalModelNode(U2, false)
      model.addFunction(new PFunction(X, List(Z, U1)))
      model.addFunction(new PFunction(Y, List(X, Z, U2)))
      model.addFunction(new PFunction(Z, List(U1, U2)))
      1 must be equalTo (1)
    }
  }

  "3.9f" should {
    "work" in {
      val model = new CausalModel("3.9f")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z = RandomVariable0("Z")
      val U1 = RandomVariable0("U1")
      val U2 = RandomVariable0("U2")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z)
      model += CausalModelNode(U1, false)
      model += CausalModelNode(U2, false)
      model.addFunction(new PFunction(X, List(U1)))
      model.addFunction(new PFunction(Z, List(X, U2)))
      model.addFunction(new PFunction(Y, List(Z, U1, U2)))
      1 must be equalTo (1)
    }
  }

  "3.9g" should {
    "work" in {
      val model = new CausalModel("3.9g")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z1 = RandomVariable0("Z1")
      val Z2 = RandomVariable0("Z2")
      val U1 = RandomVariable0("U1")
      val U2 = RandomVariable0("U2")
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z1)
      model += CausalModelNode(Z2)
      model += CausalModelNode(U1, false)
      model += CausalModelNode(U2, false)
      model.addFunction(new PFunction(X, List(U1)))
      model.addFunction(new PFunction(Z1, List(X, U2)))
      model.addFunction(new PFunction(Z2, List(U1, U2)))
      model.addFunction(new PFunction(Y, List(Z1, Z2)))
      1 must be equalTo (1)
    }
  }

  "3.9h" should {
    "work" in {
      val model = CausalModel("3.9h")
      val W = RandomVariable0("W")
      val X = RandomVariable0("X")
      val Y = RandomVariable0("Y")
      val Z = RandomVariable0("Z")
      val U1 = RandomVariable0("U1")
      val U2 = RandomVariable0("U2")
      val U3 = RandomVariable0("U3")
      val U4 = RandomVariable0("U4")
      model += CausalModelNode(W)
      model += CausalModelNode(X)
      model += CausalModelNode(Y)
      model += CausalModelNode(Z)
      model += CausalModelNode(U1, false)
      model += CausalModelNode(U2, false)
      model += CausalModelNode(U3, false)
      model += CausalModelNode(U4, false)
      model.addFunction(new PFunction(W, List(X, U3)))
      model.addFunction(new PFunction(X, List(Z, U1, U2)))
      model.addFunction(new PFunction(Y, List(W, U2, U4)))
      model.addFunction(new PFunction(Z, List(U1, U3, U4)))
      1 must be equalTo (1)
    }
  }

  "smoking model" should {
    "work" in {
      val model = new CausalModel("Smoking Model")

      val U = RandomVariable0("U")
      val X = RandomVariable0("X") // smoke
      val Z = RandomVariable0("Z") // tar
      val Y = RandomVariable0("Y") // cancer
      model += CausalModelNode(U, false)
      model += CausalModelNode(X)
      model += CausalModelNode(Z)
      model += CausalModelNode(Y)
      model.addFunction(new PFunction(Z, List(X)))
      model.addFunction(new PFunction(X, List(U)))
      model.addFunction(new PFunction(Y, List(Z, U)))

      // TODO
      //      def doTask1(model: CausalModel, namer: VariableNamer) = {
      //        val question = Set(model.getVariable("Z").nextVariable(namer))
      //        val given = Set[Variable]()
      //        val actions = Set(model.getVariable("X").nextVariable(namer))
      //        val task1 = new Probability(question, given, actions)
      //        println("task1: " + task1.toString())
      //        for (q <- ActionToObservation(task1, model, namer)) {
      //          println("after rule 2 application: " + q)
      //        }
      //      }
      //
      //      def doTask2(model: CausalModel, namer: VariableNamer) = {
      //
      //        val question = Set(model.getVariable("Y").nextVariable(namer))
      //        val given = Set[Variable]()
      //        val actions = Set(model.getVariable("Z").nextVariable(namer))
      //
      //        val task2 = new Probability(question, given, actions)
      //        println("task2: " + task2.toString())
      //
      //        println("Trying ActionToObservation")
      //        val result = ActionToObservation(task2, model, namer)
      //        result.map(q => {
      //          println("after rule 2 application: " + q)
      //        })
      //
      //        val e = task2.caseAnalysis(model.getVariable("X"), namer)
      //        println("after conditioning and summing over X:\n" + e)
      //
      //        val p = e.getExpression() // asInstanceOf[Product]
      //
      //        val former = p.getMultiplicand(0) // Probabiblity
      //        println("former = " + former)
      //
      //        for (q <- ActionToObservation(former, model, namer)) {
      //          println("after rule ActionToObservation application: " + q)
      //        }
      //
      //        val latter = p.getMultiplicand(1)
      //        println("latter = " + latter)
      //
      //        for (q <- DeleteAction(latter, model, namer)) {
      //          println("after rule DeleteAction application: " + q)
      //        }
      //      }
      //
      //      def doTask3(model: CausalModel, namer: VariableNamer) = {
      //
      //        val question = Set(model.getVariable("Y").nextVariable(namer))
      //
      //        val given = Set[Variable]()
      //
      //        val actions = Set(model.getVariable("X").nextVariable(namer))
      //
      //        val task3 = new Probability(question, given, actions)
      //        println("task3: " + task3.toString())
      //
      //        val s = task3.caseAnalysis(model.getVariable("Z"), namer)
      //        println("after summing over Z:")
      //        println(s)
      //
      //        val p = s.getExpression() // Product
      //
      //        val former = p.getMultiplicand(0) // Probabiblity
      //        println("former = " + former)
      //
      //        val result2 = ObservationToAction(former, model, namer)
      //        for (q <- result2) {
      //          println("after rule ObservationToAction application: " + q)
      //        }
      //
      //        val former2 = result2(0).asInstanceOf[Probability] // Probability
      //        println("former2 = " + former2)
      //
      //        for (q <- DeleteAction(former2, model, namer)) {
      //          println("after rule DeleteAction application: " + q)
      //        }
      //
      //        println("latter = " + p.getMultiplicand(1)) // Probabiblity
      //        // see task 1
      //      }

      // doTask1(this)
      // doTask2(this)
      // doTask3(this, new VariableNamer())

      1 must be equalTo (1)

    }
  }

}
