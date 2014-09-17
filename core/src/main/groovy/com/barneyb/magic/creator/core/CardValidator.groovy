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
        def ctx = new BaseValidator.Ctx<Card>(card)
        validateCastingCost(ctx)
        ctx.messages
    }

    protected void validateCastingCost(BaseValidator.Ctx<Card> ctx) {
        ctx.prop = 'castingCost'
        def cost = ctx.item.castingCost
        if (ctx.item.isType("land")) {
            if (cost != null && ! cost.empty) {
                ctx.error("Lands MUST NOT have a casting cost")
            }
        } else {
            if (cost == null || cost.empty) {
                ctx.error("Spells MUST have a casting cost")
            }
        }
    }

}
