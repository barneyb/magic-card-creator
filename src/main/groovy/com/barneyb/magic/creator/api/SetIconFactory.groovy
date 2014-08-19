package com.barneyb.magic.creator.api

import org.w3c.dom.svg.SVGDocument

/**
 * I am in charge of converting set names (as Strings) into SVGDocuments that
 * visually represent them.  All documents will contain a single
 * <tt>&lt;g&gt;</tt> element with everything inside.
 *
 * @author bboisvert
 */
interface SetIconFactory {

    /**
     * I construct the icon for the named set assuming the COMMON rarity.
     */
    SVGDocument getIcon(String setName)

    /**
     * I construct the icon for the named set at the specified rarity.
     */
    SVGDocument getIcon(String setName, Rarity rarity)

}