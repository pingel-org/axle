#!/bin/bash

sbt -J-Xmx6G "project axle-docs" makeSite

# mkdir -p axle-docs/target/site/tutorial/images/
mv *.svg *.png axle-docs/target/site/tutorial/images/

sed -i -e 's/releaseVersion/0.4.0/g' `find axle-docs/target/site/ -name '*.md'`

sed -i -e 's/snapshotVersion/0.4.1-SNAPSHOT/g' `find axle-docs/target/site/ -name '*.md'`

find axle-docs -name '*.md-e' | xargs rm

cp -R axle-docs/src/site/css axle-docs/target/site/

jekyll build --source axle-docs/target/site/ --destination ~/s3/axle-lang.org/

(cd ~/s3/axle-lang.org/; python -m SimpleHTTPServer 8000)
