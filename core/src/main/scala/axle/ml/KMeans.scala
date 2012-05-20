package axle.ml

object KMeans extends KMeans()

trait KMeans {

  import axle.matrix.JblasMatrixFactory._ // TODO: generalize
  type M[T] = JblasMatrix[T]

  def cluster[T](
    data: Seq[T],
    featureExtractor: T => List[Double],
    constructor: List[Double] => T,
    numFeatures: Int,
    K: Int,
    iterations: Int): KMeansClassifier[T] = {

    val X = matrix(
      data.length,
      numFeatures,
      data.flatMap(featureExtractor(_)).toArray).t

    val (scaledX, colMins, colRanges) = Utilities.scaleColumns(X)
    val (centroids, assignments) = clusterLA(scaledX, K, iterations)
    KMeansClassifier(featureExtractor, constructor, numFeatures, centroids, assignments, colMins, colRanges)
  }

  /**
   * assumes that X has already been normalized
   */

  def clusterLA(X: M[Double], K: Int, iterations: Int): (M[Double], M[Int]) = {

    val n = X.columns
    val m = X.rows

    assert(K < m)

    val centroids = rand[Double](K, n) // random initial K centroids μ in R^n (aka M)

    val assignments = rand[Int](m, 1) // TODO indexes of centroids closest to xi

    (0 until iterations).map(x => {
      (0 until m).map(i => {
        // TODO ci = index of centroid closest to xi
      })
      (0 until K).map(k => {
        // TODO μk = average of points assigned to cluster k
      })
    })

    (centroids, assignments)
  }

  case class KMeansClassifier[D](
    featureExtractor: D => List[Double],
    constructor: List[Double] => D,
    numFeatures: Int,
    centroids: M[Double],
    assignments: M[Int],
    colMins: M[Double],
    colRanges: M[Double]) {

    def classify(observation: D): D = {
      val featureList = featureExtractor(observation)
      val featureRowMatrix = matrix(1, featureList.length, featureList.toArray)
      // val scaledX = ones[Double](1, 1) +|+ (diag(colRanges).inv ⨯ (featureRowMatrix.subRowVector(colMins).t)).t
      // val scaledY = (scaledX ⨯ θ).scalar
      // (scaledY * yRange.scalar) + yMin.scalar
      val r = 0 // TODO !!
      constructor(0.until(centroids.columns).map(c => centroids(r, c)).toList) // would be nice to have a row(r).toList
    }

  }

}
