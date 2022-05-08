# Text

Natural Langage Processing (NLP), Linguistics, and Programming Languages

## Language Modules

Natural-language-specific stop words, tokenization, stemming, etc.

### English

Currently English is the only language module.  A language modules supports tokenization, stemming, and stop words.  The stemmer is from tartarus.org, which is released under a compatible BSD license.  (It is not yet available via Maven, so its source has been checked into the Axle github repo.)

Example

```scala mdoc:silent
val text = """
Now we are engaged in a great civil war, testing whether that nation, or any nation,
so conceived and so dedicated, can long endure. We are met on a great battle-field of
that war. We have come to dedicate a portion of that field, as a final resting place
for those who here gave their lives that that nation might live. It is altogether
fitting and proper that we should do this.
"""
```

Usage

```scala mdoc
import axle.nlp.language.English

English.
  tokenize(text.toLowerCase).
  filterNot(English.stopWords.contains).
  map(English.stem).
  mkString(" ")
```

## Edit Distance

See the Wikipedia page on [Edit distance](https://en.wikipedia.org/wiki/Edit_distance)

### Levenshtein

See the Wikipedia page on [Levenshtein distance](https://en.wikipedia.org/wiki/Levenshtein_distance)

Imports and implicits

```scala mdoc:silent:reset
import org.jblas.DoubleMatrix

import cats.implicits._

import spire.algebra.Ring
import spire.algebra.NRoot

import axle._
import axle.nlp.Levenshtein
import axle.jblas._

implicit val ringInt: Ring[Int] = spire.implicits.IntAlgebra
implicit val nrootInt: NRoot[Int] = spire.implicits.IntAlgebra
implicit val laJblasInt = linearAlgebraDoubleMatrix[Int]
implicit val space = Levenshtein[IndexedSeq, Char, DoubleMatrix, Int]()
```

Usage

```scala mdoc
space.distance("the quick brown fox", "the quik brown fax")
```

Usage with spire's `distance` operator

Imports

```scala mdoc:silent
import axle.algebra.metricspaces.wrappedStringSpace
import spire.syntax.metricSpace.metricSpaceOps
```

Usage

```scala mdoc
"the quick brown fox" distance "the quik brown fax"

"the quick brown fox" distance "the quik brown fox"

"the quick brown fox" distance "the quick brown fox"
```

## Vector Space Model

See the Wikipedia page on [Vector space model](https://en.wikipedia.org/wiki/Vector_space_model)

### Example

```scala mdoc:silent:reset
val corpus = Vector(
    "a tall drink of water",
    "the tall dog drinks the water",
    "a quick brown fox jumps the other fox",
    "the lazy dog drinks",
    "the quick brown fox jumps over the lazy dog",
    "the fox and the dog are tall",
    "a fox and a dog are tall",
    "lorem ipsum dolor sit amet"
)
```

#### Unweighted Distance

The simplest application of the vector space model to documents is the unweighted space:

```scala mdoc:silent
import cats.implicits._

import spire.algebra.Field
import spire.algebra.NRoot

import axle.nlp.language.English
import axle.nlp.TermVectorizer

implicit val fieldDouble: Field[Double] = spire.implicits.DoubleAlgebra
implicit val nrootDouble: NRoot[Double] = spire.implicits.DoubleAlgebra

val vectorizer = TermVectorizer[Double](English.stopWords)
```

```scala mdoc
val v1 = vectorizer(corpus(1))

val v2 = vectorizer(corpus(2))
```

The object defines a `space` method, which returns a `spire.algebra.MetricSpace` for document vectors:

```scala mdoc:silent
import axle.nlp.UnweightedDocumentVectorSpace

implicit val unweighted = UnweightedDocumentVectorSpace().normed
```

```scala mdoc
unweighted.distance(v1, v2)

unweighted.distance(v1, v1)
```

Compute a "distance matrix" for a given set of vectors using the metric space:

```scala mdoc:silent
import axle.jblas._
import axle.algebra.DistanceMatrix

val dm = DistanceMatrix(corpus.map(vectorizer))
```

```scala mdoc
dm.distanceMatrix.show

dm.distanceMatrix.max
```

#### TF-IDF Distance

```scala mdoc:silent
import axle.nlp.TFIDFDocumentVectorSpace

val tfidf = TFIDFDocumentVectorSpace(corpus, vectorizer).normed
```

```scala mdoc
tfidf.distance(v1, v2)

tfidf.distance(v1, v1)
```

## Angluin Learner

Models [Dana Angluin](https://en.wikipedia.org/wiki/Dana_Angluin)'s Language Learner.

### Example: Baby Angluin Learner

Imports

```scala mdoc:silent:reset
import axle._
import axle.lx._
import Angluin._
```

Setup

```scala mdoc:silent
val mHi = Symbol("hi")
val mIm = Symbol("I'm")
val mYour = Symbol("your")
val mMother = Symbol("Mother")
val mShut = Symbol("shut")
val mUp = Symbol("up")

val Σ = Alphabet(Set(mHi, mIm, mYour, mMother, mShut, mUp))

val s1 = Expression(mHi :: mIm :: mYour :: mMother :: Nil)
val s2 = Expression(mShut :: mUp :: Nil)
val ℒ = Language(Set(s1, s2))

val T = Text(s1 :: ♯ :: ♯ :: s2 :: ♯ :: s2 :: s2 :: Nil)

val ɸ = memorizingLearner
```

Usage

```scala mdoc
import axle.algebra.lastOption

val outcome = lastOption(ɸ.guesses(T))

outcome.get.ℒ

ℒ

T

T.isFor(ℒ)
```

## Gold Paradigm

Models the Gold Paradigm.

### Example: Baby Gold Learner

Imports

```scala mdoc:silent:reset
import axle._
import axle.lx._
import GoldParadigm._
```

Setup

```scala mdoc:silent
val mHi = Morpheme("hi")
val mIm = Morpheme("I'm")
val mYour = Morpheme("your")
val mMother = Morpheme("Mother")
val mShut = Morpheme("shut")
val mUp = Morpheme("up")

val Σ = Vocabulary(Set(mHi, mIm, mYour, mMother, mShut, mUp))

val s1 = Expression(mHi :: mIm :: mYour :: mMother :: Nil)
val s2 = Expression(mShut :: mUp :: Nil)

val ℒ = Language(Set(s1, s2))

val T = Text(s1 :: ♯ :: ♯ :: s2 :: ♯ :: s2 :: s2 :: Nil)

val ɸ = memorizingLearner
```

Usage

```scala mdoc
import axle.algebra.lastOption

lastOption(ɸ.guesses(T)).get

ℒ

T

T.isFor(ℒ)
```

## Python Grammar

This is part of a larger project on source code search algorithms.

`python2json.py` will take any python 2.6 (or older) file and return a json document that represents the
abstract syntax tree.
There are a couple of minor problems with it, but for the most part it works.

As an example, let's say we have the following python in
example.py:

```python
x = 1 + 2
print x
```

Invoke the script like so to turn example.py into json:

```bash
python2json.py -f example.py
```

You can also provide the input via stdin:

```bash
cat example.py | python2json.py
```

I find it useful to chain this pretty-printer when debugging:

```bash
cat example.py | python2json.py | python -mjson.tool
```

The pretty-printed result in this case is:

```javascript
{
    "_lineno": null,
    "node": {
        "_lineno": null,
        "spread": [
            {
                "_lineno": 2,
                "expr": {
                    "_lineno": 2,
                    "left": {
                        "_lineno": 2,
                        "type": "Const",
                        "value": "1"
                    },
                    "right": {
                        "_lineno": 2,
                        "type": "Const",
                        "value": "2"
                    },
                    "type": "Add"
                },
                "nodes": [
                    {
                        "_lineno": 2,
                        "name": "x",
                        "type": "AssName"
                    }
                ], 
                "type": "Assign"
            }, 
            {
                "_lineno": 3,
                "nodes": [
                    {
                        "_lineno": 3,
                        "name": "x",
                        "type": "Name"
                    }
                ],
                "type": "Printnl"
            }
        ],
        "type": "Stmt"
    },
    "type": "Module"
}
```

## Future Work

### Python Grammar organization

* factor out `axle-ast-python`
* `axle-ast-python`

### AST

* move ast view xml (how is it able to refer to `xml.Node`?)
  * ast.view.AstNodeFormatter (xml.Utility.escape)
  * ast.view.AstNodeFormatterXhtmlLines
  * ast.view.AstNodeFormatterXhtml
* Tests for `axle.ast`
* Redo axle.ast.* (rm throws, more typesafe)
* `cats.effect` for `axle.ast.python2`

### Linguistics

* Nerod Partition
* Finish Angluin Learner
* Motivation for Gold Paradigm, Angluin Learner
