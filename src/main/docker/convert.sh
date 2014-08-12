#!/bin/bash
#
# I convert everything in the $1 directory and stick it in the $2 directory,
# based on the format(s) that are listed in $3 and higher.  Currently only
# 'pdf' and 'png' are supported.  For the 'png' format, it may be optionally
# suffixed with a colon and width which will be used to do an aspect-ratio
# preserving size conversion (e.g., png:400 will create 400px wide images).

src=$1
shift
target=$1
shift

# now process the formats
while [ "$1" != "" ]; do
    spec=$1
    echo "format $spec"
    shift
    format=`echo $spec | cut -d : -f 1`
    cmd="rsvg-convert -f $format"
    if [ $format = "png" ]; then
        width=`echo $spec | cut -d : -f 2`
        if [ "$width" != "" -a $width != $format ]; then
            cmd="$cmd -w $width -a"
            format="$width.$format"
        fi
    fi
    for i in `find $src -name "*.svg"`; do
        echo "[$format] $i"
        $cmd -o $target/`basename $i`.$format $i
    done
done