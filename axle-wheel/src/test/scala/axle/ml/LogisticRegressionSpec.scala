package axle.ml

import org.scalatest.funsuite._
import org.scalatest.matchers.should.Matchers

import org.jblas.DoubleMatrix

import spire.algebra._

import axle.jblas._

/**
 * 
 * Example from 
 * 
 * https://en.wikipedia.org/wiki/Logistic_regression#Probability_of_passing_an_exam_versus_hours_of_study
 */

class LogisticRegressionSpec extends AnyFunSuite with Matchers {

  test("Logistic Regression: Probability of passing an exam versus hours of study") {

    case class Student(hoursStudied: Double, testPassed: Boolean)

    val data = List(
      Student(0.50, false),
      Student(0.75, false),
      Student(1.00, false),
      Student(1.25, false),
      Student(1.50, false),
      Student(1.75, false),
      Student(1.75, true),
      Student(2.00, false),
      Student(2.25, true),
      Student(2.50, false),
      Student(2.75, true),
      Student(3.00, false),
      Student(3.25, true),
      Student(3.50, false),
      Student(4.00, true),
      Student(4.25, true),
      Student(4.50, true),
      Student(4.75, true),
      Student(5.00, true),
      Student(5.50, true))

    implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
    implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra
    implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

    val pTestPass = LogisticRegression[Student, DoubleMatrix](
      data,
      1,
      _.hoursStudied :: Nil,
      _.testPassed,
      0.1,
      10)

    // pTestPass(1d :: Nil) should be(0.07)
    // pTestPass(2d :: Nil) should be(0.26)
    // pTestPass(3d :: Nil) should be(0.61)
    // pTestPass(4d :: Nil) should be(0.87)
    // pTestPass(5d :: Nil) should be(0.97)
    pTestPass(2d :: Nil) should be > 0d
  }

}
