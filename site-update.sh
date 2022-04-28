#!/bin/bash -x

set -e

date
sbt -J-Xmx8G clean mdoc
sbt laikaSite ghpagesPushSite
# ghpagesCleanSite
date
