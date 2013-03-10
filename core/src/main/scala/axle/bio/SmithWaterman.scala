package axle.bio

/**
 *
 * http://en.wikipedia.org/wiki/Smith-Waterman_algorithm
 *
 */

object SmithWaterman {

  def similarity(x: Char, y: Char, gapPenalty: Int) =
    if (x == '-' || y == '-')
      gapPenalty
    else
      (x, y) match {
        case ('A', 'A') => 10
        case ('A', 'G') => -1
        case ('A', 'C') => -3
        case ('A', 'T') => -4
        case ('G', 'A') => -1
        case ('G', 'G') => 7
        case ('G', 'C') => -5
        case ('G', 'T') => -3
        case ('C', 'A') => -3
        case ('C', 'G') => -5
        case ('C', 'C') => 9
        case ('C', 'T') => 0
        case ('T', 'A') => -4
        case ('T', 'G') => -3
        case ('T', 'C') => 0
        case ('T', 'T') => 8
      }

  def alignmentScore(dna1: String, dna2: String, gapPenalty: Int = -5): Int = {
    assert(dna1.length == dna2.length)
    (0 until dna1.length).map(i => similarity(dna1(i), dna2(i), gapPenalty)).sum
  }

  // alignmentScore("AGACTAGTTAC", "CGA‒‒‒GACGT")

}