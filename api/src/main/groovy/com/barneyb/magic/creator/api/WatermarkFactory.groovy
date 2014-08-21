package com.barneyb.magic.creator.api

import org.w3c.dom.svg.SVGDocument

/**
 * I am in charge of converting watermark names (as Strings) into SVGDocuments
 * that visually represent them.  All documents will contain a single
 * <tt>&lt;g&gt;</tt> element with everything inside.
 *
 * @author bboisvert
 */
interface WatermarkFactory {

    /**
     * I construct an the visual representation of the named watermark.
     */
    SVGDocument getWatermark(String name)

}
