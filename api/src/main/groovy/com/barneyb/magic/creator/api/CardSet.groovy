package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface CardSet {

    String getTitle()

    String getKey()

    List<Card> getCards()

    String getCopyright()

}
