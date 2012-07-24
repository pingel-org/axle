package axle

trait MapReduce {

  import collection._

  def mapReduce[D, K, V](data: Iterator[D], mapper: D => Seq[(K, V)], reducer: (V, V) => V): immutable.Map[K, V]

}

object ScalaMapReduce extends MapReduce {

  import collection._

  def mapReduce[D, K, V](data: Iterator[D], mapper: D => Seq[(K, V)], reducer: (V, V) => V): immutable.Map[K, V] =
    data
      .map(mapper(_))
      .flatMap(x => x)
      .toList // TODO inefficient
      .groupBy(_._1)
      .map(kv => (kv._1, kv._2.map(_._2).reduce(reducer)))

}
