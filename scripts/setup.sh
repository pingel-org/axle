#!/bin/bash -x

set -e

function install_base {
    add-apt-repository "deb http://archive.canonical.com/ lucid partner"
    apt-get update
    apt-get install sun-java6-bin sun-java6-jre sun-java6-jdk
    apt-get install mysql-client mysql-server
    apt-get install subversion
    apt-get install git-core
}

function install_mvn {
    echo "install mvn"
}

function install_scala {

    echo "install scala"

    SCALATAG=2.8.1.final

    (cd /usr/local
	curl -O http://www.scala-lang.org/downloads/distrib/files/scala-${SCALATAG}.tgz
	tar xvfz scala-${SCALATAG}.tgz
	ln -s scala-${SCALATAG} scala
	)
}

function install_sbt {

    echo "install sbt"

    SBTVERSION=0.7.4

    mkdir -p ~/bin
    cd ~/bin
    curl -O http://simple-build-tool.googlecode.com/files/sbt-launch-${SBTVERSION}.jar
    echo 'java -Xmx512M -jar `dirname $0`/sbt-launch-${SBTVERSION}.jar "$@"' > sbt
    chmod u+x sbt

    export PATH=~/bin:$PATH
}

function install_ensime {

    echo "install ensime"

    # TODO
}

function install_mongo {

    echo "install mongo"

    DATADIR=/data/db
    MONGOVERSION=1.6.5

    mkdir -p $DATADIR

    (cd /usr/local
	curl -O http://fastdl.mongodb.org/linux/mongodb-linux-x86_64-${MONGOVERSION}.tgz
	tar xvfz mongodb-linux-x86_64-${MONGOVERSION}.tgz
	ln -s mongodb-linux-x86_64-${MONGOVERSION} mongodb
	)

    # /usr/local/mongodb/bin/mongod --dbpath $DATADIR &
    # http://localhost:28017/

}

function install_lift {

    echo "install lift"

    # TODO
}

function install_hadoop {

    echo "install hadoop"

    # TODO
}

function install_lucene {

    echo "install lucene"

    # TODO
}

function install_mahout {

    echo "install mahout"

    # TODO
}
