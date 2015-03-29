package axle.jblas

import org.jblas.DoubleMatrix

import axle.algebra.FunctionPair

case class ConvertedJblasDoubleMatrix[T](jdm: DoubleMatrix)(implicit val fp: FunctionPair[Double, T])
