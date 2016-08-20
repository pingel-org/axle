#!/bin/bash

sbt -J-Xmx6G "project axle-docs" tut

mv *.svg docs/images/

cp -R axle-docs/target/scala-2.11/tut/* docs/

sed -i -e 's/releaseVersion/0.2.3/g' `find docs -name '*.md'`

sed -i -e 's/snapshotVersion/0.2.4-SNAPSHOT/g' `find docs -name '*.md'`

find . -name '*.md-e' | xargs rm
