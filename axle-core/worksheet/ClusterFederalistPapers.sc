
import axle.data.FederalistPapers._
import axle.nlp._
import axle.ml._
import axle.ml.distance._
import axle.matrix._
import spire.implicits._
import spire.algebra._
import KMeansModule._
import JblasMatrixModule._

object ClusterFederalistPapers {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val filename = "/Users/pingel/github-clones/axle/axle-core/src/main/data/federalist.txt"
                                                  //> filename  : String = /Users/pingel/github-clones/axle/axle-core/src/main/dat
                                                  //| a/federalist.txt

  val articles = parseArticles(filename)          //> articles  : List[axle.data.FederalistPapers.FederalistPaper] = List(Federali
                                                  //| stPaper(1,HAMILTON,
                                                  //| 
                                                  //| To the People of the State of New York:
                                                  //| 
                                                  //| AFTER an unequivocal experience of th
                                                  //| Output exceeds cutoff limit.

  articles.size                                   //> res0: Int = 86


  val corpus = new Corpus(articles)               //> corpus  : axle.nlp.Corpus[axle.data.FederalistPapers.FederalistPaper] = 
                                                  //| Corpus of 86 documents.
                                                  //| There are 1017 unique words used more than 20 time(s).
                                                  //| Top 10 words: the, of, to, and, 
                                                  //| Output exceeds cutoff limit.

  val topWords = corpus.topWords(20)              //> topWords  : List[String] = List(the, of, to, and, in, a, be, that, is, which
                                                  //| , it, by, as, have, would, will, or, for, The, not, this, their, with, are, 
                                                  //| from, on, an, government, they, ma
                                                  //| Output exceeds cutoff limit.
  val topBigrams = corpus.topBigrams(200)         //> topBigrams  : List[(String, String)] = List((of,the), (to,the), (in,the), (t
                                                  //| o,be), (that,the), (by,the), (of,a), (the,people), (would,be), (will,be), (o
                                                  //| n,the), (for,the), (from,the), (it
                                                  //| Output exceeds cutoff limit.

  val numDimensions = topWords.size + topBigrams.size
                                                  //> numDimensions  : Int = 1217
  implicit val space: MetricSpace[Matrix[Double], Double] = Euclidian(numDimensions)
                                                  //> space  : spire.algebra.MetricSpace[axle.matrix.JblasMatrixModule.Matrix[Doub
                                                  //| le],Double] = Euclidian(1217)

  def featureExtractor(fp: FederalistPaper) = {
    val doc = implicitly[Document[FederalistPaper]]
    val wordCounts = doc.wordCounts(fp)
    val bigramCounts = doc.bigramCounts(fp)
    val wordFeatures = topWords.map((w: String) => wordCounts(w).toDouble)
    val bigramFeatures = topBigrams.map((bg: (String, String)) => bigramCounts(Vector(bg._1, bg._2)).toDouble)
    wordFeatures ++ bigramFeatures
  }                                               //> featureExtractor: (fp: axle.data.FederalistPapers.FederalistPaper)List[Doub
                                                  //| le]

  val f = classifier(
    articles,
    N = numDimensions,
    featureExtractor,
    (xs: Seq[Double]) => FederalistPaper(0, "", "", ""),
    K = 4,
    iterations = 100)                             //> f  : axle.ml.KMeansModule.KMeansClassifier[axle.data.FederalistPapers.Feder
                                                  //| alistPaper] = <function1>

  f.confusionMatrix(articles, (p: FederalistPaper) => p.author)
                                                  //> res1: axle.ml.ConfusionMatrix[axle.data.FederalistPapers.FederalistPaper,In
                                                  //| t,String] =  1 47  0  4 : 52 HAMILTON
                                                  //|  0  3  0  0 :  3 HAMILTON AND MADISON
                                                  //|  0  5  0  0 :  5 JAY
                                                  //|  0
                                                  //| Output exceeds cutoff limit.

}