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

    static final File outputDir = new File("src/main/design")

    @BeforeClass
    static void ensureTarget() {
        if (! outputDir.exists()) {
            outputDir.mkdir()
        }
        def f = new File(outputDir, "preview_batik.html")
        f.text = """\
<html>
<head>
<style>
img {
    width: 400px;
    height: 560px;
}
</style>
</head>
<body>
<embed src="barney.svg"></embed>
<img src="barney.png" />
<img src="../../../build/barney.png" />
</body>
</html>"""
    }

    @Test
    void png() {
        def src = new File('src/main/design/barney.svg')
        def dest = new File(src.parent, "barney.png")
        new PNGTranscoder().transcode(
            new TranscoderInput(src.toURI().toString()),
            new TranscoderOutput(dest.newOutputStream())
        )
    }

    @Test
    void pdf() {
        def src = new File('src/main/design/barney.svg')
        def dest = new File(src.parent, "barney.pdf")
        new PDFTranscoder().transcode(
            new TranscoderInput(src.toURI().toString()),
            new TranscoderOutput(dest.newOutputStream())
        )
    }

}
