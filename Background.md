
Philosophy
==========

There is a lot to know.

In 1910 Russel and Whitehead published the first of three volumes of their
["Principia Mathematica"](http://en.wikipedia.org/wiki/Principia_Mathematica),
which was an attempt to "derive all mathematical truths from a well-defined set
of axioms and inference rules in symbolic logic. [...] 
A fourth volume on the foundations of geometry had been planned, but the authors admitted to intellectual exhaustion upon completion of the third."

Some [claim](http://en.wikipedia.org/wiki/John_von_Neumann) that John von Neumann
(who died in 1957) was the last human to master all branches of mathematics.
For decades, it has been clear to college freshmen planning a degree
that only a small portion of all knowledge could be studied, let alone mastered.

Disciplines like philosophy, mathematics, and physics promise
to offer general purpose languages and analytical frameworks that
make other more concrete or applied fields easier to cut through.
And yet without concrete examples along the way it can be difficult
to keep one's bearing among the maze of overlapping or even contradictory
theories.

Introduction
------------

Pingel.org models a set of formal subjects that the author has encountered
throughout his lifetime.
They take the form of functioning code that allows the reader to experiment
with alternative examples.

Graph Theory, for instance, arises repeatedly in subjects studied by 
computer scientists, mathematicians, operations researchers, and many others.
By providing a concrete, functional interface to Graph Theory, these
domains can be encoded directly in terms of the same shared library.
This allows the reader to more quickly understand similarities between
topics.

The primary goal of this project is to provide implementations of a range
of theories that enable experimentation and exploration, which can
ultimately lead to better understanding of complex subjects and the
connections between them.
Modern programming languages and tools are narrowing the gap between
implementation of an idea and the typeset representation of an idea.
To the greatest extent possible, the programs created with this 
code should resemble the subjects as they are described in the literature.
Unicode characters are used whenever there is substantial pre-existing literature
that establishes symbolic conventions.

On the spectrum of "library" to "language", this project prefers the latter.
The expressive syntax and type-system of the host language -- Scala --
is leveraged to make the space of valid constructions as close as possible to the set of
programs that are well-formed under the theories that the models claim to represent.

Although the primary aim of this code is education, scalability and performance
are secondary goals.

Theory
------

There are a couple of reasons why this kind of project is difficult.

The first is that without the resources to hire engineers to craft new
languages for each domain, a project like this must build on a highly expressive
host language.
Such languages are a relatively new phenomenon.

The other reason is more fundamental.
In software engineering, it's called the "tyranny of the dominant decomposition".
There are many equivalent ways to encode the same behavior, no matter what
language is used.
Choosing a particular representation for a program will *always* make other
programs that share code less well-tuned in that language.

For example: In choosing the "matrix" is a core concept, Matlab necessarily makes
other data types more difficult to support.

The constraints are both theoretical and technical.
This is an active area of computer science research
(eg [example](http://www.cs.cornell.edu/~jnfoster/papers/grace-report.pdf) )
.
A deeper understanding of bidirectional transformations and Category Theory
may help resolve resolve some of this tension.

The pingel.org packages address the first concern by utilizing Scala's expressive
syntax and typesystem.
The second concern is not addressed, and therefore these packages do have
their own kind of "tyranny".
Care has been taken to choose conceptual pillars that cover a wide range of 
ideas without introducing unnecessary inconsistencies or impedance mismatches.

(This section will eventually be updated with more concrete examples.)


History
-------

Many of these projects started their life as Java.
However, difficulties with its unexpressive syntax and typesystem diminished
the utility of this code as a study aid.
The conversion to Scala began in 2009.
Scala's support of "family polymorphism" and other advanced capabilities
promise to solve some of those modelling difficulties.

As computer science evolves these packages will be udpated to make use of
helpful new tools and techniques wherever possible.


About the Author
----------------

Adam Pingel is a Iowa native who moved to the San Francisco Bay Area in 1992
to study Computer Science at Stanford.  After graduating in 1996, he spent
six years working in the nascent internet industry writing tools for Operations
at Excite.com and then NOCpulse (a startup that was acquired by Red Hat).
In 2002 he left Silicon Valley to pursue a PhD in Computer Science at UCLA.

While there he worked for a year as a TA for the undergraduate Artificial
Intelligence class, another year as a researcher working on programming tools
for artists at the UCLA School of Theater, Film, and Television.

From 2005 - 2009 he mixed continued graduate studies with occasional 
consulting.
In 2009 he moved to San Francisco with an MS to return to industry full-time.

Since then he works as a Lead Systems Engineer in the music industry.
