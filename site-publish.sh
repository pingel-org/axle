#!/usr/bin/env bash

set -x
set -e

s3cmd sync -v $SITESTAGEDIR $SITES3URL

s3cmd setacl -v $SITES3URL --acl-public --recursive
