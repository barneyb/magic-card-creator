package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface SymbolFactory {

    /**
     * I create a Symbol instance from the provided symbol string.  The string
     * will be parsed in a case-insensitive manner (e.g., <tt>{U}</tt> and
     * <tt>{u}</tt> are equivalent), and the wrapping braces are optional (e.g.,
     * <tt>{U}</tt> and <tt>U</tt> are equivalent).
     *
     * <p>This method is symmetrical with {@link Symbol#toString()}.
     *
     * @param symbol the symbol string to create a symbol for.
     * @return the Symbol instance
     * @throws IllegalArgumentException if the symbol string is unrecognized.
     */
    Symbol getSymbol(String symbol)

}