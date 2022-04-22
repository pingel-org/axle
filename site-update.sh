#!/bin/bash -x

set -e

sbt -J-Xmx6G "project axle-docs" clean mdoc makeSite

export SITEBUILDDIR=axle-docs/target/site

mkdir -p $SITEBUILDDIR
cp axle-docs/src/site/_config.yml $SITEBUILDDIR
cp axle-docs/src/site/favicon.ico $SITEBUILDDIR
cp -R axle-docs/src/site/tutorial $SITEBUILDDIR
cp -R axle-docs/target/site/* $SITEBUILDDIR
mkdir -p $SITEBUILDDIR/tutorial/images/
mv *.svg *.png $SITEBUILDDIR/tutorial/images/
# Where does bowl.html belong?
cp -R axle-docs/src/site/css $SITEBUILDDIR
# Where is $SITEBUILDDIR/target coming from?

sbt 'project axle-docs' ghpagesCleanSite ghpagesPushSite
