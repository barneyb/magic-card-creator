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
     * @param symbol the symbol string to create a Symbol for.
     * @return the Symbol instance.
     * @throws IllegalArgumentException if the symbol string is unrecognized.
     */
    Symbol getSymbol(String symbol)

    /**
     * I create a SymbolGroup from the provided string versions.  The string
     * will be parsed according to the rules of {@link #getSymbol(java.lang.String)}
     * with the addition that multiple symbols are supported.
     *
     * @param symbols the symbols string to create a SymbolGroup for.
     * @return a SymbolGroup parsed from the string.
     * @throws IllegalArgumentException if an unrecognized symbol is found.
     */
    SymbolGroup getCost(String symbols)

    /**
     * I create a SymbolGroup from the provided string versions.  Each string
     * will be individually parsed according to the rules of
     * @link #getSymbol(java.lang.String)}.
     *
     * @param symbols the collection of symbol strings to create a SymbolGroup for.
     * @return a SymbolGroup parsed from the strings
     * @throws IllegalArgumentException if an unrecognized symbol is found.
     */
    SymbolGroup getCost(Collection<String> symbols)

}
