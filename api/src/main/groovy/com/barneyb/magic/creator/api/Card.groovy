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
     * match the Layout's expected dimensions, it will be scaled to cover and
     * the other dimension centered.
     */
    Artwork getArtwork()

    /**
     * I am additional artwork (very likely derived from the main artwork) which
     * will be scaled and positioned exactly the same as the main artwork, but
     * which will not be clipped, and will be placed above all other card
     * elements.  This is designed to allow the artwork to overrun the layout
     * boundaries, but should be used with caution as it can irrevocably occlude
     * important card elements.  For example, the effect of Nicol Bolas,
     * Planeswalker's wingtip or a pile of Unhinged cards.
     */
    Artwork getOverArtwork()

    List<String> getTypes()

    List<String> getSubtypes()

    /**
     * I am the List of paragraphs of rules text for the card, potentially
     * including {@link NonNormativeText} and {@link SymbolGroup}s.  Each item
     * in the list is a run of {@link BodyItem} that should be laid out as a
     * single line.
     */
    List<List<BodyItem>> getRulesText()

    /**
     * I am a list of paragraphs of flavor text for the card.  I should only
     * contain {@link NonNormativeText} and {@link LineBreak} elements. Each
     * item in the list is a run of {@link BodyItem} that should be laid out as
     * a single line.
     */
    List<List<BodyItem>> getFlavorText()

    /**
     * I indicate the colors this card and/or the permanent it represents
     * belongs to.  In nearly all cases, this is based wholly on the card's
     * casting cost (an exception is Transguild Courier).  Note that every card
     * has at least one color, though it may be {@link ManaColor#COLORLESS}
     * (e.g., most artifacts and lands).
     */
    List<ManaColor> getColors()

    /**
     * I indicate whether the ManaColor(s) of this card are explicit (meaning
     * they are explicitly stipulated, instead of being implicitly based on the
     * c cost of the spell or COLORLESS for lands).
     */
    boolean isColorExplicit()

    /**
     * I indicate whether this card is multiple colors
     * ({@link ManaColor#COLORLESS} counts).
     */
    boolean isMultiColor()

    /**
     * I am the name of the watermark that should be placed in the textbox
     * before the body is rendered into it.
     */
    String getWatermarkName()

    String getCopyright()

    Rarity getRarity()

    Locale getLocale()

    /**
     * The name of the set this Card belongs to, or null if standalone.
     */
    String getSetName()

    /**
     * This Card's number w/in its set, or null if standalone.
     */
    Integer getCardNumber()

    /**
     * The total number of Cards w/in this Card's set, or null if standalone.
     */
    Integer getSetCardCount()

}
