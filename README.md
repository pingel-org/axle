
pingel.org
==========

These projects focus on core programming concepts, languages, and patterns.
I am particularly interested in in domains like linguistics, artificial intelligence,
(interactive) data visualization, and Category Theory.

Some of these projects started their life as Java code.  But I have found that its 
unexpressive syntax and typesystem diminished the utility of this code as a study
aid.

After reading a paper on Scala in the spring of 2008, I began using it for
a personal project a year later.  I became convinced that Scala will do a much better job
of allowing me to express these concepts more concisely and discover deeper connections
among them.  Not only is Scala helping me express (and learn about) these subjects, but the
subjects are also forcing me to really understand my chosen language.

I have also embraced using unicode characters whenever there the original literature
uses them.  I would likely not do this for production code meant for the workplace, but
as the primary goal of this project is pedagogy, I think it's appropriate to use this
technique to minimize the cognitive overhead of switching between the printed literature
and the working Scala models.

Adam Pingel
<pingel@gmail.com>
January 2012

cattheory
---------

Notes I've taken while exploring the deeper functional, theoretical aspects of Scala.

It's clear that Haskell has been a strong influence on Scala and the functional
faction of its community, so some of that this is oriented towards mapping Haskell concepts
to Scala.

Other notes are directly related to Category Theory.  I first encountered "Category Theory"
by name when my advisor at UCLA suggested that some of my interests seemed similar.  At the time
I was about five years into a PhD program in computer science with a major field in programming
languages, and minor fields in linguistics and artificial intelligence.
I never finished that degree, but in the years since then I have come to realize that
Category Theory is in fact an important piece of what I was looking for.
Now that the pressure of
completing a degree is off I am taking my time to properly learn this subject.

The goal is to have working models of concepts that bear a stong resemblance to their
presentation in the original source material.  Eventually that will mean providing
typeset TeX output, as well as other visualizations for specific data types (such as
graphs).

axle
----

An attempt to implement a Haskell-like language as an internal DSL in Scala.
My goal is to make the examples in "Learn You a Haskell for Great Good" work
using a syntax as similar to Haskell as possible using Scala.


linguistics
-----------

Models the Gold Paradigm and Angluin's Language Learner, which
I encountered in a couple of Ed Stabler's graduate courses
on language evolution.

ai 
----------------------------

Artificial Intelligence

Code for Bayesian Networks and Causality.
It's currently not buildable.
Originally written in Java.
I am actively rewriting this code in Scala.

python2json
-----------

This is part of a larger project on source code
search algorithms.

This file will take any python 2.6 (or less) file
and return a json document that represents the
abstract syntax tree.  There are a couple of minor
problems with it, but for the most part it works.

Feel free to submit bug patches.

util
----

Mostly iterators like PowerSet and CrossProduct that were
interesting to write while learning about Java 5's generics.
The difficulty in expressing the difference between a
DirectedGraph and an UndirectedGraph with Java 5 is one of
the main reason I have gravitated recently to Scala -- its
algebraic data types can properly model this kind of
"family polymorphism".
