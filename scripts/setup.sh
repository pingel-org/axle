#!/bin/bash -x

set -e

function install_base {
    add-apt-repository "deb http://archive.canonical.com/ lucid partner"
    apt-get update
    apt-get install sun-java6-bin sun-java6-jre sun-java6-jdk
    apt-get install strace dstat
    apt-get install mysql-client mysql-server
    apt-get install subversion
    apt-get install git-core
    apt-get install maven2
}

function install_scala {

    echo "install scala"

    SCALATAG=2.8.1.final

    (cd /usr/local
	curl -O http://www.scala-lang.org/downloads/distrib/files/scala-${SCALATAG}.tgz
	tar xvfz scala-${SCALATAG}.tgz
	ln -s scala-${SCALATAG} scala
	)

    # TODO: put this somewhere
    export PATH=/usr/local/scala/bin/:$PATH
}

function install_emacs {

    apt-get install emacs

    mkdir ~/.emacs.backups

    echo <<EOF
(setq
   backup-by-copying t      ; don't clobber symlinks
   backup-directory-alist
    '(("." . "~/.emacs.backups"))    ; don't litter my fs tree
   delete-old-versions t
   kept-new-versions 6
   kept-old-versions 2
   version-control t)       ; use versioned backups

EOF
}

function install_sbt {

    echo "install sbt"

    SBTVERSION=0.7.4

    mkdir -p ~/bin
    cd ~/bin
    curl -O http://simple-build-tool.googlecode.com/files/sbt-launch-${SBTVERSION}.jar
    echo 'java -Xmx512M -jar `dirname $0`/sbt-launch-${SBTVERSION}.jar "$@"' > sbt
    chmod u+x sbt

    # TODO: put this in .profile or .bashrc
    export PATH=~/bin:$PATH
}

function install_ensime {

    echo "install ensime"

    # https://github.com/aemoncannon/ensime/blob/master/README.md

    # Step 1: install scala distribution

    (
	cd /usr/local/scala/misc/scala-tool-support/emacs
	make
    )
    echo <<EOF > ~/.emacs
(add-to-list 'load-path "/usr/local/scala/misc/scala-tool-support/emacs")
(require 'scala-mode-auto)

EOF

    # Step 2: ensime-mode
    (
	cd /usr/local
	curl -O http://cloud.github.com/downloads/aemoncannon/ensime/ensime_2.8.1-0.4.4.tar.gz
	tar xvfz ensime_2.8.1-0.4.4.tar.gz
	ln -s ensime_2.8.1-0.4.4 ensime
    )

    echo <<"EOF" >> ~/.emacs
(add-to-list 'load-path "/usr/local/ensime/elisp/")
(require 'ensime)
(add-hook 'scala-mode-hook 'ensime-scala-mode-hook)
EOF

    # Step 3: Verify permissions

    chmod +x /usr/local/ensime/bin/server

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

function install_lucene {

    echo "install lucene"

    (
	cd /usr/local/
	curl -O http://apache.mirrors.hoobly.com//lucene/java/lucene-3.0.3.tar.gz
	tar xvfz lucene-3.0.3.tar.gz
	ln -s lucene-3.0.3 lucene
    )

}

function install_hadoop {

    echo "install hadoop"

    # TODO
}

function install_mahout {

    echo "install mahout"

    (
	cd /usr/local
	curl -O http://apache.osuosl.org//mahout/0.4/mahout-distribution-0.4.tar.gz
	tar xvfz mahout-distribution-0.4.tar.gz
	ln -s mahout-distribution-0.4 mahout
	)

    # TODO
}

function new_project {

    echo <<EOF
import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info)
with assembly.AssemblyBuilder
{
   // BEGIN
   // END
}
EOF

    echo <<EOF > project/plugins/Plugins.scala
class Plugins(info: sbt.ProjectInfo) extends sbt.PluginDefinition(info) {
  val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"
  val assemblySBT = "com.codahale" % "assembly-sbt" % "0.1"
}
EOF

    # emacs: M-x ensime-config-gen

}
