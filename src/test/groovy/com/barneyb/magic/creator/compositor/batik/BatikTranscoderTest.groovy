package com.barneyb.magic.creator.compositor.batik

import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.fop.svg.PDFTranscoder
import org.junit.BeforeClass
import org.junit.Test
/**
 *
 * @author bboisvert
 */
class BatikTranscoderTest {

    static final File outputDir = new File("build")

    @BeforeClass
    static void ensureTarget() {
        if (! outputDir.exists()) {
            outputDir.mkdir()
        }
        def f = new File(outputDir, "preview_batik.html")
        f.text = """\
<html>
<body>
<embed src="barney.svg"></embed>
<img src="barney_tc.png" />
<img src="barney_tc_small.png" />
<img src="barney.png" />
</body>
</html>"""
    }

    @Test
    void png() {
        def src = new File(outputDir, 'barney.svg')
        if (! src.exists()) {
            println "can't transcode - source doesn't exist"
            return
        }
        def dest = new File(src.parent, "barney_tc.png")
        new PNGTranscoder().transcode(
            new TranscoderInput(src.toURI().toString()),
            new TranscoderOutput(dest.newOutputStream())
        )
        dest = new File(src.parent, "barney_tc_small.png")
        def t = new PNGTranscoder()
        t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 400f)
        t.transcode(
            new TranscoderInput(src.toURI().toString()),
            new TranscoderOutput(dest.newOutputStream())
        )
    }

    @Test
    void pdf() {
        def src = new File(outputDir, 'barney.svg')
        if (! src.exists()) {
            println "can't transcode - source doesn't exist"
            return
        }
        def dest = new File(src.parent, "barney_tc.pdf")
        new PDFTranscoder().transcode(
            new TranscoderInput(src.toURI().toString()),
            new TranscoderOutput(dest.newOutputStream())
        )
    }

}
