#!/bin/bash -x

set -e

sudo apt-get install ruby-full build-essential zlib1g-dev

sudo apt-get install -y s3cmd

cat << EOF >> ~/.s3cfg
[default]
access_key = ${SITEAWSACCESSKEY}
secret_key = ${SITEAWSSECRETKEY}
EOF

ls -l ~/.s3cfg

wc -l ~/.s3cfg

md5sum ~/.s3cfg

sudo gem install jekyll bundler


