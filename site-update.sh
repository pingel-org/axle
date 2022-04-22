#!/bin/bash -x

set -e

sbt 'project axle-docs' clean

# axle-docs/src/main/mdoc -> axle-docs/target/mdoc/
sbt -J-Xmx6G 'project axle-docs' mdoc

# sbt 'project axle-docs' makeSite

export SITEBUILDDIR=axle-docs/target/site

mkdir -p $SITEBUILDDIR

sbt 'project axle-docs' laikaSite

cp axle-docs/src/site/favicon.ico $SITEBUILDDIR
mkdir -p $SITEBUILDDIR/tutorial/images/
mv *.svg *.png $SITEBUILDDIR/tutorial/images/ # Where does bowl.html belong?
#cp -R axle-docs/src/site/css $SITEBUILDDIR

# Where is $SITEBUILDDIR/target coming from?

# sbt 'project axle-docs' ghpagesCleanSite

sbt 'project axle-docs' ghpagesPushSite
