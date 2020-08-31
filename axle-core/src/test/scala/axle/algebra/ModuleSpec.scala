package axle.algebra

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers
import org.typelevel.discipline.Predicate
import org.typelevel.discipline.scalatest.Discipline

import spire.algebra._
import spire.math._
import spire.laws._
import spire.laws.arb._
import spire.implicits.rightModuleOps

class ModuleSpec() extends AnyFunSuite with Matchers with Discipline {

  {
    import axle.algebra.modules.realRationalModule
    implicit val predRational = Predicate.const[Rational](true)
    checkAll("Module[Real, Rational]", VectorSpaceLaws[Real, Rational].rightModule)
  }

  // Lawless "dogs"

  test("Module[Double, Double]") {
    implicit val module = axle.algebra.modules.doubleDoubleModule
    (2d :* 3d) should be(6d)
    module.negate(3d) should be(-3d)
    module.plus(3d, 3d) should be(6d)
    module.zero should be(0d)
  }

  test("Module[Double, Rational]") {
    implicit val module = axle.algebra.modules.doubleRationalModule
    (3d :* Rational(2)) should be(6d)
    module.negate(3d) should be(-3d)
    module.plus(3d, 3d) should be(6d)
    module.zero should be(0d)
  }

  test("Module[Float, Rational]") {
    implicit val module = axle.algebra.modules.floatRationalModule
    (3f :* Rational(2)) should be(6f)
    module.negate(3f) should be(-3f)
    module.plus(3f, 3f) should be(6f)
    module.zero should be(0f)
  }

  test("Module[Real, Double]") {
    implicit val module = axle.algebra.modules.realDoubleModule
    (Real(3) :* 2d) should be(Real(6))
    module.negate(Real(3)) should be(Real(-3))
    module.plus(Real(3), Real(3)) should be(Real(6))
    module.zero should be(Real(0))
  }

  test("Module[Double, Int]") {
    implicit val module = axle.algebra.modules.doubleIntModule
    implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
    (3d :* 2) should be(6d)
    module.negate(3d) should be(-3d)
    module.plus(3d, 3d) should be(6d)
    module.zero should be(0d)
  }
    
  test("Module[Float, Double]") {
    implicit val module = axle.algebra.modules.floatDoubleModule
    (3f :* 2d) should be(6f)
    module.negate(3f) should be(-3f)
    module.plus(3f, 3f) should be(6f)
    module.zero should be(0f)
  }

  test("Module[Rational, Double]") {
    implicit val module = axle.algebra.modules.rationalDoubleModule
    (Rational(3) :* 2d) should be(Rational(6))
    module.negate(Rational(3)) should be(Rational(-3))
    module.plus(Rational(3), Rational(3)) should be(Rational(6))
    module.zero should be(Rational(0))
  }


}
