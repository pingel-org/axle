package axle.matrix

object MatrixFactory extends MatrixFactory()

trait MatrixFactory {

  /**
   * Type Parameters:
   *
   * T element type
   * S storage type
   * M subtype of Matrix that is backed by storage S and has elements of type T
   */

  type M[T] <: Matrix[T]

  trait Matrix[T] {

    type S

    def getStorage: S
    
    def rows: Int
    def columns: Int
    def length: Int

    def valueAt(i: Int, j: Int): T
    def setValueAt(i: Int, j: Int, v: T): Unit

    def column(j: Int): M[T]
    def row(i: Int): M[T]

    def isEmpty(): Boolean
    def isRowVector(): Boolean
    def isColumnVector(): Boolean
    def isVector(): Boolean
    def isSquare(): Boolean
    def isScalar(): Boolean
    // resize
    // reshape

    def dup(): M[T]
    def negate(): M[T]
    def transpose(): M[T]
    def diag(): M[T]
    def invert(): M[T]
    def ceil(): M[T]
    def floor(): M[T]
    def log(): M[T]
    def log10(): M[T]
    def fullSVD(): (M[T], M[T], M[T]) // (U, S, V) such that A = U * diag(S) * V' // TODO: all Matrix[Double] ?
    // def truth(): M[Boolean]

    def pow(p: Double): M[T]

    def addScalar(x: T): M[T]
    def subtractScalar(x: T): M[T]
    def multiplyScalar(x: T): M[T]
    def divideScalar(x: T): M[T]
    def mulRow(i: Int, x: T): M[T]
    def mulColumn(i: Int, x: T): M[T]

    // Operations on pairs of matrices

    def addMatrix(other: M[T]): M[T]
    def subtractMatrix(other: M[T]): M[T]
    def multiplyMatrix(other: M[T]): M[T]
    def concatenateHorizontally(right: M[T]): M[T]
    def concatenateVertically(under: M[T]): M[T]
    def solve(B: M[T]): M[T] // returns X, where this == A and A x X = B

    // Operations on a matrix and a column/row vector

    def addRowVector(row: M[T]): M[T]
    def addColumnVector(column: M[T]): M[T]
    def subRowVector(row: M[T]): M[T]
    def subColumnVector(column: M[T]): M[T]

    // Operations on pair of matrices that return M[Boolean]

    def lt(other: M[T]): Matrix[Boolean]
    def le(other: M[T]): Matrix[Boolean]
    def gt(other: M[T]): Matrix[Boolean]
    def ge(other: M[T]): Matrix[Boolean]
    def eq(other: M[T]): Matrix[Boolean]
    def ne(other: M[T]): Matrix[Boolean]

    def and(other: M[T]): Matrix[Boolean]
    def or(other: M[T]): Matrix[Boolean]
    def xor(other: M[T]): Matrix[Boolean]
    def not(): Matrix[Boolean]

    // various mins and maxs

    def max(): T
    def argmax(): (Int, Int)
    def min(): T
    def argmin(): (Int, Int)
    def columnMins(): M[T]
    // def columnArgmins
    def columnMaxs(): M[T]
    // def columnArgmaxs

    // In-place versions

    def ceili(): Unit
    def floori(): Unit
    def powi(p: Double): Unit

    def addi(x: T): Unit
    def subtracti(x: T): Unit
    def multiplyi(x: T): Unit
    def dividei(x: T): Unit

    def addMatrixi(other: M[T]): Unit
    def subtractMatrixi(other: M[T]): Unit
    def addiRowVector(row: M[T]): Unit
    def addiColumnVector(column: M[T]): Unit
    def subiRowVector(row: M[T]): Unit
    def subiColumnVector(column: M[T]): Unit

    // aliases

    def t() = transpose()
    def tr() = transpose()
    def inv() = invert()

    def scalar() = {
      assert(isScalar)
      valueAt(0, 0)
    }

    def +(x: T) = addScalar(x)
    def +=(x: T) = addi(x)
    def +(other: M[T]) = addMatrix(other)
    def +=(other: M[T]) = addMatrixi(other)

    def -(x: T) = subtractScalar(x)
    def -=(x: T) = subtracti(x)
    def -(other: M[T]) = subtractMatrix(other)
    def -=(other: M[T]) = subtractMatrixi(other)

    def *(x: T) = multiplyScalar(x)
    def *=(x: T) = multiplyi(x)
    def ⨯(other: M[T]) = multiplyMatrix(other)
    def mm(other: M[T]) = multiplyMatrix(other)

    def /(x: T) = divideScalar(x)
    def /=(x: T) = dividei(x)

    def +|+(right: M[T]) = concatenateHorizontally(right)
    def +/+(under: M[T]) = concatenateVertically(under)

    def <(other: M[T]) = lt(other)
    def <=(other: M[T]) = le(other)
    def ≤(other: M[T]) = le(other)
    def >(other: M[T]) = gt(other)
    def >=(other: M[T]) = ge(other)
    def ≥(other: M[T]) = ge(other)
    def ==(other: M[T]) = eq(other)
    def !=(other: M[T]) = ne(other)
    def ≠(other: M[T]) = ne(other)
    def &(other: M[T]) = and(other)
    def ∧(other: M[T]) = and(other)
    def |(other: M[T]) = or(other)
    def ∨(other: M[T]) = or(other)
    def ⊕(other: M[T]) = xor(other)
    def ⊻(other: M[T]) = xor(other)
    def !() = not()
    def ~() = not()
    def ¬() = not()

  }

}
