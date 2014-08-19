package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface Card {

    String getTitle()

    /**
     * I am the main artwork for the card, which will be rendered into the
     * designated artwork area in the Layout.  If the image dimensions do not
     * match the Layout's expected dimensions, it will be scaled to fit.
     */
    Artwork getArtwork()

    /**
     * I am additional artwork (very likely derived from the main artwork) which
     * will be scaled and positioned exactly the same as the main artwork, but
     * which will not be clipped, and will be placed above all other card
     * elements.  This is designed to allow the artwork to overrun the layout
     * boundaries, but should be used with caution as it can irrevocably occlude
     * important card elements.  For example, the effect of Nicol Bolas,
     * Planeswalker's wingtip or Sarkhan the Mad's spear tassels.
     */
    Artwork getOverArtwork()

    String getType()

    List<String> getTypeParts()

    String getSubtype()

    List<String> getSubtypeParts()

    List<List<BodyItem>> getBody()

}
