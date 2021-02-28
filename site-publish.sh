#!/usr/bin/env bash

SITESTAGEDIR=~/s3/axle-lang.org/
SITEURL=s3://axle-lang.org/

s3cmd sync $SITESTAGEDIR $SITEURL

s3cmd setacl $SITEURL --acl-public --recursive
