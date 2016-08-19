#!/bin/bash

sbt -J-Xmx6G tut

mv *.svg docs/images/

cp -R axle-test/target/scala-2.11/tut/* docs/

sed -i -e 's/releaseVersion/0.2.3/g' `find docs -name '*.md'`

sed -i -e 's/snapshotVersion/0.2.4-SNAPSHOT/g' `find docs -name '*.md'`
