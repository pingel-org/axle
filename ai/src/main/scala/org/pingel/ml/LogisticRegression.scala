

// package org.pingel.ml

import scalala.scalar._
import scalala.tensor.::
import scalala.tensor.mutable._
import scalala.tensor.dense._
import scalala.tensor.sparse._
import scalala.library.Library._
import scalala.library.LinearAlgebra._
import scalala.library.Statistics._
import scalala.library.Plotting._
import scalala.operators.Implicits._


// http://s3.amazonaws.com/mlclass-resources/docs/slides/Lecture4.pdf

/*
 * X is an m x n+1 matrix of m examples with n features
 *    the 0th column should be hard-coded to 1
 *    the n features should be scaled (ideally between -1 and 1)
 *
 * y is a m x 1 matrix of answers that correspond to the m examples
 * 
 * α is the learning rate
 *   increase to converge faster
 *   decrease if cost is not monotonically decreasing
 *
 * λ is the regularization parameter
 *
 * θ is a n+1 x 1 vector 
 *   initialize wherever
 * 
 */

// , λ: Double

object Regression {

  def scaleFeatures(X: DenseMatrix[Double], y: Vector[Double]) = {

    var xMax = Vector.zeros[Double](X.numCols).t
    (0 until X.numCols).map( c => xMax(c) = X(::,c).max )
    
    var xMin = Vector.zeros[Double](X.numCols).t
    (0 until X.numCols).map( c => xMin(c) = X(::,c).min )
    
    val xRange = xMax - xMin
    
    var Xscaled = X.copy
    (0 until X.numRows).map(
      r => (0 until X.numCols).map(
	c => Xscaled(r, c) = c match {
	  case 0 => 1.0
	  case _ => ( X(r, c) - xMin(c) ) / xRange(c)
	}
      )
    )

    val yMin = y.min
    val yMax = y.max
    val yRange = yMax - yMin

    var yScaled = Vector.zeros[Double](y.size)
    // var yScaled = y(::,0)

    (0 until y.size).map( r => { yScaled(r) = (y(r) - yMin)/yRange } )
    
    (Xscaled, yScaled)
  }

  def hypothesis(xi: VectorRow[Double], theta: DenseVectorCol[Double]) = xi * theta

  def cost(X: Matrix[Double], y: Vector[Double], theta: DenseVectorCol[Double], i: Int) = {
    hypothesis(X(i,::), theta) - y(i)
  }

  def gradientDescentUpdate(X: Matrix[Double], y: Vector[Double], theta: DenseVectorCol[Double]) = {
    (0 until X.numRows)
    .map( i => X(i,::) * cost(X, y, theta, i) )
    .foldLeft( DenseVector.zeros[Double](X.numCols) )(_+_) / X.numRows.toDouble
  }
  
  def gradientDescent(X: Matrix[Double], y: Vector[Double],
		      theta: DenseVectorCol[Double], alpha: Double, iterations: Int) = {

    (0 until iterations).map( k => {

      println("k = " + k)

      val dTheta = gradientDescentUpdate(X, y, theta)
      println("dTheta = " + dTheta)

      theta -= dTheta * alpha
      println("theta = " + theta)

      println
    })

    theta
  }

  // theta that optimally reduces squared cost
  def normalEquation(X: DenseMatrix[Double], y: DenseVectorCol[Double]) = inv(X.t * X) * X.t * y

}

object Example {

  import Regression._

  val X = Matrix(
    (1.0, 2104.0, 5.0, 1.0, 45.0),
    (1.0, 1416.0, 3.0, 2.0, 40.0),
    (1.0, 1534.0, 3.0, 2.0, 30.0),
    (1.0,  852.0, 2.0, 1.0, 36.0)
  )
  
  val y = Vector[Double](
    460.0,
    232.0,
    315.0,
    178.0
  )

  val (scaledX, scaledY) = scaleFeatures(X, y)

  val gdTheta = gradientDescent(scaledX, scaledY, DenseVector.ones[Double](X.numCols), 0.1, 10)

  val optimalTheta = normalEquation(scaledX, scaledY)
  
}
