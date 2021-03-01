#!/bin/bash -x

set -e

sbt -J-Xmx6G "project axle-docs" mdoc
sbt -J-Xmx6G "project axle-docs" makeSite

mkdir -p $SITEBUILDDIR
cp axle-docs/src/site/Gemfile $SITEBUILDDIR
cp axle-docs/src/site/_config.yml $SITEBUILDDIR
cp axle-docs/src/site/favicon.ico $SITEBUILDDIR
cp -R axle-docs/src/site/tutorial $SITEBUILDDIR
cp -R axle-docs/target/site/* $SITEBUILDDIR
mv *.svg *.png $SITEBUILDDIR/tutorial/images/
cp -R axle-docs/src/site/css $SITEBUILDDIR

mkdir -p $SITESTAGEDIR

(cd $SITEBUILDDIR; bundle install)

JEKYLL_ENV=production jekyll build --source $SITEBUILDDIR --destination $SITESTAGEDIR --trace

find $SITEBUILDDIR

# (cd $SITESTAGEDIR; python -m SimpleHTTPServer 8000)
