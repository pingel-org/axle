#!/usr/bin/env bash

set -x
set -e

s3cmd sync $SITESTAGEDIR $SITES3URL

s3cmd setacl $SITES3URL --acl-public --recursive
