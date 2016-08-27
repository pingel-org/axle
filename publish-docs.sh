#!/usr/bin/env bash

s3cmd sync ~/s3/axle-lang.org/ s3://axle-lang.org/
# s3cmd setacl s3://axle-lang.org/ --acl-public --recursive
