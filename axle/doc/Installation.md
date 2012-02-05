
Installation
------------

TODO

The Matrix class depends on JBLAS, which is not hosted on any public repository.

AXLE itself it not yet hosted in a public repository.

### SBT

```bash
curl http://cloud.github.com/downloads/mikiobraun/jblas/jblas-1.2.0.jar -o lib/jblas-1.2.0.jar
```

### Maven

Download and install in the local build tool:

```bash
curl http://cloud.github.com/downloads/mikiobraun/jblas/jblas-1.2.0.jar -o jblas-1.2.0.jar

mvn install:install-file -DgroupId=org.jblas -DartifactId=jblas -Dversion=1.2.0 -Dfile=jblas-1.2.0.jar -Dpackaging=jar -DgeneratePom=true
```

