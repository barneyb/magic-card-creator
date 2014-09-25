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

    @Override
    protected String getUrlPath() {
        "/convert/pdf?r=$rotate.direction"
    }

    @Override
    protected String getFileSuffix() {
        ".pdf"
    }

}
