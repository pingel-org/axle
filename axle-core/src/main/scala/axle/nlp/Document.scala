package axle.nlp

import axle._
import spire.implicits._

class Document(text: String) {

  lazy val tokens = language.English.tokenize(text)

  lazy val bigrams = tokens.sliding(2).toVector
  
  lazy val wordCounts = tokens.countMap
  
  lazy val bigramCounts = bigrams.countMap

  lazy val averageWordLength = tokens.map(_.length).sum / tokens.length.toDouble

}
