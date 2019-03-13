package axle.algebra

trait SimilaritySpace[T, V] {

  def similarity(s1: T, s2: T): V
}
