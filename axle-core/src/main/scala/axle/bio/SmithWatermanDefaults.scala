package axle.bio

object SmithWatermanDefaults {

  def w(x: Char, y: Char, mismatchPenalty: Int): Int =
    if (x != y) {
      mismatchPenalty
    } else {
      2 // also see NeedlemanWunsch.Default.similarity
    }

  val mismatchPenalty = -1

  val gap = '-'

}
