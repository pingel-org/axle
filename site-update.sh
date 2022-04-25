#!/bin/bash -x

set -e

date
sbt -J-Xmx8G 'project axle-docs' clean mdoc laikaSite ghpagesPushSite
# ghpagesCleanSite
date
