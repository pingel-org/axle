#!/bin/bash -x

set -e

sbt 'project axle-docs' clean

sbt -J-Xmx6G 'project axle-docs' mdoc # axle-docs/src/main/mdoc -> axle-docs/target/mdoc/
sbt 'project axle-docs' laikaSite # {axle-docs/target/mdoc, axle-docs/src/site} -> axle-docs/target/site

export SITEBUILDDIR=axle-docs/target/site
mkdir -p $SITEBUILDDIR/tutorial/images/
mv *.svg *.png $SITEBUILDDIR/tutorial/images/ # Where does bowl.html belong?

# sbt 'project axle-docs' ghpagesCleanSite
sbt 'project axle-docs' ghpagesPushSite
