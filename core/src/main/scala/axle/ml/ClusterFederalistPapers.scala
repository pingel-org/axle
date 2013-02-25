package axle.ml

import axle.lx._
import axle.ml.distance._

// TODO: date, venue (For, From, etc)
case class FederalistArticle(id: String, header: String, author: String, addressee: String, text: String)
  extends Document(text)

class FederalistPapers extends Corpus[FederalistArticle]() {

  lazy val filename = "federalist.txt"

  lazy val _documents = extractArticles()

  def documents() = _documents

  lazy val BookRE = """FEDERALIST\.? No. \d+\s+""".r
  lazy val ArticleRE = """(?s)(.*)\n+([A-Z ]+)(\n){3,}To ([\w ]+)[\:\.][\s\n]+(.*)""".r

  def extractArticles(): IndexedSeq[FederalistArticle] =
    (BookRE.split(io.Source.fromFile(filename).mkString("")).tail)
      .zipWithIndex.flatMap({
        case (article, i) => {
          val id = (i + 1).toString
          try {
            val ArticleRE(header, author, _, addressee, text) = article
            Some(FederalistArticle(id, header, author, addressee, text.replaceAll("\n", " ").toLowerCase))
          } catch {
            case e: Exception => {
              println("Exception during article " + id + ": " + e)
              None
            }
          }
        }
      })
      .filter(article => article.id != "71")

}

object ClusterFederalistPapers {

  import axle.matrix.JblasMatrixModule._
  import axle.ml.KMeansModule._
  import axle.ml.distance._

  // TODO:
  // * relative frequencies vs counts
  // * incorporate document frequencies (tf-idf)
  // * type/token ratio
  // * capitalization patterns
  // * average sentence length

  //  def features(): IndexedSeq[Double] = {
  //    val theCount = tokens.filter(_ == "the").length
  //    val peopleCount = tokens.filter(_ == "people").length
  //    val whichCount = tokens.filter(_ == "which").length
  //    Vector(theCount, peopleCount, whichCount)
  //  }

  def main(args: Array[String]): Unit = {

    val corpus = new FederalistPapers {}

    val k = 4
    val numIterations = 100
    val featureExtractor = (article: FederalistArticle) => article.features(corpus)
    val numFeatures = featureExtractor(corpus.documents()(0)).length
    val distance = Euclidian(numFeatures)
    val labelExtractor = (article: FederalistArticle) => article.author
    val constructor = (features: Seq[Double]) => ???

    val classifier =
      KMeansModule.Classifier(corpus.documents, numFeatures, featureExtractor, constructor, distance, k, numIterations)

    val confusionMatrix = classifier.confusionMatrix(corpus.documents, labelExtractor)

    println(confusionMatrix)
  }
}