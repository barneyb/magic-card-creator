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
     * I indicate the ManaColors this symbol is associated with, which may
     * include {@link ManaColor#COLORLESS}, and may be empty (e.g., for the
     * {T} or tap symbol)
     */
    List<ManaColor> getColors()

    /**
     * I indicate whether this symbol has color ({@link ManaColor#COLORLESS}
     * counts as a color).
     */
    boolean isColored()

    /**
     * I indicate whether this symbol is associated with multiple colors.
     * @return
     */
    boolean isMultiColor()

    /**
     * I return the symbol's primary color.
     *
     * @throws UnsupportedOperationException if this symbol is not associated
     *  with any colors.
     */
    ManaColor getColor()

    /**
     * I return the Symbol as it would be referenced in textual rules format.
     * E.g., {2/U} for the colorless/blue hybrid mana symbol, or {Q} for the
     * untap symbol. Strings are always uppercase, and always wrapped in braces.
     *
     * <p>This method is symmetrical with {@link SymbolFactory#getSymbol(java.lang.String)}.
     */
    String toString()

}