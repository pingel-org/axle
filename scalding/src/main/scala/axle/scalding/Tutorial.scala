package axle.scalding

import com.twitter.scalding.Args
import com.twitter.scalding.TextLine
import com.twitter.scalding.Job
import com.twitter.scalding.Tsv

class Tutorial(args: Args) extends Job(args) {

  // simple mapping and projection

  TextLine("hello.txt")
    .read
    .map('line -> 'reversed) { line: String => line.reverse }
    .project('reversed)
    .write(TextLine("axleoutput0.txt"))

  // word count

  //  TextLine("hello.txt")
  //    .read
  //    .flatMap('line -> 'word) { line: String => line.split("\\s") }
  //    .groupBy('word) { group => group.size }
  //    .write(Tsv("wc.tsv"))

  // joining

  //  val scores = TextLine("words")
  //    .read
  //    .rename('offset, 'score)
  //    .map('line -> 'dictWord) { line: String => line.toLowerCase }
  //    .project('score, 'dictWord)
  //  // .write(Tsv("words.tsv"))
  //
  //  TextLine("hello.txt")
  //    .read
  //    .flatMap('line -> 'word) { line: String => line.split("\\s").map { _.toLowerCase } }
  //    .joinWithLarger('word -> 'dictWord, scores)
  //    .groupBy('line) { group => group.sum('score) }
  //    .write(Tsv("joinout.tsv"))

}
