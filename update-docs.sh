#!/bin/bash

sbt -J-Xmx6G "project axle-docs" makeSite

mkdir -p axle-docs/target/site/chapter/images/
mv *.svg axle-docs/target/site/chapter/images/

sed -i -e 's/releaseVersion/0.2.3/g' `find axle-docs/target/site/ -name '*.md'`

sed -i -e 's/snapshotVersion/0.2.4-SNAPSHOT/g' `find axle-docs/target/site/ -name '*.md'`

find axle-docs -name '*.md-e' | xargs rm

jekyll build --source axle-docs/target/site/ --destination ~/s3/axle-lang.org/

(cd ~/s3/axle-lang.org/; python -m SimpleHTTPServer 8000)
