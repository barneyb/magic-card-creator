package com.barneyb.magic.creator.api
/**
 *
 * @author bboisvert
 */
interface CardSet extends Keyed {

    String getTitle()

    List<Card> getCards()

    int getCardsInSet()

    String getCopyright()

    /**
     * The field for the set's icon, which is laid under the symbol based on
     * different composition rules.  If not provided, the symbol will be used
     * on its own (a la The Dark or Arabian Nights).
     *
     * <p>
     * The icon returned should be style-free and expressed solely in "stuff"
     * that can be filled (paths, text, shapes).
     */
    Icon getIconField()

    /**
     * The symbol of the set's icon, which is laid over the field based on
     * different composition rules.  If not provided, a default mechanism will
     * be used based on the set's key (also ignoring the icon field, if any).
     *
     * <p>
     * The icon returned should be style-free and expressed solely in "stuff"
     * that can be filled (paths, text, shapes).
     */
    Icon getIconSymbol()

}
