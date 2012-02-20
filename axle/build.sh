#!/bin/bash

set -e

echo "building Axle"

sbt +compile

# sbt +assembly
# sbt +publish
