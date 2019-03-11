#!/bin/bash

sbt -J-Xmx6G "project axle-docs" mdoc

sbt -J-Xmx6G "project axle-docs" makeSite

mkdir -p axle-docs/target/site/tutorial/images/
mv *.svg *.png axle-docs/target/site/tutorial/images/

cp -R axle-docs/src/site/css axle-docs/target/site/

jekyll build --source axle-docs/target/site/ --destination ~/s3/axle-lang.org/

(cd ~/s3/axle-lang.org/; python -m SimpleHTTPServer 8000)
