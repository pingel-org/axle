#!/bin/bash -x

set -e

sbt 'project axle-docs' clean

mkdir -p docwork
sbt -J-Xmx6G 'project axle-docs' mdoc # axle-docs/src/main/mdoc -> axle-docs/target/mdoc/; write new stuff to docwork

sbt 'project axle-docs' laikaSite # {axle-docs/target/mdoc, axle-docs/src/site} -> axle-docs/target/site

# sbt 'project axle-docs' ghpagesCleanSite

sbt 'project axle-docs' ghpagesPushSite
