#!/bin/bash -x

set -e

date
sbt -J-Xmx8G 'project axle-docs' clean mdoc
sbt 'project axle-docs' laikaSite ghpagesPushSite
# ghpagesCleanSite
date
