#!/bin/sh
#################################
#
# This script builds the fruity
# binary file.  Usage:
#
# make-binary.sh
#
#################################

SHNAME=fruity

echo "Making Uberjar"
lein uberjar

echo "Creating Executable"
cat scripts/installer.sh fruity.jar > bin/$SHNAME
chmod 755 bin/$SHNAME

