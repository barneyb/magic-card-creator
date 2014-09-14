package com.barneyb.magic.creator.core
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.ValidationMessage
/**
 *
 *
 * @author barneyb
 */
class CardSetValidator extends BaseValidator<CardSet> {

    Collection<ValidationMessage<CardSet>> validate(CardSet set) {
        def ctx = new Ctx<CardSet>(set)
        validateTitle(ctx)
        validateKey(ctx)
        validateCopyright(ctx)
        validateCards(ctx)
        ctx.messages
    }

    protected void validateTitle(Ctx<CardSet> ctx) {
        ctx.prop = 'title'
        nullOrEmpty(ctx.&warning, 'Title', ctx.item.title)
    }

    protected void validateKey(Ctx<CardSet> ctx) {
        ctx.prop = 'key'
        nullOrEmpty(ctx.&error, 'Key', ctx.item.key)
    }

    protected void validateCopyright(Ctx<CardSet> ctx) {
        ctx.prop = 'copyright'
        nullOrEmpty(ctx.&warning, 'Copyright', ctx.item.copyright)
    }

    protected void validateCards(Ctx<CardSet> ctx) {
        ctx.prop = 'cards'
        def cards = ctx.item.cards
        if (cards.size() == 0) {
            ctx.error("Cardsets must contain at least one card")
        }
    }

}
