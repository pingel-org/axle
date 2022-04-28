#!/bin/bash -x

set -e

date
sbt -J-Xmx8G 'project axle-docs' clean mdoc
sbt 'project axle-docs' laikaSite
#sbt ghpagesCleanSite
sbt 'project axle-docs' ghpagesPushSite
date
