package axle

case class EnrichedIterable[T](ita: Iterable[T]) {

  def doubles: Seq[(T, T)] = ita.toIndexedSeq.permutations(2).map(d => (d(0), d(1))).toSeq

  def triples: Seq[(T, T, T)] = ita.toIndexedSeq.permutations(3).map(t => (t(0), t(1), t(2))).toSeq

  def ⨯[S](right: Iterable[S]) = for {
    x <- ita
    y <- right
  } yield (x, y)

}
