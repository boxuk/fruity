#!/bin/sh
#################################
#
# This script runs fruity.  Usage:
#
#   fruity /path/to/config.clj
#
##################################

JARNAME=fruity.jar

# unpack jar file
export TMPDIR=`mktemp -d /tmp/selfextract.XXXXXX`
ARCHIVE=`awk '/^__ARCHIVE_BELOW__/ {print NR + 1; exit 0; }' $0`
tail -n+$ARCHIVE $0 > $TMPDIR/$JARNAME

# execute jar file
java -jar $TMPDIR/$JARNAME $@

# cleanup
rm -rf $TMPDIR

exit 0

__ARCHIVE_BELOW__
