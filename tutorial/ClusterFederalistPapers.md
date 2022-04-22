---
layout: page
title: Clusters Federalist Papers with k-Means
permalink: /tutorial/cluster_federalist_papers_k_means/
---

Imports

```scala
import axle.data.FederalistPapers
import FederalistPapers.Article
```

Download (and cache) the Federalist articles downloader:

```scala
val ec = scala.concurrent.ExecutionContext.global
// ec: concurrent.ExecutionContextExecutor = scala.concurrent.impl.ExecutionContextImpl$$anon$3@6dc22cea[Running, parallelism = 6, size = 2, active = 0, running = 0, steals = 3, tasks = 0, submissions = 0]
val blocker = cats.effect.Blocker.liftExecutionContext(ec)
// blocker: cats.effect.Blocker = cats.effect.Blocker@6dc22cea
implicit val cs = cats.effect.IO.contextShift(ec)
// cs: cats.effect.ContextShift[cats.effect.IO] = cats.effect.internals.IOContextShift@7d03f3a7

val articlesIO = FederalistPapers.articles[cats.effect.IO](blocker)
// articlesIO: cats.effect.IO[List[Article]] = Map(
//   source = Async(
//     k = cats.effect.internals.IOBracket$$$Lambda$9964/0x0000000802619898@15247f25,
//     trampolineAfter = false
//   ),
//   f = axle.data.FederalistPapers$$$Lambda$9977/0x000000080261f118@571d9299,
//   index = 0
// )

val articles = articlesIO.unsafeRunSync()
// articles: List[Article] = List(
//   Article(
//     id = 1,
//     author = "HAMILTON",
//     text = """
// 
// To the People of the State of New York:
// 
// AFTER an unequivocal experience of the inefficacy of the
// subsisting federal government, you are called upon to deliberate on
// a new Constitution for the United States of America. The subject
// speaks its own importance; comprehending in its consequences
// nothing less than the existence of the UNION, the safety and welfare
// of the parts of which it is composed, the fate of an empire in many
// respects the most interesting in the world. It has been frequently
// remarked that it seems to have been reserved to the people of this
// country, by their conduct and example, to decide the important
// question, whether societies of men are really capable or not of
// establishing good government from reflection and choice, or whether
// they are forever destined to depend for their political
// constitutions on accident and force. If there be any truth in the
// remark, the crisis at which we are arrived may with propriety be
// regarded as the era in which that decision is to be made; and a
// wrong election of the part we shall act may, in this view, deserve
// to be considered as the general misfortune of mankind.
// 
// This idea will add the inducements of philanthropy to those of
// patriotism, to heighten the solicitude which all considerate and
// good men must feel for the event. Happy will it be if our choice
// should be directed by a judicious estimate of our true interests,
// unperplexed and unbiased by considerations not connected with the
// public good. But this is a thing more ardently to be wished than
// seriously to be expected. The plan offered to our deliberations
// affects too many particular interests, innovates upon too many local
// institutions, not to involve in its discussion a variety of objects
// foreign to its merits, and of views, passions and prejudices little
// favorable to the discovery of truth.
// 
// Among the most formidable of the obstacles which the new
// Constitution will have to encounter may readily be distinguished the
// obvious interest of a certain class of men in every State to resist
// all changes which may hazard a diminution of the power, emolument,
// and consequence of the offices they hold under the State
// establishments; and the perverted ambition of another class of men,
// who will either hope to aggrandize themselves by the confusions of
// their country, or will flatter themselves with fairer prospects of
// elevation from the subdivision of the empire into several partial
// confederacies than from its union under one government.
// 
// ...
```

The result is a `List[Article]`.  How many articles are there?

```scala
articles.size
// res0: Int = 86
```

Construct a `Corpus` object to assist with content analysis

```scala
import axle.nlp._
import axle.nlp.language.English

import spire.algebra.CRing
implicit val ringLong: CRing[Long] = spire.implicits.LongAlgebra
// ringLong: CRing[Long] = spire.std.LongAlgebra@2b4451ec

val corpus = Corpus[Vector, Long](articles.map(_.text).toVector, English)
// corpus: Corpus[Vector, Long] = Corpus(
//   documents = Vector(
//     """
// 
// To the People of the State of New York:
// 
// AFTER an unequivocal experience of the inefficacy of the
// subsisting federal government, you are called upon to deliberate on
// a new Constitution for the United States of America. The subject
// speaks its own importance; comprehending in its consequences
// nothing less than the existence of the UNION, the safety and welfare
// of the parts of which it is composed, the fate of an empire in many
// respects the most interesting in the world. It has been frequently
// remarked that it seems to have been reserved to the people of this
// country, by their conduct and example, to decide the important
// question, whether societies of men are really capable or not of
// establishing good government from reflection and choice, or whether
// they are forever destined to depend for their political
// constitutions on accident and force. If there be any truth in the
// remark, the crisis at which we are arrived may with propriety be
// regarded as the era in which that decision is to be made; and a
// wrong election of the part we shall act may, in this view, deserve
// to be considered as the general misfortune of mankind.
// 
// This idea will add the inducements of philanthropy to those of
// patriotism, to heighten the solicitude which all considerate and
// good men must feel for the event. Happy will it be if our choice
// should be directed by a judicious estimate of our true interests,
// unperplexed and unbiased by considerations not connected with the
// public good. But this is a thing more ardently to be wished than
// seriously to be expected. The plan offered to our deliberations
// affects too many particular interests, innovates upon too many local
// institutions, not to involve in its discussion a variety of objects
// foreign to its merits, and of views, passions and prejudices little
// favorable to the discovery of truth.
// 
// Among the most formidable of the obstacles which the new
// Constitution will have to encounter may readily be distinguished the
// obvious interest of a certain class of men in every State to resist
// all changes which may hazard a diminution of the power, emolument,
// and consequence of the offices they hold under the State
// establishments; and the perverted ambition of another class of men,
// who will either hope to aggrandize themselves by the confusions of
// their country, or will flatter themselves with fairer prospects of
// elevation from the subdivision of the empire into several partial
// confederacies than from its union under one government.
// 
// It is not, however, my design to dwell upon observations of this
// nature. I am well aware that it would be disingenuous to resolve
// ...
```

