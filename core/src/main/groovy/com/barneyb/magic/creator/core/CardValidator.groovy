package com.barneyb.magic.creator.core
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.ValidationMessage
import com.barneyb.magic.creator.api.Validator
/**
 *
 *
 * @author barneyb
 */
class CardValidator implements Validator<Card> {

    Collection<ValidationMessage<Card>> validate(Card card) {
        []
    }

}
