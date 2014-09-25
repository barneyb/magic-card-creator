package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.util.SvgUtils
import com.barneyb.magic.creator.util.XmlUtils
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import org.w3c.dom.svg.SVGDocument

/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "pdf", commandDescription = "transcode composed cards to PDF (via docker)", separators = "=")
class PdfCommand extends BaseTranscodeCommand {

    static enum Rotate {
        none('north'),
        cw('east'),
        ccw('west'),
        flip('south')

        final String direction

        private Rotate(String d) {
            direction = d
        }
    }

    @Parameter(names = "--rotate", description = "how to rotate the cards when transcoding (none, cw, ccw, flip)")
    Rotate rotate = Rotate.none

    @Parameter(names = "--bleed", description = "if true, add a print bleed when transcoding")
    boolean bleed = false

    @Override
    protected File transformSource(File src) {
        if (bleed) {
            def doc = XmlUtils.read(src.newReader())
            doc = addBleed(doc)
            def ns = new File(src.parentFile, src.name.contains('.') ? src.name.replaceFirst(/\./, '-bleed.') : src.name + "-bleed")
            ns.deleteOnExit()
            def out = ns.newWriter()
            XmlUtils.write(doc, out)
            out.close()
            src = ns
        }
        src
    }

    protected SVGDocument addBleed(SVGDocument doc) {
        def xbleed = 17.5
        def ybleed = 17.5
        def rotate = 0 // todo: setting this to anything but 0 mod 360 screws up the filters and patterns.  no idea why.
        def size = SvgUtils.size(doc)
        def base = doc
        doc = XmlUtils.create()

        def w = size.width
        def h = size.height
        def flip = rotate % 180 != 0
        def newWidth = (flip ? h : w) + xbleed * 2
        def newHeight = (flip ? w : h) + ybleed * 2

        XmlUtils.elattr(doc.rootElement, [
            width : newWidth,
            height: newHeight
        ])
        XmlUtils.el(doc.rootElement, 'rect', [
            width : "100%",
            height: "100%",
            fill  : "#000"
        ])
        def g = XmlUtils.el(doc.rootElement, 'g', [
            transform: "translate($xbleed $ybleed)"
        ])
        if (rotate % 360 != 0) {
            g = XmlUtils.el(g, 'g', [
                transform: (flip ? "translate(${(h - w) / 2} ${(w - h) / 2}) " : "") + "rotate($rotate ${(float) w / 2} ${(float) h / 2})"
            ])
        }
        g.appendChild(doc.adoptNode(base.rootElement))
        doc
    }

    @Override
    protected String getUrlPath() {
        "/convert/pdf?r=$rotate.direction"
    }

    @Override
    protected String getFileSuffix() {
        ".pdf"
    }

}
