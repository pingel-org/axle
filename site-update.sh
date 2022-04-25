#!/bin/bash -x

set -e

date
sbt 'project axle-docs' clean
sbt -J-Xmx6G 'project axle-docs' mdoc
sbt 'project axle-docs' laikaSite
# sbt 'project axle-docs' ghpagesCleanSite
sbt 'project axle-docs' ghpagesPushSite
date
