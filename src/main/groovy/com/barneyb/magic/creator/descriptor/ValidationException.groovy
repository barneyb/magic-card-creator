package com.barneyb.magic.creator.descriptor
/**
 *
 * @author bboisvert
 */
class ValidationException extends Exception {

    final Card card
    final String propertyName

    def ValidationException(Card card, String prop, String message) {
        super(message)
        this.card = card
        this.propertyName = prop
    }

}
