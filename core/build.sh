#!/bin/bash

set -e

echo "building Axle"

sbt +compile

sbt +package

sbt +publish-local

# sbt +assembly
# sbt +publish
