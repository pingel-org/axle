#!/bin/bash

sbt -J-Xmx6G "project axle-docs" makeSite

mv *.svg axle-docs/target/site/images/

sed -i -e 's/releaseVersion/0.2.3/g' `find axle-docs/target/site/ -name '*.md'`

sed -i -e 's/snapshotVersion/0.2.4-SNAPSHOT/g' `find axle-docs/target/site/ -name '*.md'`

find . -name '*.md-e' | xargs rm
