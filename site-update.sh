#!/bin/bash -x

set -e

date
sbt 'project axle-docs' clean
sbt 'project axle-docs' mdoc
sbt 'project axle-docs' laikaSite
#sbt ghpagesCleanSite
sbt 'project axle-docs' ghpagesPushSite
date
