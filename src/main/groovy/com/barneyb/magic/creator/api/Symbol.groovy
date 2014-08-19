package com.barneyb.magic.creator.api
import com.barneyb.magic.creator.descriptor.ManaColor
/**
 * I represent a symbol (blue mana, tap, etc.) used in magic rules.  Not all
 * symbols have color, and some may have multiple colors.
 *
 * @author bboisvert
 */
interface Symbol {

    /**
     * I return the Symbol as it would be referenced in textual rules format.
     * E.g., {2/U} for the colorless/blue hybrid mana symbol, or {Q} for the
     * untap symbol. Strings are always uppercase, and always wrapped in braces.
     */
    String toString()

    boolean isColored()

    List<ManaColor> getColors()

    boolean isMultiColor()

    /**
     * I return the symbol's primary color, or
     * @return
     */
    ManaColor getColor()

}