Define a feature extractor using top words and bigrams.

```scala
val frequentWords = corpus.wordsMoreFrequentThan(100)
// frequentWords: List[String] = List(
//   "the",
//   "of",
//   "to",
//   "and",
//   "in",
//   "a",
//   "be",
//   "that",
//   "it",
//   "is",
//   "which",
//   "by",
//   "as",
//   "this",
//   "would",
//   "have",
//   "will",
//   "for",
//   "or",
//   "not",
//   "their",
//   "with",
//   "from",
//   "are",
//   "on",
//   "they",
//   "an",
//   "states",
//   "government",
//   "may",
//   "been",
//   "state",
//   "all",
//   "but",
//   "its",
//   "other",
//   "people",
//   "power",
//   "has",
//   "no",
//   "more",
//   "at",
//   "if",
//   "than",
//   "any",
//   "them",
//   "one",
//   "those",
// ...

val topBigrams = corpus.topKBigrams(200)
// topBigrams: List[(String, String)] = List(
//   ("of", "the"),
//   ("to", "the"),
//   ("in", "the"),
//   ("to", "be"),
//   ("that", "the"),
//   ("it", "is"),
//   ("by", "the"),
//   ("of", "a"),
//   ("the", "people"),
//   ("on", "the"),
//   ("would", "be"),
//   ("will", "be"),
//   ("for", "the"),
//   ("from", "the"),
//   ("the", "state"),
//   ("may", "be"),
//   ("have", "been"),
//   ("and", "the"),
//   ("the", "same"),
//   ("in", "a"),
//   ("with", "the"),
//   ("the", "union"),
//   ("has", "been"),
//   ("of", "their"),
//   ("the", "states"),
//   ("of", "this"),
//   ("the", "constitution"),
//   ("as", "the"),
//   ("the", "federal"),
//   ("the", "government"),
//   ("power", "of"),
//   ("the", "national"),
//   ("the", "most"),
//   ("the", "other"),
//   ("which", "the"),
//   ("all", "the"),
//   ("the", "united"),
//   ("to", "a"),
//   ("united", "states"),
//   ("the", "executive"),
//   ("it", "will"),
//   ("the", "public"),
//   ("is", "to"),
//   ("ought", "to"),
//   ("in", "this"),
//   ("the", "power"),
//   ("and", "to"),
//   ("must", "be"),
// ...

val numDimensions = frequentWords.size + topBigrams.size
// numDimensions: Int = 403

import axle.syntax.talliable.talliableOps

def featureExtractor(fp: Article): List[Double] = {

  val tokens = English.tokenize(fp.text.toLowerCase)
  val wordCounts = tokens.tally[Long]
  val bigramCounts =  bigrams(tokens).tally[Long]
  val wordFeatures = frequentWords.map(wordCounts(_) + 0.1)
  val bigramFeatures = topBigrams.map(bigramCounts(_) + 0.1)
  wordFeatures ++ bigramFeatures
}
```

Place a `MetricSpace` implicitly in scope that defines the space in which to
measure similarity of Articles.

```scala
import spire.algebra._

import axle.algebra.distance.Euclidean

import org.jblas.DoubleMatrix
import axle.jblas.linearAlgebraDoubleMatrix

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

implicit val space = {
  implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
  implicit val inner = axle.jblas.rowVectorInnerProductSpace[Int, Int, Double](numDimensions)
  new Euclidean[DoubleMatrix, Double]
}
```

Create 4 clusters using k-Means

```scala
import axle.ml.KMeans
import axle.ml.PCAFeatureNormalizer
```

