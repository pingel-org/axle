package axle.scalding

import com.twitter.scalding._

import TDsl._

class TypedTutorial(args: Args) extends Job(args) {

  // Annotating a stream of birds

//  object Bird {
//    def fromTuple(t: (Double, Double)): Bird = Bird(t._1, t._2)
//  }
//
//  case class Bird(weight: Double, height: Double) {
//    def toTuple: (Double, Double) = (weight, height)
//  }
//
//  TypedTsv[(Double, Double)]("birds.tsv", ('weight, 'height)).map { Bird.fromTuple(_) }
//    .map { bird => (bird.weight, bird.height, bird.weight * bird.height) }
//    .write(('weight, 'height, 'foo), Tsv("birdfoo.txt"))

  // Word Count

//  case class Line(s: String) {
//    def toTuple(): (String) = (s)
//    def words() = {
//      // println("s = " + s)
//      if (s != null) s.split("\\s").toList else List()
//    }
//  }
//
//  val pipe =
//    TypedTsv[(String)]("hello.txt", ('line)).map { Line(_) }
//      .flatMap(_.words.map(w => (w, 1)))
//      .groupBy((wc: (String, Int)) => wc._1)
//      .reduce((u, v) => (u._1, u._2 + v._2))
//      .map(kv => (kv._1, kv._2._2))
//      .write(TextLine("typedwc.txt"))

  // join

  val scores = TextLine("words")
    .read
    .rename('offset, 'score)
    .map('line -> 'dictWord) { line: String => line.toLowerCase }
    .project('score, 'dictWord)

  //  TextLine("hello.txt")
  //    .read
  //    .flatMap('line -> 'word) { line: String => line.split("\\s").map { _.toLowerCase } }
  //    .joinWithLarger('word -> 'dictWord, scores)
  //    .groupBy('line) { group => group.sum('score) }
  //    .write(Tsv("joinout.tsv"))

}