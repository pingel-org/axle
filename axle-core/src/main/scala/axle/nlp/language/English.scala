package axle.nlp.language

import concurrent.duration._
import concurrent.Await
import org.tartarus.snowball.SnowballStemmer
import org.tartarus.snowball.ext.englishStemmer

/*
import axle.actor.Defaults._
import akka.actor._
import akka.pattern.ask

object StemmerProtocol {
  
  case class Stem(word: String)
}

class StemmerActor(stemmer: SnowballStemmer) extends Actor {

  import StemmerProtocol._

  def receive = {
    case Stem(word) => {
      stemmer.setCurrent(word)
      stemmer.stem
      sender ! stemmer.getCurrent
    }
  }

}

*/

object English {

  // From Lucene's list of stopwords:

  val stopWords = Set(
    "a", "an", "and", "are", "as", "at", "be", "but", "by",
    "for", "if", "in", "into", "is", "it",
    "no", "not", "of", "on", "or", "such",
    "that", "the", "their", "then", "there", "these",
    "they", "this", "to", "was", "will", "with"
  )

  def tokenize(s: String): IndexedSeq[String] = s
    .replaceAll("""([\?!()\";\|\[\]\.\,'])""", " ")
    .trim
    .split("\\s+")
    .toIndexedSeq

  val stemmer = new englishStemmer()

  def stem(word: String): String = {
    stemmer.setCurrent(word)
    stemmer.stem
    stemmer.getCurrent
  }

  // import StemmerProtocol._
  //
  // lazy val englishStemmerActor = system.actorOf(Props(new StemmerActor(new englishStemmer())))
  //
  // def stem(word: String): String =
  //   Await.result((englishStemmerActor ? Stem(word)).mapTo[String], 1.second)

}
