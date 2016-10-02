package axle.ml

import org.specs2.mutable._
import axle.jblas._

class LogisticRegressionSpec extends Specification {

  "Logistic Regression" should {
    "work" in {

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

      import spire.implicits.DoubleAlgebra
      implicit val laJblasDouble = linearAlgebraDoubleMatrix[Double]

      val pTestPass = LogisticRegression(
        data,
        1,
        (s: Student) => (s.hoursStudied :: Nil),
        (s: Student) => s.testPassed,
        0.1,
        10)

      pTestPass(1d :: Nil) must be equalTo 0.07
      pTestPass(2d :: Nil) must be equalTo 0.26
      pTestPass(3d :: Nil) must be equalTo 0.61
      pTestPass(4d :: Nil) must be equalTo 0.87
      pTestPass(5d :: Nil) must be equalTo 0.97
    }
  }

}