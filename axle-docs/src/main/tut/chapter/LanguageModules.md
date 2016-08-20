
Language Modules
================

Natural-language-specific stop words, tokenization, stemming, etc. 

English
-------

Currently English is the only language module.  A language modules supports tokenization, stemming, and stop words.  The stemmer is from tartarus.org, which is released under a compatible BSD license.  (It is not yet available via Maven, so its source has been checked into the Axle github repo.)

Example

```tut
val text = """
Now we are engaged in a great civil war, testing whether that nation, or any nation,
so conceived and so dedicated, can long endure. We are met on a great battle-field of
that war. We have come to dedicate a portion of that field, as a final resting place
for those who here gave their lives that that nation might live. It is altogether
fitting and proper that we should do this.
"""
```

Usage

```tut
import axle.nlp.language.English

English.
  tokenize(text.toLowerCase).
  filterNot(English.stopWords.contains).
  map(English.stem).
  mkString(" ")
```