```scala
import cats.implicits._
import spire.random.Generator.rng

val normalizer = (PCAFeatureNormalizer[DoubleMatrix] _).curried.apply(0.98)
// normalizer: DoubleMatrix => PCAFeatureNormalizer[DoubleMatrix] = scala.Function2$$Lambda$10012/0x00000008025e7510@49b801e8

val classifier = KMeans[Article, List, DoubleMatrix](
  articles,
  N = numDimensions,
  featureExtractor,
  normalizer,
  K = 4,
  iterations = 100)(rng)
// classifier: KMeans[Article, List, DoubleMatrix] = KMeans(
//   data = List(
//     Article(
//       id = 1,
//       author = "HAMILTON",
//       text = """
// 
// To the People of the State of New York:
// 
// AFTER an unequivocal experience of the inefficacy of the
// subsisting federal government, you are called upon to deliberate on
// a new Constitution for the United States of America. The subject
// speaks its own importance; comprehending in its consequences
// nothing less than the existence of the UNION, the safety and welfare
// of the parts of which it is composed, the fate of an empire in many
// respects the most interesting in the world. It has been frequently
// remarked that it seems to have been reserved to the people of this
// country, by their conduct and example, to decide the important
// question, whether societies of men are really capable or not of
// establishing good government from reflection and choice, or whether
// they are forever destined to depend for their political
// constitutions on accident and force. If there be any truth in the
// remark, the crisis at which we are arrived may with propriety be
// regarded as the era in which that decision is to be made; and a
// wrong election of the part we shall act may, in this view, deserve
// to be considered as the general misfortune of mankind.
// 
// This idea will add the inducements of philanthropy to those of
// patriotism, to heighten the solicitude which all considerate and
// good men must feel for the event. Happy will it be if our choice
// should be directed by a judicious estimate of our true interests,
// unperplexed and unbiased by considerations not connected with the
// public good. But this is a thing more ardently to be wished than
// seriously to be expected. The plan offered to our deliberations
// affects too many particular interests, innovates upon too many local
// institutions, not to involve in its discussion a variety of objects
// foreign to its merits, and of views, passions and prejudices little
// favorable to the discovery of truth.
// 
// Among the most formidable of the obstacles which the new
// Constitution will have to encounter may readily be distinguished the
// obvious interest of a certain class of men in every State to resist
// all changes which may hazard a diminution of the power, emolument,
// and consequence of the offices they hold under the State
// establishments; and the perverted ambition of another class of men,
// who will either hope to aggrandize themselves by the confusions of
// their country, or will flatter themselves with fairer prospects of
// elevation from the subdivision of the empire into several partial
// confederacies than from its union under one government.
// ...
```

Show cluster vs author in a confusion matrix:

```scala
import axle.ml.ConfusionMatrix
```

```scala
val confusion = ConfusionMatrix[Article, Int, String, Vector, DoubleMatrix](
  classifier,
  articles.toVector,
  _.author,
  0 to 3)
// confusion: ConfusionMatrix[Article, Int, String, Vector, DoubleMatrix] = ConfusionMatrix(
//   classifier = KMeans(
//     data = List(
//       Article(
//         id = 1,
//         author = "HAMILTON",
//         text = """
// 
// To the People of the State of New York:
// 
// AFTER an unequivocal experience of the inefficacy of the
// subsisting federal government, you are called upon to deliberate on
// a new Constitution for the United States of America. The subject
// speaks its own importance; comprehending in its consequences
// nothing less than the existence of the UNION, the safety and welfare
// of the parts of which it is composed, the fate of an empire in many
// respects the most interesting in the world. It has been frequently
// remarked that it seems to have been reserved to the people of this
// country, by their conduct and example, to decide the important
// question, whether societies of men are really capable or not of
// establishing good government from reflection and choice, or whether
// they are forever destined to depend for their political
// constitutions on accident and force. If there be any truth in the
// remark, the crisis at which we are arrived may with propriety be
// regarded as the era in which that decision is to be made; and a
// wrong election of the part we shall act may, in this view, deserve
// to be considered as the general misfortune of mankind.
// 
// This idea will add the inducements of philanthropy to those of
// patriotism, to heighten the solicitude which all considerate and
// good men must feel for the event. Happy will it be if our choice
// should be directed by a judicious estimate of our true interests,
// unperplexed and unbiased by considerations not connected with the
// public good. But this is a thing more ardently to be wished than
// seriously to be expected. The plan offered to our deliberations
// affects too many particular interests, innovates upon too many local
// institutions, not to involve in its discussion a variety of objects
// foreign to its merits, and of views, passions and prejudices little
// favorable to the discovery of truth.
// 
// Among the most formidable of the obstacles which the new
// Constitution will have to encounter may readily be distinguished the
// obvious interest of a certain class of men in every State to resist
// all changes which may hazard a diminution of the power, emolument,
// and consequence of the offices they hold under the State
// establishments; and the perverted ambition of another class of men,
// who will either hope to aggrandize themselves by the confusions of
// their country, or will flatter themselves with fairer prospects of
// elevation from the subdivision of the empire into several partial
// ...

confusion.show
// res1: String = """ 7 12  0 33 : 52 HAMILTON
//  0  1  0  2 :  3 HAMILTON AND MADISON
//  7  1  3  4 : 15 MADISON
//  0  1  0  4 :  5 JAY
//  1  2  0  8 : 11 HAMILTON OR MADISON
// 
// 15 17  3 51
// """
```
