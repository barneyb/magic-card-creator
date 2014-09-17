package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface CardSet extends Keyed {

    String getTitle()

    List<Card> getCards()

    String getCopyright()

}
