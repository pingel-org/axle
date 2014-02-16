package axle

trait MapReduce {

  def apply[D, K, V](data: Iterable[D], mapper: D => Seq[(K, V)], reducer: (V, V) => V): Map[K, V]

}

object ScalaMapReduce extends MapReduce {

  def apply[D, K, V](data: Iterable[D], mapper: D => Seq[(K, V)], reducer: (V, V) => V): Map[K, V] =
    data
      .flatMap(mapper)
      .groupBy(_._1)
      .map({ case (k, v) => (k, v.map(_._2).reduce(reducer)) })

}
