package axle.ml

trait KMeans {

  import axle.matrix.JblasMatrixFactory._ // TODO: generalize
  type M[T] = JblasMatrix[T]

  // X is NOT left-padded with 1's for k-means clustering
  
  def cluster(K: Int, X: M[Double], iterations: Int): (M[Double], M[Double]) = {

    val n = X.columns
    val m = X.rows

    // assert: K < m
    
    // TODO: normalize X

    val centroids = rand[Double](K, n) // random initial K centroids μ in R^n (aka M)

    val C = rand[Double](1, 1) // TODO indexes of centroids closest to xi
    
    (0 until iterations).map(x => {
      (0 until m).map(i => {
    	  // TODO ci = index of centroid closest to xi
      })
      (0 until K).map(k => {
    	  // TODO μk = average of points assigned to cluster k
      })
    })

    (centroids, C)
  }

}

// http://en.wikipedia.org/wiki/Greek_alphabet