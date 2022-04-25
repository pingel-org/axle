# Spokes

Witnesses for 3rd party libraries: The "Spokes"

## Parallel Collections

```sbt
"org.axle-lang" %% "axle-parallel" % "@RELEASE_VERSION@"
```

For use with Scala [Parallel Collections](https://github.com/scala/scala-parallel-collections) library
(`"org.scala-lang.modules" %% "scala-parallel-collections" % ...`)

## XML

```sbt
"org.axle-lang" %% "axle-xml" % "@RELEASE_VERSION@"
```

For use with Scala [XML](https://github.com/scala/scala-xml) library
(`"org.scala-lang.modules" %% "scala-xml" % ...`)

XML includes `axle.web`, where HTML and SVG visualizations reside.

## JBLAS

```sbt
"org.axle-lang" %% "axle-jblas" % "@RELEASE_VERSION@"
```

[Linear Algebra](../fundamental/LinearAlgebra.md) and other witnesses for [JBLAS](http://jblas.org/) which itself is a wrapper for [LAPACK](http://www.netlib.org/lapack/).
Includes Principal Component Analysis (PCA).

## JODA

```sbt
"org.axle-lang" %% "axle-joda" % "@RELEASE_VERSION@"
```

Witnesses for the [Joda](http://www.joda.org/joda-time/) time library.

## JUNG

```sbt
"org.axle-lang" %% "axle-jung" % "@RELEASE_VERSION@"
```

[Graph](../fundamental/Graph.md) Directed and undirected graph witnesses for the [JUNG](http://jung.sourceforge.net/) library.

## AWT

```sbt
"org.axle-lang" %% "axle-awt" % "@RELEASE_VERSION@"
```

Witnesses for [AWT](https://docs.oracle.com/javase/7/docs/api/java/awt/package-summary.html)
