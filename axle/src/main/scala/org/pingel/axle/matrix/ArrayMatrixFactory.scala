package org.pingel.axle.matrix

abstract class ArrayMatrixFactory extends MatrixFactory {

  type M = ArrayMatrixImpl

  type S = (Int, Int, Array[T])

  abstract class ArrayMatrixImpl(triple: (Int, Int, Array[T])) extends Matrix[T] {
    // def add(other: M) = pure(triple) // TODO
    // def getTriple() = triple
  }

//  def pure(double: (Int, Int)): M = {
//      var array = new Array[Set[T]](m * n)
//      for (i <- 0 until m; j <- 0 until n) {
//        array(i * m + j) = Set[T]()
//      }
//      new ArrayMatrixImpl(triple)
//  }

}
