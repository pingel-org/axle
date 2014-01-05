package axle.nlp

import axle._
import spire.implicits._
import spire.math._
import collection.GenSeq

object Document {

  implicit object textDocument extends Document[String] {

    def tokens(text: String): IndexedSeq[String] = language.English.tokenize(text)

    // TODO: Vector[(String, String)] would be better
    def bigrams(text: String): Vector[IndexedSeq[String]] = tokens(text).sliding(2).toVector

    def wordCounts(text: String): Map[String, Long] = tokens(text) tally

    def bigramCounts(text: String): Map[IndexedSeq[String], Long] = bigrams(text) tally

    def averageWordLength(text: String): Number = {
      val ts = tokens(text)
      ts.map(_.length).sum / ts.length.toDouble
    }

  }

}

trait Document[T] {

  def tokens(text: T): GenSeq[String]

  def bigrams(text: T): GenSeq[IndexedSeq[String]]

  def wordCounts(text: T): Map[String, Long]

  def bigramCounts(text: T): Map[IndexedSeq[String], Long]

  def averageWordLength(text: T): Number

}
