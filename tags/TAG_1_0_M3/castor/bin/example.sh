#!/bin/sh

# $Id$

if [ -z "$JAVA_HOME" ] ; then
  JAVA=`which java`
  if [ -z "$JAVA" ] ; then
    echo "Cannot find JAVA. Please set your PATH."
    exit 1
  fi
  JAVA_BIN=`dirname $JAVA`
  JAVA_HOME=$JAVA_BIN/..
fi

JAVA=$JAVA_HOME/bin/java

CLASSPATH=../build/classes:../build/examples:$CLASSPATH
CLASSPATH=`echo ../lib/*.jar | tr ' ' ':'`:$CLASSPATH

if [ -z $1 ] ; then
  echo "Usage: example <pkg>";
  exit;
fi
$JAVA -cp $CLASSPATH $1.Test $2 $3 $4 $5 $6


