#!/usr/bin/env bash

s3cmd sync $SITESTAGEDIR $SITES3URL

s3cmd setacl $SITES3URL --acl-public --recursive
