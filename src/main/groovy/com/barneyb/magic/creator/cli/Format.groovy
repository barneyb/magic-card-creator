package com.barneyb.magic.creator.cli

/**
 * Created by barneyb on 7/25/2014.
 */
enum Format {

    svg('embed', { byte[] svg ->
        svg
    }),
    png('img', { byte[] svg ->
        def width = Integer.getInteger("IMAGE_WIDTH", 0)
        throw new UnsupportedOperationException("formatting as png (at $width doesn't work yet)")
    }),
    pdf(null, { byte[] svg ->
        throw new UnsupportedOperationException('formatting as pdf doesn\'t work yet')
    })

    final String proofTag
    final Closure<byte[]> formatter

    def Format(String pt, Closure<byte[]> f) {
        proofTag = pt
        formatter = f
    }

    boolean isProofed() {
        proofTag != null
    }
}

