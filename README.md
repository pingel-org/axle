
pingel.org
==========

A wide range of code I've written over the years, primarily for the JVM.

Adam Pingel
<pingel@gmail.com>
February 2011

python2json
-----------

This is part of a larger project on source code
search algorithms.

This file will take any python 2.6 (or less) file
and return a json document that represents the
abstract syntax tree.  There are a couple of minor
problems with it, but for the most part it works.

Feel free to submit bug patches.


Linguistics
-----------

Models the Gold Paradigm and Angluin's Language Learner, which
I encountered in a couple of Ed Stabler's graduate courses
on language evolution.

AI (Artificial Intelligence)
----------------------------

Code for Bayesian Networks and Causality.
It's currently not buildable.
I am actively rewriting this code in Scala.

Util
----

Mostly iterators like PowerSet and CrossProduct that were
interesting to write while learning about Java 5's generics.
The difficulty in expressing the difference between a
DirectedGraph and an UndirectedGraph with Java 5 is one of
the main reason I have gravitated recently to Scala -- its
algebraic data types can properly model this kind of
"family polymorphism".
