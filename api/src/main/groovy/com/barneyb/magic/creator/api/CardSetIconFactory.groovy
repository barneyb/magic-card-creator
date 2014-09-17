package com.barneyb.magic.creator.api
/**
 * I am in charge of converting set keys (as Strings) into SVGDocuments that
 * visually represent them.  All documents will contain a single
 * <tt>&lt;g&gt;</tt> element with everything inside.
 *
 * @author bboisvert
 */
interface CardSetIconFactory {

    /**
     * I construct the icon for the named set assuming the COMMON rarity.
     */
    Icon getIcon(CardSet cs)

    /**
     * I construct the icon for the named set at the specified rarity.
     */
    Icon getIcon(CardSet cs, Rarity rarity)

}
