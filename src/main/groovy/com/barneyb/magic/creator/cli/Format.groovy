package com.barneyb.magic.creator.cli

import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.fop.svg.PDFTranscoder

/**
 * Created by barneyb on 7/25/2014.
 */
enum Format {

    svg('embed', { byte[] svg ->
        svg
    }),
    png('img', { byte[] svg ->
        def width = Integer.getInteger("imageWidth", 0)
        def dest = new ByteArrayOutputStream()
        def t = new PNGTranscoder()
        if (width != null && width > 0) {
            t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width.floatValue())
        }
        t.transcode(
            new TranscoderInput(new ByteArrayInputStream(svg)),
            new TranscoderOutput(dest)
        )
        dest.flush()
        dest.toByteArray()
    }),
    pdf(null, { byte[] svg ->
        def dest = new ByteArrayOutputStream()
        new PDFTranscoder().transcode(
            new TranscoderInput(new ByteArrayInputStream(svg)),
            new TranscoderOutput(dest)
        )
        dest.flush()
        dest.toByteArray()
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

