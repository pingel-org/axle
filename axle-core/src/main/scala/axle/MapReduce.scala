package axle

trait MapReduce {

  def mapReduce[D, K, V](data: Iterator[D], mapper: D => Seq[(K, V)], reducer: (V, V) => V): Map[K, V]

  def count[D, K](dit: Iterator[D], tokenizer: D => Seq[K]): Map[K, Int] =
    mapReduce(
      dit,
      mapper = (d: D) => tokenizer(d).map((_, 1)),
      reducer = (x: Int, y: Int) => x + y)
  
}

object ScalaMapReduce extends MapReduce {

  def mapReduce[D, K, V](data: Iterator[D], mapper: D => Seq[(K, V)], reducer: (V, V) => V): Map[K, V] =
    data
      .flatMap(mapper(_))
      .toList // TODO inefficient
      .groupBy(_._1)
      .map({ case (k, v) => (k, v.map(_._2).reduce(reducer)) })

}
